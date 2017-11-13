package com.github.lessjava.types.inference.impl;

import com.github.lessjava.types.inference.HMType;

public class HMTypeBase extends HMType
{
    private BaseDataType baseType;

    public HMTypeBase(BaseDataType baseType)
    {
        this.baseType = baseType;
    }

    public BaseDataType getBaseType()
    {
        return baseType;
    }

    @Override
    public String toString()
    {
        switch (this.baseType) {
            case INT:
                return "int";
            case BOOL:
                return "boolean";
            case VOID:
                return "void";
            case STR:
                return "String";
            default:
                return "???";
        }

    }

}
