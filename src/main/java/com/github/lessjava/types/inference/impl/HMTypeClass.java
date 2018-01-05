package com.github.lessjava.types.inference.impl;

import java.util.HashSet;

import com.github.lessjava.types.inference.HMType;

public class HMTypeClass extends HMType {
    public String name;

    public HashSet<String> methods;

    public HMTypeClass(String name, HashSet<String> methods) {
        this.name = name;
        this.methods = methods;
    }

    @Override
    public HMType clone() {
        return new HMTypeClass(this.name, new HashSet<>(this.methods));
    }
}
