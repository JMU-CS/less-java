package com.github.lessjava.visitor.impl;

import java.util.Arrays;
import java.util.List;

import com.github.lessjava.types.Symbol;
import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTAttribute;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTBreak;
import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.types.ast.ASTConditional;
import com.github.lessjava.types.ast.ASTContinue;
import com.github.lessjava.types.ast.ASTForLoop;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTGlobalAssignment;
import com.github.lessjava.types.ast.ASTLiteral;
import com.github.lessjava.types.ast.ASTLocation;
import com.github.lessjava.types.ast.ASTMemberAccess;
import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.ast.ASTTest;
import com.github.lessjava.types.ast.ASTUnaryExpr;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.ast.ASTVoidFunctionCall;
import com.github.lessjava.types.ast.ASTWhileLoop;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeClass;

public class LJStaticAnalysis extends StaticAnalysis {

    private static final List<String> RESERVED_WORDS = Arrays.asList("this", "super", "class", "int", "boolean", "double", "char", "long", "float");
    private ASTClass enclosingClass;

    @Override
    public void preVisit(ASTProgram node) {
        StaticAnalysis.collectErrors = true;
    }

    @Override
    public void postVisit(ASTProgram node) {
    }

    @Override
    public void preVisit(ASTClass node) {
        StaticAnalysis.collectErrors = true;
        enclosingClass = node;
    }

    @Override
    public void postVisit(ASTClass node) {
        enclosingClass = null;
    }

    @Override
    public void preVisit(ASTGlobalAssignment node) {
        StaticAnalysis.collectErrors = true;
    }

    @Override
    public void preVisit(ASTFunction node) {
        StaticAnalysis.collectErrors = node.concrete;
    }

    @Override
    public void postVisit(ASTFunction node) {
    }

    @Override
    public void preVisit(ASTVariable node) {
        super.preVisit(node);
    }

    /**
     * Add an error if the variable doesn't have type information in the symbol table, i.e. it was never assigned to
     *
     * @param node the variable
     */
    @Override
    public void postVisit(ASTVariable node) {
        super.postVisit(node);

        // Don't need to check if the var is within a member access
        if(node.getParent() instanceof ASTMemberAccess) {
            return;
        }

        List<Symbol> symbols = BuildSymbolTables.searchScopesForSymbol(node, node.name, Symbol.SymbolType.VARIABLE);

        if(symbols == null || symbols.isEmpty()) {
            addError(node, "Cannot find variable " + node.name);
        }
    }

    @Override
    public void preVisit(ASTBlock node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTBlock node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTAssignment node) {
        super.preVisit(node);
    }

    /**
     * Make sure we don't assign to some reserved words
     *
     * @param node an assignment node
     */
    @Override
    public void postVisit(ASTAssignment node) {
        super.postVisit(node);
        if(RESERVED_WORDS.contains(node.variable.name)) {
            addError(node, "Cannot assign to " + node.variable.name);
        }
    }

    @Override
    public void preVisit(ASTVoidFunctionCall node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTVoidFunctionCall node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTConditional node) {
        super.preVisit(node);
    }

    /**
     * Ensure that the conditional is a boolean expression
     *
     * @param node
     */
    @Override
    public void postVisit(ASTConditional node) {
        super.postVisit(node);
        if(!node.condition.type.equals(new HMTypeBase(HMType.BaseDataType.BOOL))) {
            addError(node, node.condition.type + " is not a boolean expression");
        }
    }

    @Override
    public void preVisit(ASTWhileLoop node) {
        super.preVisit(node);
    }

    /**
     * Ensure that the while loop guard is a boolean expression
     *
     * @param node
     */
    @Override
    public void postVisit(ASTWhileLoop node) {
        super.postVisit(node);
        if(!node.guard.type.equals(new HMTypeBase(HMType.BaseDataType.BOOL))) {
            addError(node, node.guard.type + " is not a boolean expression");
        }
    }

    @Override
    public void preVisit(ASTReturn node) {
    }

    @Override
    public void postVisit(ASTReturn node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTBreak node) {
        super.preVisit(node);
    }

    /**
     * Ensures that break statements only occur within while or for loops
     *
     * @param node
     */
    @Override
    public void postVisit(ASTBreak node) {
        super.postVisit(node);
        ASTNode parent = node.getParent();
        while(parent != null && !(parent instanceof ASTWhileLoop || parent instanceof ASTForLoop)) {
            parent = parent.getParent();
        }
        if(parent == null) {
            addError(node, "Break statement must be inside a loop");
        }
    }

    /**
     * Ensures that continue statements only occur in while or for loops
     *
     * @param node
     */
    @Override
    public void preVisit(ASTContinue node) {
        super.preVisit(node);
        ASTNode parent = node.getParent();
        while(parent != null && !(parent instanceof ASTWhileLoop || parent instanceof ASTForLoop)) {
            parent = parent.getParent();
        }
        if(parent == null) {
            addError(node, "Continue statement must be inside a loop");
        }
    }

    @Override
    public void postVisit(ASTContinue node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTTest node) {
        super.preVisit(node);
        StaticAnalysis.collectErrors = true;
    }

    @Override
    public void postVisit(ASTTest node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTBinaryExpr node) {
        super.preVisit(node);
    }

    @Override
    public void inVisit(ASTBinaryExpr node) {
        super.inVisit(node);
    }

    /**
     * Ensure that binary expressions are only between compatible types
     *
     * @param node a binary expression
     */
    @Override
    public void postVisit(ASTBinaryExpr node) {
        super.postVisit(node);
        HMTypeBase INT_TYPE = new HMTypeBase(HMType.BaseDataType.INT);
        HMTypeBase DOUBLE_TYPE = new HMTypeBase(HMType.BaseDataType.DOUBLE);
        HMTypeBase BOOL_TYPE = new HMTypeBase(HMType.BaseDataType.BOOL);
        switch (node.operator) {
            case ADDASGN:
            case SUBASGN:
                if(!INT_TYPE.equals(node.rightChild.type) && !DOUBLE_TYPE.equals(node.rightChild.type)) {
                    addError(node, "Cannot apply operator " + node.operator + " with non-numeric right expression type " + node.rightChild.type);
                }
                break;
            case OR:
            case AND:
                if(!BOOL_TYPE.equals(node.leftChild.type)) {
                    addError(node, "Cannot apply operator " + node.operator + " with non-boolean left expression type " + node.leftChild.type);
                }
                if(!BOOL_TYPE.equals(node.rightChild.type)) {
                    addError(node, "Cannot apply operator " + node.operator + " with non-boolean right expression type " + node.rightChild.type);
                }
                break;
            case EQ:
            case NE:
                break;
            case LT:
            case GT:
            case LE:
            case GE:
            case ADD:
            case SUB:
            case MUL:
            case DIV:
            case MOD:
                if(!INT_TYPE.equals(node.leftChild.type) && !DOUBLE_TYPE.equals(node.leftChild.type)) {
                    addError(node, "Cannot apply operator " + node.operator + " with non-numeric left expression type " + node.leftChild.type);
                }
                if(!INT_TYPE.equals(node.rightChild.type) && !DOUBLE_TYPE.equals(node.rightChild.type)) {
                    addError(node, "Cannot apply operator " + node.operator + " with non-numeric right expression type " + node.rightChild.type);
                }
                break;
            default:
        }
    }

    @Override
    public void preVisit(ASTUnaryExpr node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTUnaryExpr node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTFunctionCall node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTFunctionCall node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTLocation node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTLocation node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTLiteral node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTLiteral node) {
        super.postVisit(node);
    }

    /**
     * Ensures type safety for for loops. If we have a lower bound, then we're iterating over integers and need to type
     * check. Otherwise we're iterating through a list and type inference would've caught errors already.
     *
     * @param node
     */
    @Override
    public void postVisit(ASTForLoop node) {
        super.postVisit(node);
        if(node.lowerBound != null) {
            // Iterating over ints
            final HMType INT_TYPE = new HMTypeBase(HMType.BaseDataType.INT);
            boolean varIsInt = INT_TYPE.equals(node.var.type) && BuildSymbolTables.searchScopesForSymbol(node, node.var.name, Symbol.SymbolType.VARIABLE).stream().allMatch(s -> INT_TYPE.equals(s.type));
            boolean lowerIsInt = INT_TYPE.equals(node.lowerBound.type);
            boolean upperIsInt = INT_TYPE.equals(node.upperBound.type);
            if (!BuildSymbolTables.searchScopesForSymbol(node.getParent(), node.var.name, Symbol.SymbolType.VARIABLE).isEmpty()) {
                addError(node, "Variable named " + node.var.name + " already exists");
            }
            if (!(varIsInt && lowerIsInt && upperIsInt)) {
                addError(node, "For loops can only run through integers");
            }
        }
    }

    /**
     * Determine if the member access accesses a member that both exists and is visible
     *
     * @param node a member access
     */
    @Override
    public void postVisit(ASTMemberAccess node) {
        super.postVisit(node);
        if(node.instance.type instanceof HMTypeClass) {
            ASTClass instanceClass = ASTClass.nameClassMap.get(((HMTypeClass)node.instance.type).name);
            ASTClass classToLookIn = instanceClass;
            ASTAttribute member;
            do {
                member = classToLookIn.block.classAttributes.stream().filter(attr -> attr.assignment.variable.name.equals(node.var.name)).findFirst().orElse(null);
                classToLookIn = classToLookIn.parent;
            } while (member == null && classToLookIn != null);
            if (member == null) {
                addError(node, node.instance.name + " does not have attribute " + node.var.name);
                return;
            }
            if (!isExtendedBy(instanceClass, enclosingClass) && !member.scope.equals("public")) {
                addError(node, "Attribute " + node.var.name + " is not public");
            }
        } else {
            addError(node, "Cannot access members of " + node.instance.name + "; type of " + node.instance.name + " is " + node.instance.type);
        }
    }

    /**
     * Return true iff base is extended by descendant
     * @param base base class
     * @param descendant the class we want to be a descendant of base
     * @return true iff descendant is a descendant of base
     */
    private boolean isExtendedBy(ASTClass base, ASTClass descendant) {
        ASTClass currentClass = descendant;
        while(currentClass != null && !currentClass.signature.className.equals(base.signature.className)) {
            currentClass = ASTClass.nameClassMap.get(currentClass.signature.superName);
        }
        return currentClass != null;
    }

}
