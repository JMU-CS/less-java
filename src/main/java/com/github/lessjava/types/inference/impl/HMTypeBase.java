package com.github.lessjava.types.inference.impl;

import com.github.lessjava.types.inference.HMType;

public class HMTypeBase implements HMType
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
}
