package com.github.lessjava.types.inference.iml;

import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.types.inference.HMType;

public class HMTypeBase implements HMType
{
    private ASTNode.DataType baseType;

    public HMTypeBase(ASTNode.DataType baseType)
    {
        this.baseType = baseType;
    }

    @Override
    public HMType infer()
    {
        return this;
    }

    public ASTNode.DataType getBaseType()
    {
        return baseType;
    }
}
