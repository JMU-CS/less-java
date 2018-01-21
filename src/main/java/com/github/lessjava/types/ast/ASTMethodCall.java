package com.github.lessjava.types.ast;

import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeCollection;
import com.github.lessjava.types.inference.impl.HMTypeObject;

public class ASTMethodCall extends ASTBinaryExpr {
    public ASTExpression invoker;
    public ASTFunctionCall funcCall;

    public ASTMethodCall(ASTExpression invoker, ASTFunctionCall funcCall) {
        super(BinOp.INVOKE, invoker, funcCall);
        this.invoker = invoker;
        this.funcCall = funcCall;
    }

    public String getIdentifyingString() {
        StringBuilder sb = new StringBuilder();

        sb.append(getClassName());
        sb.append(funcCall.getIdentifyingString());

        return sb.toString();
    }

    private String getClassName() {
        if (invoker.type instanceof HMTypeCollection) {
            HMTypeCollection c = (HMTypeCollection) invoker.type;

            return c.collectionName;
        }

        if (invoker.type instanceof HMTypeObject) {
            HMTypeObject c = (HMTypeObject) invoker.type;

            return c.className;
        }

        return "UNKNOWN_CLASS_NAME";
    }

    @Override
    public String toString() {
        String translation;
        StringBuilder argString;

        String collectionName = null;
        if (invoker.type instanceof HMTypeCollection) {
            collectionName = ((HMTypeCollection) invoker.type).collectionName;

            translation = ASTClass.methodTranslations.get(String.format("%s%s", collectionName, funcCall.name));
            if (translation != null) {
                argString = new StringBuilder();
                argString.append("(");
                for (ASTExpression e : funcCall.arguments) {
                    String exprString = e.toString();
                    if (argString.length() > 1) {
                        argString.append(", ");
                    }
                    argString.append(exprString);
                }
                argString.append(")");
                return String.format("%s.%s%s", invoker, translation, argString);
            }
        }

        if (type instanceof HMTypeBase) {
            return wrapPrimitive((HMTypeBase)type, String.format("%s.%s", invoker, funcCall));
        } else {
            return String.format("%s.%s", invoker, funcCall);
        }
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        invoker.traverse(visitor);
        funcCall.traverse(visitor);
        visitor.postVisit(this);
    }
}
