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
        ASTClassBlock listBlock = new ASTClassBlock(null, createListMethods());
        ASTClassBlock setBlock = new ASTClassBlock(null, createSetMethods());
        ASTClassBlock mapBlock = new ASTClassBlock(null, createMapMethods());

        ASTClass list = new ASTClass(new ASTClassSignature("List"),listBlock);
        ASTClass set = new ASTClass(new ASTClassSignature("Set"), setBlock);
        ASTClass map = new ASTClass(new ASTClassSignature("Map"), mapBlock);

        classes.add(list);
        classes.add(set);
        classes.add(map);
    }

    public ASTClassSignature signature;
    public ASTClassBlock block;

    public ASTClass(ASTClassSignature signature, ASTClassBlock block) {
        this.signature = signature;
        this.block = block;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        signature.traverse(visitor);
        block.traverse(visitor);
        visitor.postVisit(this);
    }
}
