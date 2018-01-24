package com.github.lessjava.types.ast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeVar;

public class ASTClass extends ASTNode {
    public static Set<ASTClass> classes = new HashSet<>();
    public static Map<String, String> methodTranslations = new HashMap<>();

    private static Set<ASTMethod> createListMethods() {
        Set<ASTMethod> methods = new HashSet<ASTMethod>() {
            private static final long serialVersionUID = 1L;

            {
                add("add", HMTypeBase.VOID);
                add("push", HMTypeBase.VOID);
                add("enqueue", HMTypeBase.VOID);
                add("remove", HMTypeBase.BOOL);
                add("pop", new HMTypeVar());
                add("dequeue", new HMTypeVar());
                add("insert", HMTypeBase.VOID);
                add("removeAt", new HMTypeVar());
                add("get", new HMTypeVar());
                add("set", HMTypeBase.VOID);
                add("size", HMTypeBase.INT);
            }

            public void add(String name, HMType type) {
                add(new ASTMethod(name, type, null));
            }
        };

        return methods;
    }

    private static Set<ASTMethod> createSetMethods() {
        Set<ASTMethod> methods = new HashSet<ASTMethod>() {
            private static final long serialVersionUID = 1L;

            {
                add("add", HMTypeBase.VOID);
                add("remove", HMTypeBase.BOOL);
                add("contains", HMTypeBase.BOOL);
                add("size", HMTypeBase.INT);
            }

            public void add(String name, HMType type) {
                add(new ASTMethod(name, type, null));
            }
        };

        return methods;
    }

    private static Set<ASTMethod> createMapMethods() {
        Set<ASTMethod> methods = new HashSet<ASTMethod>() {
            private static final long serialVersionUID = 1L;

            {
                add("put", HMTypeBase.VOID);
                add("get", new HMTypeVar());
                add("contains", HMTypeBase.BOOL);
                add("size", HMTypeBase.INT);
            }

            public void add(String name, HMType type) {
                add(new ASTMethod(name, type, null));
            }
        };

        return methods;
    }

    static {
        ASTClass list = new ASTClass("List", null, createListMethods());
        ASTClass set = new ASTClass("Set", null, createSetMethods());
        ASTClass map = new ASTClass("Map", null, createMapMethods());

        classes.add(list);
        classes.add(set);
        classes.add(map);
    }

    public String name;
    public Set<ASTVariable> attributes;
    public Set<ASTMethod> methods;

    public ASTClass(String name, Set<ASTVariable> attributes, Set<ASTMethod> methods) {
        this.name = name;
        this.attributes = attributes == null ? new HashSet<>() : attributes;
        this.methods = methods == null ? new HashSet<>() : methods;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);

        if (attributes != null) {
            for (ASTVariable attribute : attributes) {
                attribute.traverse(visitor);
            }
        }

        if (methods != null) {
            for (ASTMethod method : methods) {
                method.traverse(visitor);
            }
        }

        visitor.postVisit(this);
    }
}
