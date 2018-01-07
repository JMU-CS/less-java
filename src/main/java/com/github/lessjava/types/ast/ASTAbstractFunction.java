package com.github.lessjava.types.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.HMType.BaseDataType;
import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeList;
import com.github.lessjava.types.inference.impl.HMTypeSet;
import com.github.lessjava.types.inference.impl.HMTypeVar;

public abstract class ASTAbstractFunction extends ASTNode {
    public static Map<String, String> libraryFunctionStrings = new HashMap<>();

    static {
        // Output
        libraryFunctionStrings.put("print", "System.out.print(%s);");
        libraryFunctionStrings.put("printf", "System.out.printf(%s);");
        libraryFunctionStrings.put("println", "System.out.println(%s);");

        // Input
        libraryFunctionStrings.put("readInt", "scn.nextInt");
        libraryFunctionStrings.put("readDouble", "scn.nextDouble");
        libraryFunctionStrings.put("readChar", "scn.useDelimiter(\"\").next");
        libraryFunctionStrings.put("readWord", "scn.useDelimiter(\"\\\\s+\").next");
        libraryFunctionStrings.put("readLine", "scn.nextLine");
    }

    public static HashSet<ASTFunction> functions = new HashSet<>();

    static {
        // Output
        ASTFunction print = new ASTFunction("print", new HMTypeBase(BaseDataType.VOID), null);
        ASTFunction println = new ASTFunction("println", new HMTypeBase(BaseDataType.VOID), null);

        print.parameters.add(new ASTFunction.Parameter("args", new HMTypeBase(BaseDataType.STR)));
        println.parameters.add(new ASTFunction.Parameter("args", new HMTypeBase(BaseDataType.STR)));

        functions.add(print);
        functions.add(println);

        // Input
        functions.add(new ASTFunction("readInt", new HMTypeBase(BaseDataType.INT), null));
        functions.add(new ASTFunction("readReal", new HMTypeBase(BaseDataType.REAL), null));
        functions.add(new ASTFunction("readChar", new HMTypeBase(BaseDataType.STR), null));
        functions.add(new ASTFunction("readWord", new HMTypeBase(BaseDataType.STR), null));
        functions.add(new ASTFunction("readLine", new HMTypeBase(BaseDataType.STR), null));

        // Constructors (Library Data Structures)
        functions.add(new ASTFunction("List", new HMTypeList(null), null));
        //functions.add(new ASTFunction("Stack", new HMTypeObject("Stack", new HashSet<>()), null));
        //functions.add(new ASTFunction("Queue", new HMTypeObject("Queue", new HashSet<>()), null));
        functions.add(new ASTFunction("Set", new HMTypeSet(null), null));
        //functions.add(new ASTFunction("Map", new HMTypeObject("Map", new HashSet<>()), null));
    }

    public static class Parameter {
        public String name;
        public HMType type;

        public Parameter(String name, HMType type) {
            this.name = name;
            this.type = type;
        }

        public Parameter(String name) {
            this(name, null);
        }

        public String getName() {
            return this.name;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
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

        public Parameter clone() {
            return new Parameter(this.name, this.type.clone());
        }

        @Override
        public String toString() {
            return String.format("%s %s", (type != null) ? type.toString() : "unused", name);
        }
    }

    public String name;
    public HMType returnType;
    public ASTBlock body;

    public List<Parameter> parameters;

    public boolean concrete;

    public ASTAbstractFunction(String name, HMType returnType, ASTBlock body) {
        this.name = name;
        this.returnType = returnType;
        this.body = body;
        this.parameters = new ArrayList<Parameter>();
    }

    public ASTAbstractFunction(String name, ASTBlock body) {
        this(name, new HMTypeVar(), body);
    }

    public String getParameterStr() {
        StringBuffer params = new StringBuffer();
        params.append("(");
        for (Parameter p : parameters) {
            if (params.length() > 1) {
                params.append(", ");
            }
            String paramString = (p.type != null) ? String.format("%s:%s", p.name, p.type.toString())
                    : String.format("%s:unused", p.name);
            params.append(paramString);
        }
        params.append(")");
        return params.toString();
    }

    public String getIdentifyingString() {
        StringBuilder sb = new StringBuilder(name);
        for (Parameter e : parameters) {
            sb.append(e.type.toString() + ",");
        }

        return sb.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ASTAbstractFunction other = (ASTFunction) obj;
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
}
