package com.github.lessjava.types.inference.impl;

import com.github.lessjava.types.inference.HMType;

public class HMTypeSet extends HMTypeCollection {
    public static final String SET = "Set";

    public HMTypeSet(HMType elementType) {
        super(SET, elementType);
        super.collectionName = SET;
    }

    @Override
    public String toString() {
        return String.format("HashSet<%s>", elementType);
    }
}
