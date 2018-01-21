package com.github.lessjava.visitor.impl;

import java.util.List;

import com.github.lessjava.types.Symbol;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.visitor.LJAbstractAssignTypes;

public class LJUnifyVariables extends LJAbstractAssignTypes {
    @Override
    public void preVisit(ASTVariable node) {
        List<Symbol> symbols = BuildSymbolTables.searchScopesForSymbol(node, node.name);

        if (symbols != null && !symbols.isEmpty()) {
            symbols.forEach(s -> node.type = unify(node.type, s.type));
        }
    }

    @Override
    public void postVisit(ASTVariable node) {
    }
}
