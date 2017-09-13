package com.github.lessjava.types.inference;

public abstract class HMType
{
    public static enum BaseDataType
    {
        INT, BOOL, VOID, STR, UNKNOWN
    }

    @Override
    public boolean equals(Object other)
    {
        return other instanceof HMType && (this.toString()).equals(((HMType) other).toString());
    }
}
