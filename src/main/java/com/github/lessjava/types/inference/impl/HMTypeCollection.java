package com.github.lessjava.types.inference.impl;

import com.github.lessjava.types.inference.HMType;

public class HMTypeCollection extends HMType {
    public HMType elementType;
    public String collectionType;

    public HMTypeCollection(HMType elementType) {
        this.elementType = elementType;
    }

    @Override
    public String toString() {
        return String.format("Collection: %s", this.elementType.toString());
    }

    @Override
    public HMTypeCollection clone() {
        switch (collectionType) {
            case "List":
                return new HMTypeList(this.elementType);
            //case "Set":
                //return new HMTypeSet(this.elementType);
            //case "Map":
                //return new HMTypeMap(this.elementType);
        }

        return null;
    }
}
