package com.github.lessjava.types.inference.impl;

import com.github.lessjava.types.inference.HMType;

public class HMTypeList extends HMTypeCollection {
    public HMTypeList(HMType elementType) {
        super(elementType);
        super.collectionType = "List";
    }

    @Override
    public String toString() {
        return String.format("ArrayDeque<%s>", elementType);
    }
}
