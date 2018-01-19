package com.github.lessjava.types.inference.impl;

import com.github.lessjava.types.inference.HMType;

public class HMTypeList extends HMTypeCollection {
    public static final String LIST = "List";

    public HMTypeList(HMType elementType) {
        super(LIST, elementType);
        super.collectionName = LIST;
        super.isConcrete = true;
    }

    @Override
    public String toString() {
        return String.format("ArrayList<%s>", elementType instanceof HMTypeVar ? "Object" : elementType);
    }

}
