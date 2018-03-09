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
import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.impl.HMTypeVar;

/**
 * Static analysis pass to construct symbol tables. Visits an AST, maintaining a
 * stack of active symbol tables and annotating various AST nodes with the
 * appropriate symbol tables.
 */
public class BuildSymbolTables extends StaticAnalysis {
    /**
     * Stack of symbol tables, representing all active nested scopes.
     */
    protected Deque<SymbolTable> tableStack;
    protected Map<String, ASTVariable> nameVarMap;

    public static Map<ASTNode, SymbolTable> nodeSymbolTableMap = new HashMap<>();

    /**
     * Retrieves symbol information for a given symbol name. Searches for symbol
     * tables up the parent tree if there is no table at the given node. Adds a
     * static analysis error and returns null if the symbol cannot be found.
     *
     */
    public static List<Symbol> searchScopesForSymbol(ASTNode node, String name) {
        List<Symbol> symbols = null;
        try {
            if (node.attributes.containsKey("symbolTable")) {
                SymbolTable table = (SymbolTable) node.attributes.get("symbolTable");
                symbols = table.lookup(name);
            }

            if ((symbols == null || symbols.isEmpty()) && node.getParent() != null) {
                symbols = searchScopesForSymbol(node.getParent(), name);
            }
        } catch (InvalidProgramException ex) {
            //addError(new InvalidProgramException(ex.getMessage()));
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
        try {
            boolean isConcrete = node.returnType instanceof HMTypeVar;
            List<HMType> ptypes = new ArrayList<>();
            for (ASTFunction.Parameter p : node.parameters) {
                insertParamSymbol(p);
                ptypes.add(p.type);

                if (p.type instanceof HMTypeVar) {
                    isConcrete = false;
                }
            }
            Symbol symbol = new Symbol(node, node.name, node.returnType, ptypes, isConcrete);

            getCurrentTable().insert(node.name, symbol);
        } catch (InvalidProgramException ex) {
            addError(ex);
        }
    }

    protected void insertVariableSymbol(ASTVariable node) {
        try {
            // TODO: Removing this fixes collections breaks globals
            List<Symbol> symbols = searchScopesForSymbol(node, node.name);

            //Don't add the symbol if we've already encountered it
            if(symbols != null && !symbols.isEmpty()) {
                return;
            }

            Symbol symbol = new Symbol(node, node.name, node.type);
            getCurrentTable().insert(node.name, symbol);
        } catch (InvalidProgramException ex) {
            addError(ex);
        }
    }

    /**
     * Add a symbol for the given function parameter to the current (innermost)
     * scope.
     */
    protected void insertParamSymbol(Parameter p) {
        try {
            Symbol symbol = new Symbol(p, p.name, p.type);
            getCurrentTable().insert(p.name, symbol);
        } catch (InvalidProgramException ex) {
            addError(ex);
        }
    }

    @Override
    public void preVisit(ASTProgram node) {
        node.attributes.put("symbolTable", initializeScope());
    }

    @Override
    public void postVisit(ASTProgram node) {
        nodeSymbolTableMap.put(node, getCurrentTable());
        finalizeScope();
    }

    @Override
    public void preVisit(ASTClass node) {
        node.attributes.put("symbolTable", initializeScope());
    }

    @Override
    public void postVisit(ASTClass node) {
        nodeSymbolTableMap.put(node, getCurrentTable());
        finalizeScope();
    }

    @Override
    public void preVisit(ASTForLoop node) {
        node.attributes.put("symbolTable", initializeScope());
    }

    @Override
    public void postVisit(ASTForLoop node) {
        nodeSymbolTableMap.put(node, getCurrentTable());
        finalizeScope();
    }

    @Override
    public void preVisit(ASTFunction node) {
        insertFunctionSymbol(node);
        node.attributes.put("symbolTable", initializeScope());
    }

    @Override
    public void postVisit(ASTFunction node) {
        nodeSymbolTableMap.put(node, getCurrentTable());
        finalizeScope();
    }

    @Override
    public void preVisit(ASTBlock node) {
        node.attributes.put("symbolTable", initializeScope());
    }

    @Override
    public void postVisit(ASTBlock node) {
        nodeSymbolTableMap.put(node, getCurrentTable());
        finalizeScope();
    }

    @Override
    public void postVisit(ASTVariable node) {
        if(node.name.equals("set")) {
            //System.err.println(getCurrentTable());
        }
        insertVariableSymbol(node);
    }

}
