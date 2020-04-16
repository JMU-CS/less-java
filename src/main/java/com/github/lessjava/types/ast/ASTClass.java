package com.github.lessjava.types.ast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeSet;
import com.github.lessjava.types.inference.impl.HMTypeVar;

public class ASTClass extends ASTNode {
    public static Map<String, ASTClass> nameClassMap = new HashMap<>();
    public static Set<ASTClass> libraryClasses = new HashSet<>();
    public static Map<String, String> methodTranslations = new HashMap<>();

    public static String PUBLIC = "public";
    public static String PRIVATE = "private";

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
                add("set", new HMTypeVar());
                add("size", HMTypeBase.INT);
            }

            public void add(String name, HMType type) {
                add(new ASTMethod(PUBLIC, new ASTFunction(name, type, null), "List"));
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
                add(new ASTMethod(PUBLIC, new ASTFunction(name, type, null), "Set"));
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
                add("keys", new HMTypeSet(new HMTypeVar()));
            }

            public void add(String name, HMType type) {
                add(new ASTMethod(PUBLIC, new ASTFunction(name, type, null), "Map"));
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

        libraryClasses.add(list);
        libraryClasses.add(set);
        libraryClasses.add(map);
    }

    public ASTClassSignature signature;
    public ASTClassBlock block;
    public ASTClass parent;

    private Set<ASTVariable> members;
    private Set<ASTMethod> methods;

    public ASTClass(ASTClassSignature signature, ASTClassBlock block) {
        this.signature = signature;
        this.block = block;
        this.parent = null;

        if (!libraryClasses.contains(this)) {
            nameClassMap.put(signature.className, this);
        }
    }

    /**
     * Return true iff the class has a constructor that takes no parameters.
     *
     * @return true iff the class has a constructor that takes no parameters
     */
    public boolean hasEmptyConstructor() {
        return block.constructors.stream().map(c -> c.parameters).anyMatch(List::isEmpty);
    }

    public boolean hasMethod(String name) {
        return getMethod(name) != null;
    }

    public ASTMethod getMethod(String name) {
        if (methods == null) {
            methods = this.block.methods;
        }
        ASTMethod method = methods.stream()
                                  .filter(m -> m.name.equals(name))
                                  .findAny()
                                  .orElse(null);

        if (method == null && this.parent != null) {
            method = this.parent.getMethod(name);
        }

        return method;
    }

    public boolean hasAttribute(String name) {
        return getAttribute(name) != null;
    }

    public ASTVariable getAttribute(String name) {
        if (members == null) {
            members = this.block.classAttributes.stream().map(a -> a.assignment.variable).collect(Collectors.toSet());
        }

        ASTVariable attribute = members.stream().filter(v -> v.name.equals(name)).findAny().orElse(null);

        if (attribute == null && this.parent != null) {
            attribute = this.parent.getAttribute(name);
        }

        return attribute;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        signature.traverse(visitor);
        block.traverse(visitor);
        visitor.postVisit(this);
    }
}
