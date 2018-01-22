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

    private static final String LIST = "List";
    private static final String SET = "Set";
    private static final String MAP = "Map";

    private static Set<ASTMethod> createListMethods() {
        ASTMethod add = new ASTMethod("add", HMTypeBase.VOID, null);
        ASTMethod push = new ASTMethod("push", HMTypeBase.VOID, null);
        ASTMethod enqueue = new ASTMethod("enqueue", HMTypeBase.VOID, null);
        ASTMethod remove = new ASTMethod("remove", HMTypeBase.BOOL, null);
        ASTMethod pop = new ASTMethod("removeAt", new HMTypeVar(), null);
        ASTMethod dequeue = new ASTMethod("removeAt", new HMTypeVar(), null);
        // TODO: How do I handle pop??
        dequeue.internalArguments.add(new ASTLiteral(HMType.BaseDataType.INT, Integer.valueOf(0)));

        ASTMethod insert = new ASTMethod("insert", HMTypeBase.VOID, null);
        ASTMethod removeAt = new ASTMethod("removeAt", new HMTypeVar(), null);
        ASTMethod get = new ASTMethod("get", new HMTypeVar(), null);
        ASTMethod set = new ASTMethod("set", new HMTypeVar(), null);
        ASTMethod size = new ASTMethod("size", HMTypeBase.INT, null);

        Set<ASTMethod> methods = new HashSet<ASTMethod>() {
            private static final long serialVersionUID = 1L;

            {
                add(add);
                add(push);
                add(enqueue);
                add(remove);
                add(pop);
                add(dequeue);
                add(insert);
                add(removeAt);
                add(get);
                add(set);
                add(size);
            }
        };

        return methods;
    }

    private static Set<ASTMethod> createSetMethods() {
        ASTMethod add = new ASTMethod("add", HMTypeBase.VOID, null);
        ASTMethod remove = new ASTMethod("remove", HMTypeBase.BOOL, null);
        ASTMethod contains = new ASTMethod("contains", HMTypeBase.BOOL, null);
        ASTMethod size = new ASTMethod("size", HMTypeBase.INT, null);

        Set<ASTMethod> methods = new HashSet<ASTMethod>() {
            private static final long serialVersionUID = 1L;

            {
                add(add);
                add(remove);
                add(contains);
                add(size);
            }
        };

        return methods;
    }

    private static Set<ASTMethod> createMapMethods() {
        ASTMethod put = new ASTMethod("put", HMTypeBase.VOID, null);
        ASTMethod get = new ASTMethod("get", new HMTypeVar(), null);
        ASTMethod contains = new ASTMethod("contains", HMTypeBase.BOOL, null);
        ASTMethod size = new ASTMethod("size", HMTypeBase.INT, null);

        Set<ASTMethod> methods = new HashSet<ASTMethod>() {
            private static final long serialVersionUID = 1L;

            {
                add(put);
                add(get);
                add(contains);
                add(size);
            }
        };

        return methods;
    }

    private static void addListTranslations() {
        methodTranslations.put(String.format("%spush", LIST), "add");
        methodTranslations.put(String.format("%senqueue", LIST), "add");
        methodTranslations.put(String.format("%sinsert", LIST), "add");
        methodTranslations.put(String.format("%sremoveAt", LIST), "remove");
        methodTranslations.put(String.format("%spop", LIST), "remove");
        methodTranslations.put(String.format("%sdequeue", LIST), "remove");
    }

    private static void addSetTranslations() {
    }

    private static void addMapTranslations() {
        methodTranslations.put(String.format("%scontains", MAP), "containsKey");
    }

    static {
        ASTClass list = new ASTClass("List", null, createListMethods());
        ASTClass set = new ASTClass("Set", null, createSetMethods());
        ASTClass map = new ASTClass("Map", null, createMapMethods());

        classes.add(list);
        classes.add(set);
        classes.add(map);

        addListTranslations();
        addSetTranslations();
        addMapTranslations();
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
