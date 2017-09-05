package com.github.lessjava.visitor.impl;

import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTLiteral;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.visitor.LJAbstractAssignTypes;

public class LJASTAssignPrimitiveTypes extends LJAbstractAssignTypes
{

    @Override
    public void postVisit(ASTAssignment node)
    {
        if (node.value instanceof ASTLiteral) {
            node.variable.type = evalExprType(node.value);
        }
    }

    @Override
    public void postVisit(ASTVariable node)
    {
        node.type = evalExprType(node);
    }

}
