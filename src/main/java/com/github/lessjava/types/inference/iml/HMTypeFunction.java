package com.github.lessjava.types.inference.iml;

import com.github.lessjava.types.inference.HMType;

public class HMTypeFunction implements HMType
{
    private HMType left, right;

    public HMTypeFunction(HMType left, HMType right)
    {
        this.left = left;
        this.right = right;
    }

    @Override
    public HMType infer()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public HMType getLeft()
    {
        return left;
    }

    public HMType getRight()
    {
        return right;
    }
}
