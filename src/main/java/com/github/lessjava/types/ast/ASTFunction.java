package com.github.lessjava.types.ast;

import java.util.ArrayList;
import java.util.List;

import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.HMType.BaseDataType;
import com.github.lessjava.types.inference.impl.HMTypeBase;

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
        public HMType type;

        public Parameter(String name, HMType type)
        {
            this.name = name;
            this.type = type;
        }

        public Parameter(String name)
        {
            this(name, null);
        }
        
        @Override
        public String toString() {
            return String.format("%s %s", HMType.typeToString(type), name);
        }
    }

    public String   name;
    public HMType   returnType;
    public ASTBlock body;

    public List<Parameter> parameters;

    public HMType hmType;

    public ASTFunction(String name, HMType returnType, ASTBlock body)
    {
        this.name = name;
        this.returnType = returnType;
        this.body = body;
        this.parameters = new ArrayList<Parameter>();
    }

    public ASTFunction(String name, ASTBlock body)
    {
        this(name, new HMTypeBase(BaseDataType.UNKNOWN), body);
    }

    public String getParameterStr()
    {
        StringBuffer params = new StringBuffer();
        params.append("(");
        for (Parameter p : parameters) {
            if (params.length() > 1) {
                params.append(", ");
            }
            params.append(p.name + ":" + HMType.typeToString(p.type));
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
