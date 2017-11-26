package com.github.lessjava.types.ast;

import java.util.ArrayList;
import java.util.List;

import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.impl.HMTypeVar;

/**
 * Decaf function declaration. Contains a name, a return type, a list of formal
 * parameters, and a function body (block).
 */
public class ASTFunction extends ASTNode
{
    /**
     * Decaf formal parameter (name and data type).
     */
    public static class Parameter implements Cloneable
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
        
        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Parameter other = (Parameter) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (type == null) {
                if (other.type != null)
                    return false;
            } else if (!type.equals(other.type))
                return false;
            return true;
        }
        
        @Override
        public Object clone() {
            return new Parameter(this.name, this.type);
        }
        
        @Override
        public String toString()
        {
            return String.format("%s %s", (type != null) ? type.toString() : "unused", name);
        }
    }

    public String   name;
    public HMType   returnType;
    public ASTBlock body;

    public List<Parameter> parameters;

    public boolean concrete;

    public ASTFunction(String name, HMType returnType, ASTBlock body)
    {
        this.name = name;
        this.returnType = returnType;
        this.body = body;
        this.parameters = new ArrayList<Parameter>();
    }

    public ASTFunction(String name, ASTBlock body)
    {
        this(name, new HMTypeVar(), body);
    }

    public String getParameterStr()
    {
        StringBuffer params = new StringBuffer();
        params.append("(");
        for (Parameter p : parameters) {
            if (params.length() > 1) {
                params.append(", ");
            }
            String paramString = (p.type != null) ? String.format("%s:%s", p.name,
                                                             p.type.toString())
                                             : String.format("%s:unused",
                                                             p.name);
            params.append(paramString);
        }
        params.append(")");
        return params.toString();
    }

    public String getNameParamString() {
	StringBuilder sb = new StringBuilder(name);
	for (Parameter e: parameters) {
	    sb.append(e.type.toString() + ",");
	}
	
	return sb.toString();
    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ASTFunction other = (ASTFunction) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (parameters == null) {
            if (other.parameters != null)
                return false;
        } else if (!parameters.equals(other.parameters))
            return false;
        if (returnType == null) {
            if (other.returnType != null)
                return false;
        } else if (!returnType.equals(other.returnType))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return String.format("Function: %s, %s", this.name, this.returnType);
    }

    @Override
    public void traverse(ASTVisitor visitor)
    {
        visitor.preVisit(this);
        // Library functions have null bodies
        if (body != null) {
            body.traverse(visitor);
        }
        visitor.postVisit(this);
    }
}
