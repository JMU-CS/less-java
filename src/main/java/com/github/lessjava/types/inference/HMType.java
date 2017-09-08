package com.github.lessjava.types.inference;

import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeVar;

public interface HMType
{
    public static enum BaseDataType
    {
        INT, BOOL, VOID, STR, UNKNOWN
    }

    public static String typeToString(HMType t)
    {
        if (t == null) {
            return null;
        }

        if (t instanceof HMTypeBase) {
            BaseDataType baseType = ((HMTypeBase) t).getBaseType();

            switch (baseType) {
                case INT:
                    return "int";
                case BOOL:
                    return "bool";
                case VOID:
                    return "void";
                case STR:
                    return "str";
                case UNKNOWN:
                    return "unknown";
                default:
                    return "???";
            }
        } else {
            Integer id = ((HMTypeVar) t).getUniqueId();

            return String.format("T%s", id);
        }
    }
}
