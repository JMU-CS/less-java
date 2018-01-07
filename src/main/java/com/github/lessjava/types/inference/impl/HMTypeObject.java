package com.github.lessjava.types.inference.impl;

import com.github.lessjava.types.inference.HMType;

public abstract class HMTypeObject extends HMType {
    public String className;

    public HMTypeObject(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        //return String.format("T%s", this.uniqueVarId);
        return String.format("Object: %s", className);

    }
}
