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

    @Override
    public boolean equals(Object other)
    {
        if (other instanceof HMType) {
            return HMType.typeToString(this) == HMType.typeToString((HMType)other);
        } else {
            return false;
        }
    }
}
