package com.github.lessjava.visitor.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.lessjava.exceptions.InvalidProgramException;
import com.github.lessjava.types.Symbol;
import com.github.lessjava.types.SymbolTable;
import com.github.lessjava.types.ast.ASTAbstractFunction.Parameter;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.types.ast.ASTForLoop;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTMemberAccess;
import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.impl.HMTypeClass;
import com.github.lessjava.types.inference.impl.HMTypeVar;
import com.github.lessjava.visitor.LJAbstractAssignTypes;

/**
 * Static analysis pass to construct symbol tables. Visits an AST, maintaining a
 * stack of active symbol tables and annotating various AST nodes with the
 * appropriate symbol tables.
 */
public class BuildSymbolTables extends LJAbstractAssignTypes {
    public static final String SYMBOL_TABLE = "symbolTable";

    public static Map<ASTNode, SymbolTable> nodeSymbolTableMap = new HashMap<>();

    /**
     * Stack of symbol tables, representing all active nested scopes.
     */
    protected Deque<SymbolTable> tableStack;
    protected Map<String, ASTVariable> nameVarMap;

    /**
     * Retrieves symbol information for a given symbol name. Searches for symbol
     * tables up the parent tree if there is no table at the given node. Returns
     * null if the symbol cannot be found.
     *
     */
    public static List<Symbol> searchScopesForSymbol(ASTNode node, String name, Symbol.SymbolType symbolType) {
        List<Symbol> symbols = new ArrayList<>();
        try {
            if (node.attributes.containsKey(SYMBOL_TABLE)) {
                SymbolTable table = (SymbolTable) node.attributes.get(SYMBOL_TABLE);
                symbols = table.lookup(name, symbolType);
            }

            if (symbols.isEmpty() && node.getParent() != null) {
                symbols = searchScopesForSymbol(node.getParent(), name, symbolType);
            }
        } catch (InvalidProgramException ex) {
        }
        return symbols;
    }

    public BuildSymbolTables() {
        tableStack = new ArrayDeque<SymbolTable>();
        nameVarMap = new HashMap<>();
    }

    /**
     * Return the innermost active symbol table.
     */
    protected SymbolTable getCurrentTable() {
        assert (tableStack.size() > 0);
        return tableStack.peek();
    }

    /**
     * Return the previous active symbol table.
     */
    protected SymbolTable getPreviousTable() {
        assert (tableStack.size() > 0);

        SymbolTable current = tableStack.pop();
        SymbolTable previous = (tableStack.size() > 0) ? tableStack.pop() : current;

        tableStack.push(previous);

        if (previous != current) {
            tableStack.push(current);
        }

        return previous;
    }

    /**
     * Create a new innermost symbol table scope and push it on the stack.
     */
    protected SymbolTable initializeScope() {
        SymbolTable table = null;
        if (tableStack.size() > 0) {
            table = new SymbolTable(getCurrentTable());
        } else {
            table = new SymbolTable();
        }
        tableStack.push(table);
        return table;
    }

    /**
     * Pop the stack and move outwards one scope level.
     */
    protected void finalizeScope() {
        assert (tableStack.size() > 0);
        tableStack.pop();
    }

    /**
     * Add a symbol for the given function to the current (innermost) scope.
     */
    protected void insertFunctionSymbol(ASTFunction node) {
        boolean isConcrete = node.returnType instanceof HMTypeVar;
        List<HMType> ptypes = new ArrayList<>();
        for (ASTFunction.Parameter p : node.parameters) {
            ptypes.add(p.type);

            if (p.type instanceof HMTypeVar) {
                isConcrete = false;
            }
        }
        Symbol symbol = new Symbol(node, node.name, node.returnType, ptypes, isConcrete);

        getCurrentTable().insert(node.name, symbol);
    }

    /**
     * Insert a symbol for a variable into the current symbol table. If the current symbol is
     * already present in the table, unify its type with the new one to get updated type information.
     *
     * @param node the variable to add a symbol for
     */
    protected void insertVariableSymbol(ASTVariable node) {
        if(!(node.type instanceof HMTypeVar)) {
            List<Symbol> symbols = searchScopesForSymbol(node, node.name, Symbol.SymbolType.VARIABLE);

            HMType type = node.type;

            if (symbols != null && !symbols.isEmpty()) {
                for(Symbol s : symbols) {
                    type = unify(node, s.type, type);
                }
                getCurrentTable().removeSymbol(node.name, Symbol.SymbolType.VARIABLE);
            }

            Symbol symbol = new Symbol(node, node.name, type);
            getCurrentTable().insert(node.name, symbol);
        }
    }

    /**
     * Add a symbol for the given function parameter to the current (innermost)
     * scope.
     */
    protected void insertParamSymbol(Parameter p) {
        Symbol symbol = new Symbol(p, p.name, p.type);
        getCurrentTable().insert(p.name, symbol);
    }

    @Override
    public void preVisit(ASTProgram node) {
        node.attributes.put(SYMBOL_TABLE, initializeScope());
    }

    @Override
    public void postVisit(ASTProgram node) {
        nodeSymbolTableMap.put(node, getCurrentTable());
        finalizeScope();
    }

    @Override
    public void preVisit(ASTClass node) {
        node.attributes.put(SYMBOL_TABLE, initializeScope());
        ASTVariable thisVar = new ASTVariable("this");
        thisVar.type = new HMTypeClass(node.signature.className);
        insertVariableSymbol(thisVar);
        ASTVariable superVar = new ASTVariable("super");
        if(node.signature.superName != null) {
            superVar.type = new HMTypeClass(node.signature.superName);
        } else {
            superVar.type = new HMTypeClass("Object");
        }
        insertVariableSymbol(superVar);
    }

    @Override
    public void postVisit(ASTClass node) {
        nodeSymbolTableMap.put(node, getCurrentTable());
        finalizeScope();
    }

    @Override
    public void preVisit(ASTForLoop node) {
        node.attributes.put(SYMBOL_TABLE, initializeScope());
    }

    @Override
    public void postVisit(ASTForLoop node) {
        nodeSymbolTableMap.put(node, getCurrentTable());
        finalizeScope();
    }

    @Override
    public void preVisit(ASTFunction node) {
        insertFunctionSymbol(node);
        node.attributes.put(SYMBOL_TABLE, initializeScope());
        node.parameters.forEach(this::insertParamSymbol);
    }

    @Override
    public void postVisit(ASTFunction node) {
        nodeSymbolTableMap.put(node, getCurrentTable());
        finalizeScope();
    }

    @Override
    public void preVisit(ASTBlock node) {
        node.attributes.put(SYMBOL_TABLE, initializeScope());
    }

    @Override
    public void postVisit(ASTBlock node) {
        nodeSymbolTableMap.put(node, getCurrentTable());
        finalizeScope();
    }

    @Override
    public void postVisit(ASTVariable node) {
        // Don't insert a symbol for the member in a member access
        if(node.getParent() instanceof ASTMemberAccess && ((ASTMemberAccess)node.getParent()).var == node) {
            return;
        }

        // Only insert a symbol if we're not indexing into a list
        if(node.index == null) {
            insertVariableSymbol(node);
        }
    }
}
