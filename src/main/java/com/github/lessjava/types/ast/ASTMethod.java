package com.github.lessjava.types.ast;

public class ASTMethod extends ASTAbstractFunction {

    public String scope;
    public ASTFunction function;
    public String containingClassName;

    public boolean isConstructor;

    public ASTMethod(String scope, ASTFunction function, String containingClassName) {
        super(function);
        this.scope = scope;
        this.function = function;
        this.containingClassName = containingClassName;
    }

    public String getIdentifyingString() {
        StringBuilder sb = new StringBuilder();

        if (!isConstructor) {
            sb.append(containingClassName);
        }

        sb.append(function.getIdentifyingString());

        return sb.toString();
    }

    public ASTMethod clone() {
        return new ASTMethod(scope, function, containingClassName);
    }

    @Override
    public String toString() {
        return String.format("Method: %s.%s, %s", containingClassName, this.name, this.returnType);
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        function.traverse(visitor);
        visitor.postVisit(this);
    }
}
