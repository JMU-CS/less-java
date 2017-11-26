package com.github.lessjava.visitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.lessjava.types.Symbol;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTExpression;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTUnaryExpr;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.impl.HMTypeVar;
import com.github.lessjava.visitor.impl.BuildSymbolTables;
import com.github.lessjava.visitor.impl.StaticAnalysis;

public abstract class LJAbstractAssignTypes extends StaticAnalysis implements LJAssignTypes {
    protected static Map<String, ASTFunction> nameparamFunctionMap = new HashMap<>();

    @Override
    public void preVisit(ASTProgram node) {
	for (ASTFunction function : node.functions) {
	    nameparamFunctionMap.put(function.getNameParamString(), function);
	}
    }

    public HMType evalExprType(ASTExpression expr) {
	HMType type;

	if (expr instanceof ASTBinaryExpr) {
	    type = evalExprType((ASTBinaryExpr) expr);
	} else if (expr instanceof ASTUnaryExpr) {
	    type = evalExprType((ASTUnaryExpr) expr);
	} else if (expr instanceof ASTFunctionCall) {
	    type = evalExprType((ASTFunctionCall) expr);
	} else if (expr instanceof ASTVariable) {
	    type = evalExprType((ASTVariable) expr);
	} else {
	    type = expr.type;
	}

	return type;
    }

    public HMType evalExprType(ASTBinaryExpr expr) {
	return evalExprType(expr.leftChild);
    }

    public HMType evalExprType(ASTUnaryExpr expr) {
	return evalExprType(expr.child);
    }

    public HMType evalExprType(ASTFunctionCall expr) {
	return nameparamFunctionMap.get(expr.getNameArgString()).returnType;
    }

    public HMType evalExprType(ASTVariable expr) {

	List<Symbol> symbols = BuildSymbolTables.searchScopesForSymbol(expr, expr.name);

	for (Symbol s : symbols) {
	    if (s != null && s.type != null) {
		return s.type;
	    }
	}

	return new HMTypeVar();
    }

    public boolean typeIsKnown(HMType type) {
	return type != null && !(type instanceof HMTypeVar);
    }

}
