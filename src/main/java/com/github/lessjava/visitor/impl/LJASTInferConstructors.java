package com.github.lessjava.visitor.impl;

import com.github.lessjava.types.ast.ASTAbstractFunction.Parameter;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTMethod;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.ast.ASTVoidFunctionCall;
import com.github.lessjava.types.inference.impl.HMTypeClass;
import com.github.lessjava.visitor.LJDefaultASTVisitor;

public class LJASTInferConstructors extends LJDefaultASTVisitor {
    @Override
    public void postVisit(ASTClass node) {
        super.postVisit(node);

        if (ASTClass.libraryClasses.contains(node)) {
            return;
        }

        if (!node.hasExplicitConstructor()) {
            ASTMethod constructor = generateConstructor(node);
            ASTMethod emptyConstructor = generateEmptyConstructor(node);

            node.block.methods.add(constructor);
            node.block.methods.add(emptyConstructor);
            node.block.constructor = constructor;
        } else {
            ASTMethod emptyConstructor = generateEmptyConstructor(node);
            node.block.methods.add(emptyConstructor);
        }
    }

    private ASTMethod generateConstructor(ASTClass node) {
        String name = node.signature.className;
        HMTypeClass type = new HMTypeClass(name);
        ASTFunction f = new ASTFunction(name, type, new ASTBlock());
        ASTMethod constructor = new ASTMethod(ASTClass.PUBLIC, f, name);

        if (node.parent != null) {
            ASTFunctionCall superConstructor = new ASTFunctionCall("super");

            for (Parameter p: node.parent.block.constructor.function.parameters) {
                f.parameters.add(p);
                ASTVariable v = new ASTVariable(p.name);
                superConstructor.arguments.add(v);

                v.setParent(f);
            }

            f.body = new ASTBlock();

            superConstructor.setParent(f);
            f.body.statements.add(new ASTVoidFunctionCall(superConstructor));
        }

        f.setParent(constructor);
        constructor.setParent(node.block);
        constructor.isConstructor = true;

        return constructor;
    }

    private ASTMethod generateEmptyConstructor(ASTClass node) {
        String name = node.signature.className;
        HMTypeClass type = new HMTypeClass(name);
        ASTFunction f = new ASTFunction(name, type, new ASTBlock());
        ASTMethod constructor = new ASTMethod(ASTClass.PUBLIC, f, name);

        if (node.parent != null) {
            ASTFunctionCall superConstructor = new ASTFunctionCall("super");
            superConstructor.setParent(f);

            f.body = new ASTBlock();
            f.body.statements.add(new ASTVoidFunctionCall(superConstructor));
        }

        f.setParent(constructor);
        constructor.setParent(node.block);
        constructor.isConstructor = true;

        return constructor;
    }
}
