package com.github.lessjava.types;

import java.util.List;

import com.github.lessjava.types.ast.ASTAbstractFunction.Parameter;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.inference.HMType;

public class Symbol {
    /**
     * Name in source code
     */
    public String name;

    /**
     * True if a concrete instance of a function
     */
    public boolean concrete;

    /**
     * Data type
     */
    public HMType type;

    /**
     * Data types of formal parameters (for function symbols, empty for others)
     */
    public List<HMType> paramTypes;

    public ASTFunction function;
    public ASTVariable variable;
    public Parameter parameter;

    public Symbol(ASTVariable variable, String name, HMType type) {
        this.variable = variable;
        this.name = name;
        this.type = type;
    }

    public Symbol(Parameter parameter, String name, HMType type) {
        this.parameter = parameter;
        this.name = name;
        this.type = type;
    }

    /**
     * Create a new function symbol
     *
     * @param name
     *            Name in source code
     * @param returnType
     *            Function return type ({@link ASTNode.DataType})
     * @param paramTypes
     *            List of formal parameter data types
     */
    public Symbol(ASTFunction function, String name, HMType returnType, List<HMType> paramTypes, boolean concrete) {
        this.function = function;
        this.name = name;
        this.type = returnType;
        this.paramTypes = paramTypes;
        this.concrete = concrete;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Simplified string representation
     */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append(name);
        str.append(" : ");
        str.append(type.toString());

        return str.toString();
    }
}
