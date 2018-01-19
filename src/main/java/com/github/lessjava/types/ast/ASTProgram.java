package com.github.lessjava.types.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Decaf program. Basically just a list of {@link ASTVariable} and
 * {@link ASTFunction} declarations.
 */
public class ASTProgram extends ASTNode {
    public List<ASTGlobalAssignment> globals;
    public List<ASTAbstractFunction> functions;
    public List<ASTClass> classes;
    public List<ASTTest> tests;

    public ASTProgram() {
        super();
        this.globals = new ArrayList<>();
        this.functions = new ArrayList<>();
        this.tests = new ArrayList<>();
        this.classes = new ArrayList<>();
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        // Avoid concurrent modification exception
        for (int i = 0; i < functions.size(); i++) {
            functions.get(i).traverse(visitor);
        }

        for (ASTGlobalAssignment g : globals) {
            g.traverse(visitor);
        }
        for (ASTClass c : classes) {
            c.traverse(visitor);
        }
        for (ASTTest t : tests) {
            t.traverse(visitor);
        }

        visitor.postVisit(this);
    }
}
