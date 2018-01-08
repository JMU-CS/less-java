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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("<");
        if (!types.isEmpty()) {
            sb.append(String.format("%s", types.get(0).toString()));

            for (int i = 1; i < types.size(); i++) {
                sb.append(String.format(" : %s", types.get(i).toString()));
            }
        }
        sb.append(" >");

        return sb.toString();
    }
}
