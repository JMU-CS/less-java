package com.github.lessjava.types.inference.impl;

import com.github.lessjava.types.inference.HMType;

public class HMTypeVar implements HMType
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

    public void setUniqueId(Integer uniqueId)
    {
        this.uniqueVarId = uniqueId;
    }
}
