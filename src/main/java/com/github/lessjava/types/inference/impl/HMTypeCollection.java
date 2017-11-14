package com.github.lessjava.types.inference.impl;

import com.github.lessjava.types.inference.HMType;

public class HMTypeCollection extends HMType {
    public HMType collectionType;

    public HMTypeCollection(HMType collectionType) {
	this.collectionType = collectionType;
    }
    
    @Override
    public String toString() {
	return String.format("ArrayDeque<%s>", this.collectionType.toString());
    }
}
