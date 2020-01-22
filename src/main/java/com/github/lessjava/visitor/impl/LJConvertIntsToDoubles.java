package com.github.lessjava.visitor.impl;

import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTConvertIntToDouble;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.visitor.LJDefaultASTVisitor;

/**
 * Analyzes the program to convert Integers to Doubles where necessary, like the 4 in 4 == 4.0
 */
public class LJConvertIntsToDoubles extends LJDefaultASTVisitor {

    // Records whether the current function returns a double
    private boolean returnsDouble = false;

    /**
     * If we're testing an Integer and a Double for equality, convert the int to a double. Strangely enough Java can handle > and < on its own.
     * @param node
     */
    @Override
    public void postVisit(ASTBinaryExpr node) {
        if(node.operator == ASTBinaryExpr.BinOp.EQ || node.operator == ASTBinaryExpr.BinOp.NE) {
            if(node.leftChild.type.equals(HMTypeBase.INT) && node.rightChild.type.equals(HMTypeBase.REAL)) {
                node.leftChild = new ASTConvertIntToDouble(node.leftChild);
            } else if(node.leftChild.type.equals(HMTypeBase.REAL) && node.rightChild.type.equals(HMTypeBase.INT)) {
                node.rightChild = new ASTConvertIntToDouble(node.rightChild);
            }
        }
    }

    /**
     * If we're assigning an int value to a double variable, convert the int to a double.
     * @param node
     */
    @Override
    public void postVisit(ASTAssignment node) {
        boolean savesToDouble = (node.memberAccess != null && node.memberAccess.type.equals(HMTypeBase.REAL))
                || (node.variable != null && node.variable.type.equals(HMTypeBase.REAL));
        if(savesToDouble && node.value.type.equals(HMTypeBase.INT)) {
            node.value = new ASTConvertIntToDouble(node.value);
        }
    }

    /**
     * Check if the function returns a double.
     * @param node
     */
    @Override
    public void preVisit(ASTFunction node) {
        returnsDouble = node.returnType.equals(HMTypeBase.REAL);
    }

    /**
     * If we're returning an int inside a function that returns a double, convert the int to a double.
     * @param node
     */
    @Override
    public void postVisit(ASTReturn node) {
        if(returnsDouble && node.value != null && node.value.type.equals(HMTypeBase.INT)) {
            node.value = new ASTConvertIntToDouble(node.value);
        }
    }
}
