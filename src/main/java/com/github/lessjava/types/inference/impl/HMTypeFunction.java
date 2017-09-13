package com.github.lessjava.types.inference.impl;

import com.github.lessjava.types.inference.HMType;

// TODO

public class HMTypeFunction extends HMType
{
    private HMType[] left, right;

    public HMTypeFunction(HMType[] left, HMType[] right)
    {
        this.left = left;
        this.right = right;
    }

    public HMType[] getLeft()
    {
        return left;
    }

    public HMType[] getRight()
    {
        return right;
    }
}
