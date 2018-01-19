package com.github.lessjava.types.inference.impl;

import com.github.lessjava.types.inference.HMType;

public class HMTypeMap extends HMTypeCollection {
    public static final String MAP = "Map";

    public HMType key;
    public HMType value;

    public HMTypeMap(HMTypeTuple entry) {
        super(MAP, entry);
        this.key = entry.types.get(0);
        this.value = entry.types.get(1);
        super.collectionName = MAP;
        super.isConcrete = true;
    }

    @Override
    public String toString() {
        return String.format("HashMap<%s, %s>", key instanceof HMTypeVar ? "Object" : key,
                value instanceof HMTypeVar ? "Object" : value);
    }
}
