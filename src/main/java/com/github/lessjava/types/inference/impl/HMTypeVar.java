package com.github.lessjava.types.inference.impl;

import com.github.lessjava.types.inference.HMType;

public class HMTypeVar extends HMType {
    public HMTypeVar() {
        this.isConcrete = false;
    }

    @Override
    public String toString() {
        return String.format("T");

    }

    @Override
    public HMTypeVar clone() {
        return new HMTypeVar();
    }
}
