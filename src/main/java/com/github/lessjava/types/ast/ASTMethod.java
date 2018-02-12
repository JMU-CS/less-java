package com.github.lessjava.types.ast;

public class ASTMethod extends ASTAbstractFunction {

    public String scope;
    public ASTFunction function;
    public String containingClassName;

    public ASTMethod(String scope, ASTFunction function, String className) {
        super(function);
        this.scope = scope;
        this.function = function;
    }

    public String getIdentifyingString() {
        StringBuilder sb = new StringBuilder();

        sb.append(containingClassName);
        sb.append(name);
        sb.append(getParameterStr());

        return sb.toString();
    }

    public ASTMethod clone() {
        return new ASTMethod(scope, function, containingClassName);
    }

    @Override
    public String toString() {
        return String.format("Method: %s.%s, %s", ((ASTClassSignature) this.getParent().getParent()).className, this.name, this.returnType);
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        // Library methods have null bodies
        if (body != null) {
            body.traverse(visitor);
        }
        visitor.postVisit(this);
    }
}
