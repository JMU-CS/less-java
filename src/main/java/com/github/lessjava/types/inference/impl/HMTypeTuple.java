package com.github.lessjava.types.inference.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.lessjava.types.inference.HMType;

public class HMTypeTuple extends HMType {
    public List<HMType> types;

    public HMTypeTuple(List<HMType> types) {
        this.types = types;
    }

    @Override
    public HMType clone() {
        return new HMTypeTuple(new ArrayList<>(this.types));
    }
}
