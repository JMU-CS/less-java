package com.github.lessjava.visitor.impl;

import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTConditional;
import com.github.lessjava.types.ast.ASTContinue;
import com.github.lessjava.types.ast.ASTForLoop;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTLiteral;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.ast.ASTVoidAssignment;
import com.github.lessjava.types.ast.ASTVoidFunctionCall;
import com.github.lessjava.types.inference.HMType;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LJDuplicateBlockTest {

    LJDuplicateBlock underTest;

    @BeforeEach
    public void init() {
        underTest = new LJDuplicateBlock();
    }

    @Test
    public void testTypesIndependent() {
        ASTBlock original = new ASTBlock();

        // var = 0
        original.statements.add(new ASTVoidAssignment(new ASTAssignment(ASTBinaryExpr.BinOp.ASGN, new ASTVariable("var"), new ASTLiteral(HMType.BaseDataType.INT, 0))));

        ASTBlock copy = underTest.duplicateBlock(original);

        assertEquals(1, copy.statements.size());
        assertEquals("var", ((ASTVoidAssignment)copy.statements.get(0)).assignment.variable.name);
        HMType originalType = ((ASTVoidAssignment)original.statements.get(0)).assignment.type;
        HMType copyType = ((ASTVoidAssignment)copy.statements.get(0)).assignment.type;
        assertNotSame(originalType, copyType);
    }

    @Test
    public void testComplexBlock() {
        // Tests that the following block can be duplicated
        /*
        var = 5
        for(i: var-2 -> var+2) {
          if(i == 5) {
            continue
          } else {
            println("Not 5")
          }
          println(i)
        }
         */

        ASTBlock original = new ASTBlock();

        // var = 5
        original.statements.add(new ASTVoidAssignment(new ASTAssignment(ASTBinaryExpr.BinOp.ASGN, new ASTVariable("var"), new ASTLiteral(HMType.BaseDataType.INT, 5))));

        // var - 2
        ASTBinaryExpr lower = new ASTBinaryExpr(ASTBinaryExpr.BinOp.SUB, new ASTVariable("var"), new ASTLiteral(HMType.BaseDataType.INT, 2));

        // var+2
        ASTBinaryExpr upper = new ASTBinaryExpr(ASTBinaryExpr.BinOp.ADD, new ASTVariable("var"), new ASTLiteral(HMType.BaseDataType.INT, 2));

        // for(i: var-2 -> var+2) {...
        ASTForLoop forLoop = new ASTForLoop(new ASTVariable("i"), lower, upper, new ASTBlock());
        original.statements.add(forLoop);

        // if(i == 5) {...
        ASTConditional cond = new ASTConditional(new ASTBinaryExpr(ASTBinaryExpr.BinOp.EQ, new ASTVariable("i"), new ASTLiteral(HMType.BaseDataType.INT, 5)), new ASTBlock(), new ASTBlock());
        forLoop.block.statements.add(cond);

        // continue
        cond.ifBlock.statements.add(new ASTContinue());

        // ...} else {...
        // println("Not 5")
        ASTVoidFunctionCall funcCall = new ASTVoidFunctionCall(new ASTFunctionCall("println"));
        cond.elseBlock.statements.add(funcCall);
        funcCall.functionCall.arguments.add(new ASTLiteral(HMType.BaseDataType.STR, "Not 5"));

        // }
        // println(i)
        ASTVoidFunctionCall printI = new ASTVoidFunctionCall(new ASTFunctionCall("println"));
        forLoop.block.statements.add(printI);
        printI.functionCall.arguments.add(new ASTVariable("i"));

        ASTBlock copy = underTest.duplicateBlock(original);

        assertEquals(original.statements.size(), copy.statements.size());
        ASTForLoop copyFor = (ASTForLoop)copy.statements.get(1);
        ASTConditional copyConditional = (ASTConditional)copyFor.block.statements.get(0);
        ASTVoidFunctionCall copyPrintI = (ASTVoidFunctionCall)copyFor.block.statements.get(1);
        assertEquals(forLoop.block.statements.size(), copyFor.block.statements.size());
        assertEquals(cond.ifBlock.statements.size(), copyConditional.ifBlock.statements.size());
        assertEquals(cond.elseBlock.statements.size(), copyConditional.elseBlock.statements.size());
        assertEquals(printI.functionCall.arguments.size(), copyPrintI.functionCall.arguments.size());
    }

}