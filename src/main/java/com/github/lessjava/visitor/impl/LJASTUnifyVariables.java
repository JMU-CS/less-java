package com.github.lessjava.visitor.impl;

import com.github.lessjava.types.Symbol;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.visitor.LJAbstractAssignTypes;

public class LJASTUnifyVariables extends LJAbstractAssignTypes
{

    /* (non-Javadoc)
     * @see com.github.lessjava.visitor.LJDefaultASTVisitor#postVisit(com.github.lessjava.types.ast.ASTVariable)
     */
    @Override
    public void postVisit(ASTVariable node)
    {
        super.postVisit(node);
        Symbol symbol = BuildSymbolTables.searchScopesForSymbol(node, node.name).get(0);
        node.type = symbol.type;
    }
    
}
