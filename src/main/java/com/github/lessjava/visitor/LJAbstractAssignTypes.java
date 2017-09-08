package com.github.lessjava.visitor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.github.lessjava.types.Symbol;
import com.github.lessjava.types.SymbolTable;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTExpression;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTUnaryExpr;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.impl.HMTypeVar;
import com.github.lessjava.visitor.impl.StaticAnalysis;

public abstract class LJAbstractAssignTypes extends StaticAnalysis implements LJAssignTypes
{
    protected Map<String, ASTFunction> nameFunctionMap = new HashMap<>();
    protected Deque<SymbolTable>       scopes          = new ArrayDeque<>();

    @Override
    public void preVisit(ASTProgram node)
    {
        for (ASTFunction function : node.functions) {
            nameFunctionMap.put(function.name, function);
        }

        scopes.push((SymbolTable) node.attributes.get("symbolTable"));
    }

    @Override
    public void postVisit(ASTProgram node)
    {
        scopes.pop();
    }

    @Override
    public void preVisit(ASTFunction node)
    {
        scopes.push((SymbolTable) node.attributes.get("symbolTable"));
    }

    @Override
    public void postVisit(ASTFunction node)
    {
        scopes.pop();
    }

    @Override
    public void preVisit(ASTBlock node)
    {
        scopes.push((SymbolTable) node.attributes.get("symbolTable"));
    }

    @Override
    public void postVisit(ASTBlock node)
    {
        scopes.pop();
    }

    @Override
    public void preVisit(ASTFunctionCall node)
    {
        node.type = nameFunctionMap.get(node.name).returnType;
    }

    @Override
    public void postVisit(ASTFunctionCall node)
    {
    }

    public HMType evalExprType(ASTExpression expr)
    {
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

    public HMType evalExprType(ASTBinaryExpr expr)
    {
        return evalExprType(expr.leftChild);
    }

    public HMType evalExprType(ASTUnaryExpr expr)
    {
        return evalExprType(expr.child);
    }

    public HMType evalExprType(ASTFunctionCall expr)
    {
        return nameFunctionMap.get(expr.name).returnType;
    }

    public HMType evalExprType(ASTVariable expr)
    {
        try {
            Iterator<SymbolTable> scopeIterator = scopes.iterator();

            // Iterate over the current active scopes for the symbol
            while (scopeIterator.hasNext()) {
                SymbolTable scope = scopeIterator.next();

                Symbol symbol = scope.lookup(expr.name);
                if (symbol != null && symbol.type != null) {
                    return symbol.type;
                }
            }

        } catch (Exception e) {
        }

        return new HMTypeVar();
    }

    public boolean typeIsKnown(HMType type)
    {
        return type != null && !(type instanceof HMTypeVar);
    }

}
