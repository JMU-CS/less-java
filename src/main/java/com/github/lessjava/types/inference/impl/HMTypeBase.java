package com.github.lessjava.types.inference.impl;

import com.github.lessjava.types.inference.HMType;

public class HMTypeBase extends HMType {
    public static final HMTypeBase INT = new HMTypeBase(BaseDataType.INT);
    public static final HMTypeBase REAL = new HMTypeBase(BaseDataType.REAL);
    public static final HMTypeBase BOOL = new HMTypeBase(BaseDataType.BOOL);
    public static final HMTypeBase STR = new HMTypeBase(BaseDataType.STR);
    public static final HMTypeBase VOID = new HMTypeBase(BaseDataType.VOID);

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
            case REAL:
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

    @Override
    public HMTypeBase clone() {
        return new HMTypeBase(this.baseType);
    }
}
