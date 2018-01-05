package com.github.lessjava.types.inference.impl;

import com.github.lessjava.types.inference.HMType;

public class HMTypeSet extends HMTypeCollection {
    public HMTypeSet(HMType elementType) {
        super(elementType);
        super.collectionType = "Set";
        super.toString = String.format("HashSet<%s>", elementType);
    }
}
