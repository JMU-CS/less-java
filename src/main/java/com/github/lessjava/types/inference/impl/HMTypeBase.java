package com.github.lessjava.types.inference.impl;

import com.github.lessjava.types.inference.HMType;

public class HMTypeBase extends HMType {
    private BaseDataType baseType;

    public HMTypeBase(BaseDataType baseType) {
        this.baseType = baseType;
        this.isConcrete = true;
    }

    public BaseDataType getBaseType() {
        return baseType;
    }

    public void setBaseType(BaseDataType baseType) {
        this.baseType = baseType;
    }

    @Override
    public String toString() {
        switch (this.baseType) {
            case INT:
                return "Integer";
            case DOUBLE:
                return "Double";
            case BOOL:
                return "Boolean";
            case VOID:
                return "void";
            case STR:
                return "String";
            default:
                return "???";
        }
    }
}
