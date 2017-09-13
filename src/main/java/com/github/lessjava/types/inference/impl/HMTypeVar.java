package com.github.lessjava.types.inference.impl;

import com.github.lessjava.types.inference.HMType;

public class HMTypeVar extends HMType
{
    private static Integer globalUniqueId = 0;

    private Integer uniqueVarId;
    
    public HMTypeVar() {
        this.uniqueVarId = globalUniqueId++;
    }

    public Integer getUniqueId()
    {
        return uniqueVarId;
    }

    @Override
    public String toString() {
        return String.format("T%s", this.uniqueVarId);
        
    }
}
