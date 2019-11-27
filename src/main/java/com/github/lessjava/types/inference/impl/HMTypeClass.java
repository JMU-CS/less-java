package com.github.lessjava.types.inference.impl;

import java.util.HashSet;
import java.util.Set;

import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.inference.HMType;

public class HMTypeClass extends HMType {
    public String name;

    private Set<String> methods;

    public HMTypeClass(String name, HashSet<String> methods) {
        this.name = name;
        this.methods = methods;
        this.isConcrete = true;
    }

    public HMTypeClass(String name) {
        this.name = name;
        this.methods = new HashSet<>();
        this.isConcrete = true;
    }

    public void addMethod(ASTFunction method) {
        methods.add(method.getIdentifyingString());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public HMType clone() {
        return new HMTypeClass(this.name, new HashSet<>(this.methods));
    }
}
