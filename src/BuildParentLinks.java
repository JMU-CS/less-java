package edu.jmu.decaf;

/**
 * AST pre-order visitor; initializes parent links for use in later AST
 * analyses.
 *
 */
class BuildParentLinks extends DefaultASTVisitor
{
    @Override
    public void preVisit(ASTProgram node)
    {
        for (ASTVariable var : node.variables) {
            var.setParent(node);
        }
        for (ASTFunction func : node.functions) {
            func.setParent(node);
        }
    }

    @Override
    public void preVisit(ASTFunction node)
    {
        node.body.setParent(node);
    }

    @Override
    public void preVisit(ASTBlock node)
    {
        for (ASTVariable var : node.variables) {
            var.setParent(node);
        }
        for (ASTStatement stmt : node.statements) {
            stmt.setParent(node);
        }
    }

    @Override
    public void preVisit(ASTAssignment node)
    {
        node.location.setParent(node);
        node.value.setParent(node);
    }

    @Override
    public void preVisit(ASTVoidFunctionCall node)
    {
        for (ASTExpression expr : node.arguments) {
            expr.setParent(node);
        }
    }

    @Override
    public void preVisit(ASTConditional node)
    {
        node.condition.setParent(node);
        node.ifBlock.setParent(node);
        if (node.hasElseBlock()) {
            node.elseBlock.setParent(node);
        }
    }

    @Override
    public void preVisit(ASTWhileLoop node)
    {
        node.guard.setParent(node);
        node.body.setParent(node);
    }

    @Override
    public void preVisit(ASTReturn node)
    {
        if (node.hasValue()) {
            node.value.setParent(node);
        }
    }

    // no need for ASTBreak or ASTContinue handlers (no children)

    @Override
    public void preVisit(ASTBinaryExpr node)
    {
        node.leftChild.setParent(node);
        node.rightChild.setParent(node);
    }

    @Override
    public void preVisit(ASTUnaryExpr node)
    {
        node.child.setParent(node);
    }

    @Override
    public void preVisit(ASTFunctionCall node)
    {
        for (ASTExpression expr : node.arguments) {
            expr.setParent(node);
        }
    }

    @Override
    public void preVisit(ASTLocation node)
    {
        if (node.hasIndex()) {
            node.index.setParent(node);
        }
    }

    // no need for ASTLiteral handler (no children)
}

