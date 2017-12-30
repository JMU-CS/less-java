package com.github.lessjava.types.inference;

public abstract class HMType {
    public boolean isConcrete;
    public boolean isCollection;

    public static enum BaseDataType {
        INT, REAL, BOOL, VOID, STR
    }

    @Override
    public boolean equals(Object other) {
        return other != null && other instanceof HMType && (this.toString()).equals(((HMType) other).toString());
    }
}
