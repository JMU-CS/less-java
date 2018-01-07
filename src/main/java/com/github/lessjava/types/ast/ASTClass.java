package com.github.lessjava.types.ast;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeVar;

public class ASTClass extends ASTNode {
    public static Set<ASTClass> classes = new HashSet<>();

    static {
        ASTMethod size = new ASTMethod("size", HMTypeBase.INT, null);

        ASTMethod add = new ASTMethod("add", HMTypeBase.VOID, null);
        ASTMethod remove = new ASTMethod("remove", HMTypeBase.VOID, null);
        ASTMethod get = new ASTMethod("get", new HMTypeVar(), null);
        ASTMethod contains = new ASTMethod("contains", HMTypeBase.BOOL, null);
        ASTMethod put = new ASTMethod("put", HMTypeBase.BOOL, null);

        Set<ASTMethod> listMethods = new HashSet<ASTMethod>() {{add(size); add(add); add(remove); add(get);}};
        Set<ASTMethod> setMethods = new HashSet<ASTMethod>() {{add(size); add(add); add(remove); add(contains);}};
        Set<ASTMethod> mapMethods = new HashSet<ASTMethod>() {{add(size); add(put); add(get); add(contains);}};

        ASTClass list = new ASTClass("List", null, listMethods);
        ASTClass set = new ASTClass("Set", null, setMethods);
        ASTClass map = new ASTClass("Map", null, mapMethods);

        classes.add(list);
        classes.add(set);
        classes.add(map);
    }

    public String name;
    public List<ASTVariable> attributes;
    public Set<ASTMethod> methods;

    public ASTClass(String name, List<ASTVariable> attributes, Set<ASTMethod> methods) {
        this.name = name;
        this.attributes = attributes;
        this.methods = methods;
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
