
public class LJASTAssignTypes extends LJBaseASTVisitor {

    private ASTNode.DataType functionReturnType;
    private ASTFunction currentFunction;

    public LJASTAssignTypes() {
    }

    @Override
    public void preVisit(ASTFunction node) {
        currentFunction = node;
    }

    @Override
    public void postVisit(ASTReturn node) {
        currentFunction.returnType = evalExprType(node.value);
    }

    public ASTNode.DataType evalExprType(ASTExpression expr) {
        ASTNode.DataType type;

        if (expr instanceof ASTBinaryExpr) {
           type = evalExprType(expr);
        } else if(expr instanceof ASTLocation) {
           type = evalExprType(expr);
        } else if(expr instanceof ASTLiteral) {
           type = evalExprType(expr);
        } else {
           type = null;
        }

        return type;
    }

    public ASTNode.DataType evalExprType(ASTBinaryExpr expr) {
        return null;
    }

    public ASTNode.DataType evalExprType(ASTLocation expr) {
        //look up value in symboltable
        return null;
    }

    public ASTNode.DataType evalExprType(ASTLiteral expr) {
        return expr.type;
    }
}

