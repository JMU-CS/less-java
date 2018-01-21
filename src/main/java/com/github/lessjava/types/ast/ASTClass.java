package com.github.lessjava.types.ast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeVar;

public class ASTClass extends ASTNode {
    public static Set<ASTClass> classes = new HashSet<>();
    public static Map<String, String> methodTranslations = new HashMap<>();

    static {
        ASTMethod size = new ASTMethod("size", HMTypeBase.INT, null);

        ASTMethod add = new ASTMethod("add", HMTypeBase.VOID, null);
        ASTMethod remove = new ASTMethod("remove", new HMTypeVar(), null);
        ASTMethod get = new ASTMethod("get", new HMTypeVar(), null);
        ASTMethod contains = new ASTMethod("contains", HMTypeBase.BOOL, null);
        ASTMethod put = new ASTMethod("put", HMTypeBase.BOOL, null);

        Set<ASTMethod> listMethods = new HashSet<ASTMethod>() {{add(size.clone()); add(add.clone()); add(remove.clone()); add(get.clone());}};
        Set<ASTMethod> setMethods = new HashSet<ASTMethod>() {{add(size.clone()); add(add.clone()); add(remove.clone()); add(contains.clone());}};
        Set<ASTMethod> mapMethods = new HashSet<ASTMethod>() {{add(size.clone()); add(put.clone()); add(get.clone()); add(contains.clone());}};

        ASTClass list = new ASTClass("List", null, listMethods);
        ASTClass set = new ASTClass("Set", null, setMethods);
        ASTClass map = new ASTClass("Map", null, mapMethods);

        classes.add(list);
        classes.add(set);
        classes.add(map);

        // List ---
        //add/push/enqueue
        methodTranslations.put(String.format("%sadd", list.name), "add");
        methodTranslations.put(String.format("%spush", list.name), "add");
        methodTranslations.put(String.format("%senqueue", list.name), "add");
        //insert
        methodTranslations.put(String.format("%sinsert", list.name), "add");
        //remove
        methodTranslations.put(String.format("%sremove", list.name), "remove");
        //removeAt
        methodTranslations.put(String.format("%sremoveAt", list.name), "remove");
        //pop/dequeue TODO: ADD DEFAULT ARGS TO THESE METHODS
        methodTranslations.put(String.format("%spop", list.name), "remove(size-1)");
        methodTranslations.put(String.format("%sdequeue", list.name), "remove(0)");
        //get
        methodTranslations.put(String.format("%sget", list.name), "get");
        //set
        methodTranslations.put(String.format("%sset", list.name), "set");
        //size
        methodTranslations.put(String.format("%ssize", list.name), "size");

        // Map ---
        methodTranslations.put(String.format("%scontains", map.name), "containsKey");
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
