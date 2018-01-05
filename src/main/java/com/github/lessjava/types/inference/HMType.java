package com.github.lessjava.types.inference;

public abstract class HMType {
    public boolean isConcrete;

    public static enum BaseDataType {
        INT, REAL, BOOL, VOID, STR
    }

    @Override
    public boolean equals(Object other) {
        return other != null && other instanceof HMType && (this.toString()).equals(((HMType) other).toString());
    }

    public abstract HMType clone();
}
