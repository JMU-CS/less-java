package com.github.lessjava.ast;
import java.util.*;

/**
 * Decaf function declaration. Contains a name, a return type, a list of formal
 * parameters, and a function body (block).
 */
public class ASTFunction extends ASTNode
{
    /**
     * Decaf formal parameter (name and data type).
     */
    public static class Parameter
    {
        public String name;
        public ASTNode.DataType type;

        public Parameter(String name, ASTNode.DataType type)
        {
            this.name = name;
            this.type = type;
        }

        public Parameter(String name) {
            this(name, null);
        }
    }

    public String name;
    public ASTNode.DataType returnType;
    public ASTBlock body;

    public List<Parameter> parameters;

    public ASTFunction(String name, ASTNode.DataType returnType, ASTBlock body)
    {
        this.name = name;
        this.returnType = returnType;
        this.body = body;
        this.parameters = new ArrayList<Parameter>();
    }

    public ASTFunction(String name, ASTBlock body) {
        this(name, ASTNode.DataType.UNKNOWN, body);
    }

    public String getParameterStr()
    {
        StringBuffer params = new StringBuffer();
        params.append("(");
        for (Parameter p : parameters) {
            if (params.length() > 1) {
                params.append(", ");
            }
            params.append(p.name + ":" + ASTNode.typeToString(p.type));
        }
        params.append(")");
        return params.toString();
    }

    @Override
    public void traverse(ASTVisitor visitor)
    {
        visitor.preVisit(this);
        body.traverse(visitor);
        visitor.postVisit(this);
    }
}

