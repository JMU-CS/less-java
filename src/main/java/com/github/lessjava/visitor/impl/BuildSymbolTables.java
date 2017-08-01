package com.github.lessjava.visitor.impl;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.github.lessjava.ast.ASTBlock;
import com.github.lessjava.ast.ASTFunction;
import com.github.lessjava.ast.ASTNode;
import com.github.lessjava.ast.ASTProgram;
import com.github.lessjava.ast.ASTVariable;
import com.github.lessjava.exceptions.InvalidProgramException;
import com.github.lessjava.types.Symbol;
import com.github.lessjava.types.SymbolTable;

/**
 * Static analysis pass to construct symbol tables. Visits an AST, maintaining
 * a stack of active symbol tables and annotating various AST nodes with the
 * appropriate symbol tables.
 */
public class BuildSymbolTables extends StaticAnalysis
{
    /**
     * Stack of symbol tables, representing all active nested scopes.
     */
    protected Deque<SymbolTable> tableStack;

    public BuildSymbolTables()
    {
        tableStack = new ArrayDeque<SymbolTable>();
    }

    /**
     * Return the innermost active symbol table.
     */
    protected SymbolTable getCurrentTable()
    {
        assert(tableStack.size() > 0);
        return tableStack.peek();
    }

    /**
     * Create a new innermost symbol table scope and push it on the stack.
     */
    protected SymbolTable initializeScope()
    {
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
    protected void finalizeScope()
    {
        assert(tableStack.size() > 0);
        tableStack.pop();
    }

    /**
     * Add a symbol for the given function to the current (innermost) scope.
     */
    protected void insertFunctionSymbol(ASTFunction node)
    {
        try {
            List<ASTNode.DataType> ptypes = new ArrayList<ASTNode.DataType>();
            for (ASTFunction.Parameter p : node.parameters) {
                ptypes.add(p.type);
            }
            Symbol symbol = new Symbol(node.name, node.returnType, ptypes);
            getCurrentTable().insert(node.name, symbol);
        } catch (InvalidProgramException ex) {
            addError(ex);
        }
    }

    protected void insertVariableSymbol(ASTVariable node)
    {
        try {
            Symbol symbol = new Symbol(node.loc.name, node.type, node.isArray, node.arrayLength);
            getCurrentTable().insert(node.loc.name, symbol);
        } catch (InvalidProgramException ex) {
            addError(ex);
        }
    }

    /**
     * Add a symbol for the given function parameter to the current (innermost) scope.
     */
    protected void insertParamSymbol(ASTFunction.Parameter p)
    {
        try {
            Symbol symbol = new Symbol(p.name, p.type);
            getCurrentTable().insert(p.name, symbol);
        } catch (InvalidProgramException ex) {
            addError(ex);
        }
    }

    /**
     * Add a symbol for a hard-coded function to the current (innermost) scope.
     * Uses the given function name; the function will take one parameter of the
     * given type and return void.
     */
    private void insertPrintFunctionSymbol(String name, ASTNode.DataType type)
    {
        List<ASTNode.DataType> ptypes;
        ptypes = new ArrayList<ASTNode.DataType>();
        ptypes.add(type);                               // one param
        try {
            getCurrentTable().insert(name,
                    new Symbol(name,                    // name
                            ASTNode.DataType.VOID,      // return
                            ptypes));                   // parameters
        } catch (InvalidProgramException ex) {
            addError(ex);
        }
    }

    @Override
    public void preVisit(ASTProgram node)
    {
        node.attributes.put("symbolTable", initializeScope());
        insertPrintFunctionSymbol("print_int",  ASTNode.DataType.INT);
        insertPrintFunctionSymbol("print_bool", ASTNode.DataType.BOOL);
        insertPrintFunctionSymbol("print_str",  ASTNode.DataType.STR);
    }

    @Override
    public void postVisit(ASTProgram node)
    {
        finalizeScope();
    }

    @Override
    public void preVisit(ASTFunction node)
    {
        insertFunctionSymbol(node);
        node.attributes.put("symbolTable", initializeScope());

        for (ASTFunction.Parameter p: node.parameters) {
            insertParamSymbol(p);
        }
    }

    @Override
    public void postVisit(ASTFunction node)
    {
        finalizeScope();
    }

    @Override
    public void preVisit(ASTBlock node)
    {
        node.attributes.put("symbolTable", initializeScope());
    }

    @Override
    public void postVisit(ASTBlock node)
    {
        finalizeScope();
    }

    @Override
    public void preVisit(ASTVariable node)
    {
        insertVariableSymbol(node);
    }
}

