package com.github.lessjava.types.inference.impl;

import com.github.lessjava.types.inference.HMType;

public abstract class HMTypeObject extends HMType {
    public String className;

    public HMTypeObject(String className) {
        this.className = className;
        super.isConcrete = false;
    }

    @Override
    public String toString() {
        return String.format("Object: %s", className);

    }
}
