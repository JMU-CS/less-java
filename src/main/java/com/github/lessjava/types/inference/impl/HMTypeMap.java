package com.github.lessjava.types.inference.impl;

public class HMTypeMap extends HMTypeCollection {
    public static final String MAP = "Map";

    public HMTypeMap(HMTypeTuple entry) {
        super(MAP, entry);
        super.collectionName = MAP;
        super.isConcrete = true;
    }

    @Override
    public String toString() {
        HMTypeTuple t = (HMTypeTuple) elementType;
        return String.format("LJMap<%s, %s>", t.types.get(0), t.types.get(1));
    }
}
