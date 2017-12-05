package com.github.lessjava.types.inference.impl;

import com.github.lessjava.types.inference.HMType;

public class HMTypeCollection extends HMType {
    private HMType collectionType;

    public HMTypeCollection(HMType collectionType) {
        this.collectionType = collectionType;
    }

    /**
     * @return the collectionType
     */
    public HMType getCollectionType() {
        return collectionType;
    }

    /**
     * @param collectionType
     *            the collectionType to set
     */
    public void setCollectionType(HMType collectionType) {
        this.isConcrete = collectionType.isConcrete;
        this.collectionType = collectionType;
    }

    @Override
    public String toString() {
        return String.format("ArrayDeque<%s>", this.collectionType);
    }
}
