package com.github.lessjava.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.lessjava.exceptions.InvalidProgramException;

/**
 * Stores name-to-symbol-info mappings for a single lexical scope. Symbol tables
 * are generated using a simple AST traversal algorithm, and are used during
 * code generation to look up type and location information for individual
 * symbols.
 */
public class SymbolTable {
    private SymbolTable parent;
    private Map<String, List<Symbol>> localTable;
    private List<Symbol> localSymbols;

    /**
     * Create a new, empty symbol table with no parent table.
     */
    public SymbolTable() {
        this(null);
    }

    /**
     * Create a new, empty symbol table.
     *
     * @param parent
     *            Reference to parent table for enclosing scope.
     */
    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
        this.localTable = new HashMap<>();
        this.localSymbols = new ArrayList<>();
    }

    /**
     * Inserts a new symbol into the symbol table
     *
     * @param name
     *            Decaf symbol name
     * @param symbol
     *            Symbol information
     * @throws InvalidProgramException
     *             Thrown if the symbol is already defined.
     */
    public void insert(String name, Symbol symbol) throws InvalidProgramException {
        this.localSymbols.add(symbol);
        this.localTable.computeIfAbsent(name, unused -> new ArrayList<>()).add(symbol);
    }

    /**
     * Retrieves symbol information for a given symbol name.
     *
     * @param name
     *            Decaf symbol name
     * @return Symbol information from either this table or a parent table
     * @throws InvalidProgramException
     *             Thrown if the symbol is not found
     */
    public List<Symbol> lookup(String name) throws InvalidProgramException {
        if (localTable.containsKey(name)) {
            return localTable.get(name);
        } else if (parent != null) {
            return parent.lookup(name);
        } else {
            throw new InvalidProgramException("Symbol not found: \"" + name + "\"");
        }
    }

    //public List<Symbol> lookupVariable(String name) throws InvalidProgramException {
        //if (localTable.containsKey(name)) {
            //return localTable.get(name);
        //} else if (parent != null) {
            //return parent.lookup(name);
        //} else {
            //throw new InvalidProgramException("Symbol not found: \"" + name + "\"");
        //}
    //}

    /**
     * Get accessible symbol count
     *
     * @return Count of all symbols accessible in this scope
     */
    public int size() {
        return getAllSymbols().size();
    }

    /**
     * Gets all symbols defined defined in this scope, in the order in which they
     * were added.
     *
     * @return List of all symbols defined in this scope
     */
    public List<Symbol> getSymbols() {
        return localSymbols;
    }

    /**
     * Gets all symbols accessible in this scope that are not defined in this scope;
     * i.e., all symbols that are defined in a parent table.
     *
     * @return List of symbols accessible but not defined in this scope
     */
    public List<Symbol> getInheritedSymbols() {
        List<Symbol> allSymbols = new ArrayList<Symbol>();
        if (parent != null) {
            for (Symbol s : parent.getAllSymbols()) {
                try {
                    if (lookup(s.name) == s) {
                        allSymbols.add(s);
                    }
                } catch (InvalidProgramException ex) {
                    // ignore
                }
            }
        }
        return allSymbols;
    }

    /**
     * Gets all symbols accessible in this scope, which includes all symbols defined
     * at this level ({@link getSymbols()}) plus all symbols defined in any parent
     * symbol table.
     *
     * @return List of symbols accessible in this scope
     */
    public List<Symbol> getAllSymbols() {
        List<Symbol> allSymbols = new ArrayList<Symbol>();
        allSymbols.addAll(getSymbols());
        allSymbols.addAll(getInheritedSymbols());
        return allSymbols;
    }

    /**
     * Helper method to create indentation strings
     *
     * @param level
     *            Indentation level
     * @return String with the given number of two-space indentations
     */
    private String indent(int level) {
        StringBuffer str = new StringBuffer();
        while (level > 0) {
            str.append("  ");
            level--;
        }
        return str.toString();
    }

    /**
     * Builds a nicely-formatted list of new symbols declared from this scope.
     */
    @Override
    public String toString() {
        return toString(0);
    }

    /**
     * Builds a nicely-formatted list of new symbols declared in this scope.
     *
     * @param level
     *            Number of two-space indentations before each symbol.
     */
    public String toString(int level) {
        StringBuffer str = new StringBuffer();
        str.append(indent(level) + "SYM TABLE:");
        for (Symbol s : getSymbols()) {
            str.append("\n" + indent(level) + "  + " + s.toString());
        }
        return str.toString();
    }
}
