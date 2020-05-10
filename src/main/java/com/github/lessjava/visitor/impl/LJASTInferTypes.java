package com.github.lessjava.visitor.impl;

import java.util.Arrays;
import java.util.List;

import com.github.lessjava.types.Symbol;
import com.github.lessjava.types.ast.ASTAbstractFunction;
import com.github.lessjava.types.ast.ASTAbstractFunction.Parameter;
import com.github.lessjava.types.ast.ASTArgList;
import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTAttribute;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.types.ast.ASTClassBlock;
import com.github.lessjava.types.ast.ASTConditional;
import com.github.lessjava.types.ast.ASTEntry;
import com.github.lessjava.types.ast.ASTExpression;
import com.github.lessjava.types.ast.ASTForLoop;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTList;
import com.github.lessjava.types.ast.ASTMap;
import com.github.lessjava.types.ast.ASTMemberAccess;
import com.github.lessjava.types.ast.ASTMethod;
import com.github.lessjava.types.ast.ASTMethodCall;
import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.ast.ASTSet;
import com.github.lessjava.types.ast.ASTUnaryExpr;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.HMType.BaseDataType;
import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeClass;
import com.github.lessjava.types.inference.impl.HMTypeCollection;
import com.github.lessjava.types.inference.impl.HMTypeList;
import com.github.lessjava.types.inference.impl.HMTypeMap;
import com.github.lessjava.types.inference.impl.HMTypeSet;
import com.github.lessjava.types.inference.impl.HMTypeTuple;
import com.github.lessjava.types.inference.impl.HMTypeVar;
import com.github.lessjava.visitor.LJAbstractAssignTypes;

public class LJASTInferTypes extends LJAbstractAssignTypes {
    private HMType returnType;

    private static ASTProgram program;

    private List<Parameter> parameters;

    @Override
    public void preVisit(ASTProgram node) {
        super.preVisit(node);

        if (LJASTInferTypes.program == null) {
            LJASTInferTypes.program = node;
        }
    }

    @Override
    public void postVisit(ASTMethod node) {
        super.preVisit(node);

        if (node.isConstructor) {
            node.returnType = node.function.returnType = new HMTypeClass(node.name);
            node.concrete = node.function.concrete = true;

            for (Parameter p : node.function.parameters) {
                ASTAttribute member = ASTClassBlock.nameAttributeMap.get(p.name);
                if (member == null) {
                    StaticAnalysis.addError(node, "Constructor parameter names must match class member names. \"" + p.name + "\" is not a member.");
                } else {
                    p.type = unify(node, p.type, ASTClassBlock.nameAttributeMap.get(p.name).assignment.type);
                }
            }
        } else {
            node.returnType = node.function.returnType;
            node.concrete = node.function.concrete;
        }
    }

    @Override
    public void preVisit(ASTFunction node) {
        super.preVisit(node);

        this.returnType = null;
        this.parameters = node.parameters;
        StaticAnalysis.collectErrors = node.concrete;
    }

    @Override
    public void postVisit(ASTFunction node) {
        super.postVisit(node);

        this.parameters = null;
        StaticAnalysis.collectErrors = true;

        if (node.body == null) {
            return;
        }

        node.returnType = this.returnType == null ? HMTypeBase.VOID : unify(node, node.returnType, this.returnType);
        BuildSymbolTables.searchScopesForSymbol(node, node.name, Symbol.SymbolType.FUNCTION).forEach(s -> node.returnType = unify(node, node.returnType, s.type));
        node.concrete = node.parameters.stream().noneMatch(p -> p.type instanceof HMTypeVar);
        this.returnType = null;
    }

    @Override
    public void preVisit(ASTForLoop node) {
        if (node.lowerBound == null) {
            if(node.upperBound.type instanceof HMTypeCollection){
                node.var.type = ((HMTypeCollection) node.upperBound.type).elementType;
            }
        } else {
            node.var.type = new HMTypeBase(BaseDataType.INT);
        }
    }

    @Override
    public void postVisit(ASTReturn node) {
        super.postVisit(node);

        if (node.value == null) {
            this.returnType = new HMTypeBase(BaseDataType.VOID);
        } else if (node.value instanceof ASTVariable) {
            ASTVariable var = (ASTVariable) node.value;

            if (var.index != null && var.type instanceof HMTypeCollection) {
                this.returnType = unify(node, this.returnType, ((HMTypeCollection) var.type).elementType);
            } else {
                this.returnType = unify(node, this.returnType, node.value.type);
            }
        } else {
            this.returnType = unify(node, this.returnType, node.value.type);
        }

        ASTNode parent = node.getParent();
        while(parent != null && !(parent instanceof ASTMethod)) {
            parent = parent.getParent();
        }
        if(parent instanceof ASTMethod && ((ASTMethod) parent).isConstructor) {
            addError(node, "Cannot return from a constructor");
        }
    }

    @Override
    public void preVisit(ASTBinaryExpr node) {
        super.preVisit(node);

        switch (node.operator) {
            case INVOKE:
                break;
            case INDEX:
                node.type = ((HMTypeCollection) node.leftChild.type).elementType;
                break;
            case ASGN:
            case ADDASGN:
            case SUBASGN:
            case ADD:
            case SUB:
            case MUL:
            case DIV:
            case MOD:
                HMType unifiedType = unify(node, node.leftChild.type, node.rightChild.type);
                node.type = unifiedType != null ? unifiedType : node.type;
                break;
            case OR:
            case AND:
            case EQ:
            case NE:
            case LT:
            case GT:
            case LE:
            case GE:
                node.type = new HMTypeBase(BaseDataType.BOOL);
                break;
        }
    }

    @Override
    public void postVisit(ASTBinaryExpr node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTUnaryExpr node) {
        super.preVisit(node);

        node.type = unify(node, node.type, node.child.type);
    }

    @Override
    public void postVisit(ASTUnaryExpr node) {
        super.postVisit(node);
    }

    @Override
    public void postVisit(ASTFunctionCall node) {
        super.postVisit(node);

        if(node.getParent() instanceof ASTMethodCall && ((ASTMethodCall) node.getParent()).funcCall == node) {
            return;
        }

        if (ASTClass.nameClassMap.containsKey(node.name)
                && !ASTFunction.libraryFunctionStrings.containsKey(node.name)) {
            node.type = new HMTypeClass(node.name);
        } else if (ASTFunction.specialCases.containsKey(node.name)) {
            node.type = unify(node, node.type, ASTFunction.specialCases.get(node.name).returnType);
        } else if (idFunctionMap.containsKey(node.getIdentifyingString())) {
            List<ASTAbstractFunction> functions = idFunctionMap.get(node.getIdentifyingString());

            for (ASTAbstractFunction function: functions) {
                if (function.parameters.size() == node.arguments.size()) {
                    node.type = unify(node, node.type, function.returnType);
                }
            }
        }
        if (collectionCopyConstructorCall(node)) {
            ((HMTypeCollection)node.type).elementType = ((HMTypeCollection)node.arguments.get(0).type).elementType;
        }
    }

    @Override
    public void preVisit(ASTVariable node) {
        super.preVisit(node);

        if (this.parameters == null) {
            return;
        }

        List<Symbol> symbols = BuildSymbolTables.searchScopesForSymbol(node, node.name, Symbol.SymbolType.VARIABLE);

        if(node.index != null) {
            if(symbols.stream().allMatch(s -> s.type instanceof HMTypeCollection)) {
                symbols.forEach(s -> node.type = unify(node, ((HMTypeCollection) s.type).elementType, node.type));
            } else {
                addError(node, "Cannot index into " + node.name + " because it is not a list. " + node.type);
            }
        } else {
            symbols.forEach(s -> node.type = unify(node, s.type, node.type));
        }

        for (Parameter p : parameters) {
            if (p.name.equals(node.name)) {
                if(node.index == null) {
                    node.type = unify(node, node.type, p.type);
                } else if(p.type instanceof HMTypeCollection) {
                    node.type = unify(node, node.type, ((HMTypeCollection) p.type).elementType);
                } else {
                    addError(node, "Cannot index into " + node.name + " because it is not a list. Type is " + p.type);
                }
            }
        }
    }

    @Override
    public void postVisit(ASTVariable node) {
        super.postVisit(node);

        // If the variable is the member in a member access, we don't infer the type here
        if(node.getParent() instanceof ASTMemberAccess && ((ASTMemberAccess)node.getParent()).var == node) {
            return;
        }

        List<Symbol> symbols = BuildSymbolTables.searchScopesForSymbol(node, node.name, Symbol.SymbolType.VARIABLE);

        if (symbols != null && !symbols.isEmpty()) {
            if(node.index == null) {
                symbols.forEach(s -> node.type = s.variable == null ? node.type : unify(node, node.type, s.type));
            } else {
                symbols.forEach((s -> node.type = s.variable == null ? node.type : unify(node, node.type, ((HMTypeCollection) s.type).elementType)));
            }
        }

        node.isCollection = node.isCollection || node.type instanceof HMTypeCollection;
    }

    @Override
    public void postVisit(ASTMemberAccess node) {
        super.postVisit(node);

        if(node.instance.type instanceof HMTypeClass) {
            ASTClass containingClass = ASTClass.nameClassMap.get(((HMTypeClass) node.instance.type).name);

            ASTVariable attribute = containingClass.getAttribute(node.var.name);

            if (attribute != null) {
                node.var.type = unify(node, node.var.type, attribute.type);
                node.type = node.var.type;
            }
        } else {
            addError(node, "Cannot access members of " + node.instance.name + "; type of " + node.instance.name + " is " + node.instance.type);
        }
    }

    @Override
    public void preVisit(ASTList node) {
        super.preVisit(node);

        node.type = new HMTypeList(node.initialElements.type);
    }

    @Override
    public void preVisit(ASTSet node) {
        super.preVisit(node);

        node.type = new HMTypeSet(node.initialElements.type);
    }

    @Override
    public void preVisit(ASTMap node) {
        super.preVisit(node);

        if (node.initialElements.type instanceof HMTypeTuple) {
            node.type = new HMTypeMap((HMTypeTuple) node.initialElements.type);
        }
    }

    @Override
    public void preVisit(ASTEntry node) {
        List<HMType> types = Arrays.asList(new HMType[] { node.key.type, node.value.type });

        node.type = new HMTypeTuple(types);
    }

    @Override
    public void preVisit(ASTArgList node) {
        super.preVisit(node);

        node.isConcrete = node.isConcrete || node.type instanceof HMTypeBase;
    }

    @Override
    public void postVisit(ASTArgList node) {
        super.postVisit(node);

        if (!node.arguments.isEmpty()) {
            node.type.isConcrete = true;
            node.type = unify(node, node.type, node.arguments.get(0).type);
        }
    }

    @Override
    public void postVisit(ASTAssignment node) {
        super.postVisit(node);

        node.variable.type = unify(node, node.variable.type, node.value.type);

        if (node.value instanceof ASTVariable) {
            ASTVariable var = (ASTVariable) node.value;

            if (var.type instanceof HMTypeCollection && var.index != null) {
                node.variable.type = ((HMTypeCollection) var.type).elementType;
            }
        }

        node.type = node.variable.type;
    }

    @Override
    public void postVisit(ASTMethodCall node) {
        super.postVisit(node);

        // Special cases for builtin classes with generics
        if (node.invoker.type instanceof HMTypeCollection) {
            HMTypeCollection t = (HMTypeCollection) node.invoker.type;
            List<ASTExpression> arguments = node.funcCall.arguments;
            String methodName = node.funcCall.name;
            switch (t.collectionName) {
                case HMTypeMap.MAP:
                    List<HMType> typeTuple = ((HMTypeTuple)t.elementType).types;
                    HMType keyType = typeTuple.get(0);
                    HMType valueType = typeTuple.get(1);
                    switch (methodName) {
                        case "get":
                            requireArgs(node, 1, arguments);
                            node.funcCall.type = unify(node, node.funcCall.type, valueType);
                            break;
                        case "put":
                            if(requireArgs(node, 2, arguments)) {
                                typeTuple.set(0, unify(node, keyType, arguments.get(0).type));
                                typeTuple.set(1, unify(node, valueType, arguments.get(1).type));
                            }
                            node.funcCall.type = HMTypeBase.VOID;
                            break;
                        case "contains":
                            requireArgs(node, 1, arguments);
                            node.funcCall.type = HMTypeBase.BOOL;
                            break;
                        case "size":
                            requireArgs(node, 0, arguments);
                            node.funcCall.type = HMTypeBase.INT;
                            break;
                        case "keys":
                            requireArgs(node, 0, arguments);
                            node.funcCall.type = new HMTypeSet(keyType);
                            break;
                        default:
                            addError(node, "Method does not exist");
                    }
                    break;
                case HMTypeList.LIST:
                    switch(methodName) {
                        case "get":
                            requireArgs(node, 1, arguments);
                            node.funcCall.type = unify(node, node.funcCall.type, t.elementType);
                            break;
                        case "push":
                        case "add":
                        case "enqueue":
                            if(requireArgs(node, 1, arguments)) {
                                t.elementType = unify(node, t.elementType, arguments.get(0).type);
                            }
                            node.funcCall.type = HMTypeBase.VOID;
                            break;
                        case "size":
                            requireArgs(node, 0, arguments);
                            node.funcCall.type = HMTypeBase.INT;
                            break;
                        case "removeAt":
                            if(requireArgs(node, 1, arguments)) {
                                if(!arguments.get(0).type.equals(HMTypeBase.INT)) {
                                    addError(node, "Method 'removeAt' requires an integer index, found " + arguments.get(0).type);
                                }
                            }
                            node.funcCall.type = unify(node, node.funcCall.type, t.elementType);
                            break;
                        case "set":
                            if(requireArgs(node, 2, arguments)) {
                                if(!arguments.get(0).type.equals(HMTypeBase.INT)) {
                                    addError(node, "Method 'set' requires an integer index, found " + arguments.get(0).type);
                                }
                                t.elementType = unify(node, t.elementType, arguments.get(1).type);
                            }
                            node.funcCall.type = unify(node, node.funcCall.type, t.elementType);
                            break;
                        case "remove":
                            requireArgs(node, 1, arguments);
                            node.funcCall.type = HMTypeBase.BOOL;
                            break;
                        case "pop":
                        case "dequeue":
                            requireArgs(node, 0, arguments);
                            node.funcCall.type = unify(node, node.funcCall.type, t.elementType);
                        case "insert":
                            if(requireArgs(node, 2, arguments)) {
                                if(!arguments.get(0).type.equals(HMTypeBase.INT)) {
                                    addError(node, "Method 'insert' requires an integer index, found " + arguments.get(0).type);
                                }
                                t.elementType = unify(node, t.elementType, arguments.get(1).type);
                            }
                            node.funcCall.type = HMTypeBase.VOID;
                        default:
                            addError(node, "Method does not exist");
                    }
                    break;
                case HMTypeSet.SET:
                    switch(methodName) {
                        case "add":
                            if(requireArgs(node, 1, arguments)) {
                                t.elementType = unify(node, t.elementType, arguments.get(0).type);
                            }
                            node.funcCall.type = HMTypeBase.VOID;
                            break;
                        case "remove":
                        case "contains":
                            requireArgs(node, 1, arguments);
                            node.funcCall.type = HMTypeBase.BOOL;
                            break;
                        case "size":
                            requireArgs(node, 0, arguments);
                            node.funcCall.type = HMTypeBase.INT;
                            break;
                        default:
                    }
                    break;
            }
        }

        if (node.invoker.type instanceof HMTypeClass) {
            HMTypeClass type = (HMTypeClass) node.invoker.type;
            ASTClass containingClass = ASTClass.nameClassMap.get(type.name);

            if(containingClass.getMethod(node.funcCall.name) != null) {
                HMType returnType = containingClass.getMethod(node.funcCall.name).returnType;

                node.type = unify(node, node.type, returnType);
            }
        }

        node.type = unify(node, node.type, node.funcCall.type);
    }

    @Override
    public void preVisit(ASTConditional node) {
        super.preVisit(node);

        node.condition.type = unify(node, node.condition.type, new HMTypeBase(BaseDataType.BOOL));
    }

    /**
     * Ensure that a method call has the correct number of arguments. Used for builtin methods primarily.
     *
     * @param node the method call to check
     * @param requiredArgs the number of required arguments
     * @param args the supplied arguments
     * @return true iff the method call has the appropriate amount of arguments. If not, add an error and return false
     */
    private boolean requireArgs(ASTMethodCall node, int requiredArgs, List<ASTExpression> args) {
        if(args.size() != requiredArgs) {
            addError(node, "Method '" + node.funcCall.name + "' requires " + requiredArgs + " arguments");
            return false;
        }
        return true;
    }

    /**
     * Determines if a function call is a call to a copy constructor of a builtin collection
     *
     * @param node the function call
     * @return true iff the call is to a copy constructor
     */
    private boolean collectionCopyConstructorCall(ASTFunctionCall node) {
        if(node.arguments.size() == 0) {
            return false;
        }
        ASTExpression arg = node.arguments.get(0);
        if(node.name.equals("Map")) {
            return arg.type instanceof HMTypeMap;
        }
        if(node.name.equals("List") || node.name.equals("Set")) {
            return arg.type instanceof HMTypeList || arg.type instanceof HMTypeSet;
        }
        return false;
    }
}
