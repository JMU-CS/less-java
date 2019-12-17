package com.github.lessjava.visitor.impl;

import java.util.HashSet;
import java.util.Set;

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

    /**
     * Add constructors to a class. After running, every class will:
     * - Have an empty constructor
     * - Have constructors that make calls to any superconstructors that exist in a parent class, unless they've been
     *   overridden in the subclass
     *
     * @param node The class to generate constructors for
     */
    @Override
    public void postVisit(ASTClass node) {
        super.postVisit(node);

        if (ASTClass.libraryClasses.contains(node)) {
            return;
        }

        if (!node.hasEmptyConstructor()) {
            ASTMethod emptyConstructor = generateEmptyConstructor(node);
            node.block.methods.add(emptyConstructor);
            node.block.constructors.add(emptyConstructor);
        }

        for(ASTMethod constructor : generateParentConstructors(node)) {
            node.block.methods.add(constructor);
            node.block.constructors.add(constructor);
        }
    }

    /**
     * Generate constructors for every constructor in a class's superclass, if it has one. If the current class already
     * has a constructor with the same number of parameters in a constructor in the superclass, do not generate a new
     * constructor.
     *
     * @param node The class to generate constructors for
     * @return A set of constructors that make calls to a superconstructor
     */
    private Set<ASTMethod> generateParentConstructors(ASTClass node) {
        Set<ASTMethod> constructors = new HashSet<>();
        if(node.parent != null) {
            for(ASTMethod parentConstructor : node.parent.block.constructors) {
                // Only bring the superconstructor into the subclass if the subclass doesn't have a constructor with a matching # of parameters
                if(node.block.constructors.stream().noneMatch(c -> c.parameters.size() == parentConstructor.parameters.size())) {
                    String name = node.signature.className;
                    HMTypeClass type = new HMTypeClass(name);
                    ASTFunction f = new ASTFunction(name, type, new ASTBlock());
                    ASTMethod constructor = new ASTMethod(ASTClass.PUBLIC, f, name);
                    ASTFunctionCall superConstructor = new ASTFunctionCall("super");

                    // Copy all parameters from the superconstructor to the constructor and the call to super
                    for(Parameter p : parentConstructor.parameters) {
                        f.parameters.add(p);
                        ASTVariable v = new ASTVariable(p.name);
                        superConstructor.arguments.add(v);
                        v.setParent(f);
                    }
                    f.body.statements.add(new ASTVoidFunctionCall(superConstructor));
                    f.setParent(constructor);
                    constructor.setParent(node.block);
                    constructor.isConstructor = true;
                    constructor.lineNumber = parentConstructor.lineNumber;
                    constructors.add(constructor);
                }
            }
        }
        return constructors;
    }

    /**
     * Generate a constructor taking no parameters, calling the empty superconstructor if the class is a subclass
     *
     * @param node The class to make a constructor for
     * @return A constructor taking no parameters
     */
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
