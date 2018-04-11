package com.github.lessjava.visitor.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.github.lessjava.generated.LJBaseListener;
import com.github.lessjava.generated.LJParser.ArgListContext;
import com.github.lessjava.generated.LJParser.AssignmentContext;
import com.github.lessjava.generated.LJParser.AttributeContext;
import com.github.lessjava.generated.LJParser.BlockContext;
import com.github.lessjava.generated.LJParser.BreakContext;
import com.github.lessjava.generated.LJParser.ClassBlockContext;
import com.github.lessjava.generated.LJParser.ClassSignatureContext;
import com.github.lessjava.generated.LJParser.Class_Context;
import com.github.lessjava.generated.LJParser.ConditionalContext;
import com.github.lessjava.generated.LJParser.ContinueContext;
import com.github.lessjava.generated.LJParser.EntryContext;
import com.github.lessjava.generated.LJParser.ExprBaseContext;
import com.github.lessjava.generated.LJParser.ExprBinContext;
import com.github.lessjava.generated.LJParser.ExprContext;
import com.github.lessjava.generated.LJParser.ExprUnContext;
import com.github.lessjava.generated.LJParser.ForContext;
import com.github.lessjava.generated.LJParser.FuncCallContext;
import com.github.lessjava.generated.LJParser.FunctionContext;
import com.github.lessjava.generated.LJParser.GlobalContext;
import com.github.lessjava.generated.LJParser.ListContext;
import com.github.lessjava.generated.LJParser.LitContext;
import com.github.lessjava.generated.LJParser.MapContext;
import com.github.lessjava.generated.LJParser.MemberAccessContext;
import com.github.lessjava.generated.LJParser.MethodCallContext;
import com.github.lessjava.generated.LJParser.MethodContext;
import com.github.lessjava.generated.LJParser.ProgramContext;
import com.github.lessjava.generated.LJParser.ReturnContext;
import com.github.lessjava.generated.LJParser.SetContext;
import com.github.lessjava.generated.LJParser.TestContext;
import com.github.lessjava.generated.LJParser.VarContext;
import com.github.lessjava.generated.LJParser.VoidAssignmentContext;
import com.github.lessjava.generated.LJParser.VoidFunctionCallContext;
import com.github.lessjava.generated.LJParser.VoidMethodCallContext;
import com.github.lessjava.generated.LJParser.WhileContext;
import com.github.lessjava.types.ast.ASTAbstractFunction;
import com.github.lessjava.types.ast.ASTArgList;
import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTAttribute;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTBinaryExpr.BinOp;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTBreak;
import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.types.ast.ASTClassBlock;
import com.github.lessjava.types.ast.ASTClassSignature;
import com.github.lessjava.types.ast.ASTCollection;
import com.github.lessjava.types.ast.ASTConditional;
import com.github.lessjava.types.ast.ASTContinue;
import com.github.lessjava.types.ast.ASTEntry;
import com.github.lessjava.types.ast.ASTExpression;
import com.github.lessjava.types.ast.ASTForLoop;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTGlobalAssignment;
import com.github.lessjava.types.ast.ASTList;
import com.github.lessjava.types.ast.ASTLiteral;
import com.github.lessjava.types.ast.ASTMap;
import com.github.lessjava.types.ast.ASTMemberAccess;
import com.github.lessjava.types.ast.ASTMethod;
import com.github.lessjava.types.ast.ASTMethodCall;
import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.ast.ASTSet;
import com.github.lessjava.types.ast.ASTTest;
import com.github.lessjava.types.ast.ASTUnaryExpr;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.ast.ASTVoidAssignment;
import com.github.lessjava.types.ast.ASTVoidFunctionCall;
import com.github.lessjava.types.ast.ASTVoidMethodCall;
import com.github.lessjava.types.ast.ASTWhileLoop;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.impl.HMTypeVar;

public class LJASTConverter extends LJBaseListener {
    private ASTProgram ast;
    private LinkedHashMap<ParserRuleContext, ASTNode> parserASTMap;

    private Stack<ASTBlock> blocks;

    private ASTClassBlock currentClassBlock;
    private ASTClassSignature currentClassSignature;

    public LJASTConverter() {
        parserASTMap = new LinkedHashMap<>();
        blocks = new Stack<ASTBlock>();
    }

    @Override
    public void exitProgram(ProgramContext ctx) {
        ast = new ASTProgram();

        for (Class_Context c : ctx.class_()) {
            ast.classes.add((ASTClass) parserASTMap.get(c));
        }

        for (FunctionContext f : ctx.function()) {
            ast.functions.add((ASTAbstractFunction) parserASTMap.get(f));
        }

        for (GlobalContext g : ctx.global()) {
            ast.globals.add((ASTGlobalAssignment) parserASTMap.get(g));
        }

        for (TestContext t : ctx.test()) {
            ast.tests.add((ASTTest) parserASTMap.get(t));
        }

        addLibraryFunctions();

        ast.setDepth(ctx.depth());

        parserASTMap.put(ctx, ast);
    }

    @Override
    public void exitClass_(Class_Context ctx) {
        ASTClass class_;

        ASTClassSignature classSignature = (ASTClassSignature) parserASTMap.get(ctx.classSignature());
        ASTClassBlock classBlock = (ASTClassBlock) parserASTMap.get(ctx.classBlock());

        class_ = new ASTClass(classSignature, classBlock);
        class_.setDepth(ctx.depth());

        parserASTMap.put(ctx, class_);
    }

    @Override
    public void exitClassSignature(ClassSignatureContext ctx) {
        ASTClassSignature signature;

        String className = ctx.name.getText();
        String superName = ctx.superName == null ? null : ctx.superName.getText();

        signature = new ASTClassSignature(className, superName);

        this.currentClassSignature = signature;

        signature.setDepth(ctx.depth());
        parserASTMap.put(ctx, signature);
    }

    @Override
    public void enterClassBlock(ClassBlockContext ctx) {
        this.currentClassBlock = new ASTClassBlock();

        this.currentClassBlock.setDepth(ctx.depth());

        parserASTMap.put(ctx, this.currentClassBlock);
    }

    @Override
    public void exitAttribute(AttributeContext ctx) {
        ASTAttribute attribute;

        String scope = ctx.scope.getText();
        ASTAssignment assignment = (ASTAssignment) parserASTMap.get(ctx.assignment());

        attribute = new ASTAttribute(scope, assignment);

        attribute.setDepth(ctx.depth());

        this.currentClassBlock.addAttribute(attribute);

        parserASTMap.put(ctx, attribute);
    }

    @Override
    public void exitMethod(MethodContext ctx) {
        ASTMethod method;
        String scope;

        boolean isConstructor = false;

        // Null if constructor
        if (ctx.scope == null) {
            scope = ASTClass.PUBLIC;
            isConstructor = true;
        } else {
            scope = ctx.scope.getText();
        }

        ASTFunction function = (ASTFunction) parserASTMap.get(ctx.function());

        method = new ASTMethod(scope, function, this.currentClassSignature.className);
        method.isConstructor = isConstructor;
        method.containingClassName = currentClassSignature.className;

        currentClassBlock.methods.add(method);

        if (isConstructor) {
            currentClassBlock.constructor = method;
        }

        method.setDepth(ctx.depth());

        parserASTMap.put(ctx, method);
    }


    @Override
    public void exitFunction(FunctionContext ctx) {
        ASTFunction function;
        ASTFunction.Parameter parameter;

        function = new ASTFunction(ctx.ID().getText(), (ASTBlock) parserASTMap.get(ctx.block()));
        if (ctx.paramList() != null && ctx.paramList().ID().size() > 0) {
            for (TerminalNode tn : ctx.paramList().ID()) {
                parameter = new ASTFunction.Parameter(tn.getText(), new HMTypeVar());
                function.parameters.add(parameter);
            }
        }

        function.setDepth(ctx.depth());

        parserASTMap.put(ctx, function);
    }

    @Override
    public void exitTest(TestContext ctx) {
        ASTTest test;
        ASTExpression expr;

        expr = (ASTExpression) parserASTMap.get(ctx.expr());
        test = new ASTTest(expr);

        test.setDepth(ctx.depth());

        parserASTMap.put(ctx, test);
    }

    @Override
    public void exitGlobal(GlobalContext ctx) {
        ASTGlobalAssignment globalAssignment;
        ASTAssignment assignment;

        assignment = (ASTAssignment) parserASTMap.get(ctx.assignment());
        globalAssignment = new ASTGlobalAssignment(assignment);

        globalAssignment.setDepth(ctx.depth());

        parserASTMap.put(ctx, globalAssignment);
    }

    @Override
    public void enterBlock(BlockContext ctx) {
        ASTBlock block;

        block = new ASTBlock();

        blocks.push(block);

        block.setDepth(ctx.depth());

        parserASTMap.put(ctx, block);
    }

    @Override
    public void exitBlock(BlockContext ctx) {
        blocks.pop();
    }

    @Override
    public void exitArgList(ArgListContext ctx) {
        ASTArgList argList;

        List<ASTExpression> args = new ArrayList<>();

        for (ExprContext e : ctx.expr()) {
            args.add((ASTExpression) parserASTMap.get(e));
        }

        for (EntryContext e : ctx.entry()) {
            args.add((ASTExpression) parserASTMap.get(e));
        }

        argList = new ASTArgList(args);

        parserASTMap.put(ctx, argList);
    }

    @Override
    public void exitVoidAssignment(VoidAssignmentContext ctx) {
        ASTVoidAssignment voidAssignment;
        ASTAssignment assignment;

        assignment = (ASTAssignment) parserASTMap.get(ctx.assignment());
        voidAssignment = new ASTVoidAssignment(assignment);

        if (!blocks.empty()) {
            blocks.peek().statements.add(voidAssignment);
        }

        voidAssignment.setDepth(ctx.depth());

        parserASTMap.put(ctx, voidAssignment);
    }

    @Override
    public void exitConditional(ConditionalContext ctx) {
        ASTConditional conditional;
        ASTExpression condition;
        ASTBlock ifBlock;
        ASTBlock elseBlock;

        condition = (ASTExpression) parserASTMap.get(ctx.expr());
        ifBlock = (ASTBlock) parserASTMap.get(ctx.block().get(0));

        if (ctx.block().size() > 1) {
            elseBlock = (ASTBlock) parserASTMap.get(ctx.block().get(1));
            conditional = new ASTConditional(condition, ifBlock, elseBlock);
        } else {
            conditional = new ASTConditional(condition, ifBlock);
        }

        if (!blocks.empty()) {
            blocks.peek().statements.add(conditional);
        }

        conditional.setDepth(ctx.depth());

        parserASTMap.put(ctx, conditional);
    }

    @Override
    public void exitWhile(WhileContext ctx) {
        ASTWhileLoop whileLoop;
        ASTExpression guard;
        ASTBlock body;

        guard = (ASTExpression) parserASTMap.get(ctx.expr());
        body = (ASTBlock) parserASTMap.get(ctx.block());

        whileLoop = new ASTWhileLoop(guard, body);

        if (!blocks.empty()) {
            blocks.peek().statements.add(whileLoop);
        }

        whileLoop.setDepth(ctx.depth());

        parserASTMap.put(ctx, whileLoop);
    }

    @Override
    public void exitFor(ForContext ctx) {
        ASTForLoop forLoop;
        ASTVariable var;
        ASTExpression lowerBound;
        ASTExpression upperBound;
        ASTBlock block;

        var = (ASTVariable) parserASTMap.get(ctx.var());
        block = (ASTBlock) parserASTMap.get(ctx.block());

        if (ctx.expr().size() > 1) {
            lowerBound = (ASTExpression) parserASTMap.get(ctx.expr(0));
            upperBound = (ASTExpression) parserASTMap.get(ctx.expr(1));
            forLoop = new ASTForLoop(var, lowerBound, upperBound, block);
        } else {
            upperBound = (ASTExpression) parserASTMap.get(ctx.expr(0));
            forLoop = new ASTForLoop(var, upperBound, block);
        }

        if (!blocks.empty()) {
            blocks.peek().statements.add(forLoop);
        }

        forLoop.setDepth(ctx.depth());

        parserASTMap.put(ctx, forLoop);
    }

    @Override
    public void exitReturn(ReturnContext ctx) {
        ASTReturn ret;
        ASTExpression expression;

        expression = (ASTExpression) parserASTMap.get(ctx.expr());

        ret = new ASTReturn(expression);

        if (!blocks.empty()) {
            blocks.peek().statements.add(ret);
        }

        ret.setDepth(ctx.depth());

        parserASTMap.put(ctx, ret);
    }

    @Override
    public void exitBreak(BreakContext ctx) {
        ASTBreak br;

        br = new ASTBreak();

        if (!blocks.empty()) {
            blocks.peek().statements.add(br);
        }

        br.setDepth(ctx.depth());

        parserASTMap.put(ctx, br);
    }

    @Override
    public void exitContinue(ContinueContext ctx) {
        ASTContinue cont;

        cont = new ASTContinue();

        if (!blocks.empty()) {
            blocks.peek().statements.add(cont);
        }

        cont.setDepth(ctx.depth());

        parserASTMap.put(ctx, cont);
    }

    @Override
    public void exitVoidFunctionCall(VoidFunctionCallContext ctx) {
        ASTVoidFunctionCall voidFuncCall;
        ASTFunctionCall functionCall;

        functionCall = (ASTFunctionCall) parserASTMap.get(ctx.funcCall());

        voidFuncCall = new ASTVoidFunctionCall(functionCall);

        voidFuncCall.setDepth(ctx.depth());

        if (!blocks.empty()) {
            blocks.peek().statements.add(voidFuncCall);
        }

        parserASTMap.put(ctx, voidFuncCall);
    }

    @Override
    public void exitVoidMethodCall(VoidMethodCallContext ctx) {
        ASTVoidMethodCall voidMethodCall;
        ASTMethodCall methodCall;

        methodCall = (ASTMethodCall) parserASTMap.get(ctx.methodCall());
        voidMethodCall = new ASTVoidMethodCall(methodCall);

        voidMethodCall.setDepth(ctx.depth());

        if (!blocks.empty()) {
            blocks.peek().statements.add(voidMethodCall);
        }

        parserASTMap.put(ctx, voidMethodCall);
    }

    @Override
    public void exitExpr(ExprContext ctx) {
        ASTExpression expr;

        expr = (ASTExpression) parserASTMap.get(ctx.exprBin());

        expr.setDepth(ctx.depth());

        parserASTMap.put(ctx, expr);
    }

    @Override
    public void exitExprBin(ExprBinContext ctx) {
        ASTExpression expr;
        ASTExpression left, right;
        BinOp binOp;

        if (ctx.op == null && ctx.assignment() == null && ctx.methodCall() == null) {
            expr = (ASTExpression) parserASTMap.get(ctx.exprUn());
        } else if (ctx.assignment() != null) {
            expr = (ASTAssignment) parserASTMap.get(ctx.assignment());
        } else if (ctx.methodCall() != null) {
            expr = (ASTMethodCall) parserASTMap.get(ctx.methodCall());
        } else {
            left = (ASTExpression) parserASTMap.get(ctx.left);
            right = (ASTExpression) parserASTMap.get(ctx.right);
            binOp = ASTBinaryExpr.stringToOp(ctx.op.getText());

            expr = new ASTBinaryExpr(binOp, left, right);
        }

        expr.setDepth(ctx.depth());

        parserASTMap.put(ctx, expr);
    }

    @Override
    public void exitExprUn(ExprUnContext ctx) {
        ASTUnaryExpr unExpr;
        ASTUnaryExpr.UnaryOp op;
        ASTExpression expr;

        // If base expression
        if (ctx.op == null) {
            expr = (ASTExpression) parserASTMap.get(ctx.exprBase());

            expr.setDepth(ctx.depth());

            parserASTMap.put(ctx, expr);
        } else {
            op = findUnaryOp(ctx.op.getText());
            expr = (ASTExpression) parserASTMap.get(ctx.expression);

            unExpr = new ASTUnaryExpr(op, expr);

            unExpr.setDepth(ctx.depth());

            parserASTMap.put(ctx, unExpr);
        }
    }

    @Override
    public void exitExprBase(ExprBaseContext ctx) {
        ASTExpression expr;

        if (ctx.memberAccess() != null) {
            expr = (ASTMemberAccess) parserASTMap.get(ctx.memberAccess());
        } else if (ctx.funcCall() != null) {
            expr = (ASTFunctionCall) parserASTMap.get(ctx.funcCall());
        } else if (ctx.methodCall() != null) {
            expr = (ASTMethodCall) parserASTMap.get(ctx.methodCall());
        } else if (ctx.collection() != null) {
            expr = (ASTCollection) parserASTMap.get(ctx.collection());
        } else if (ctx.var() != null) {
            expr = (ASTVariable) parserASTMap.get(ctx.var());
        } else if (ctx.lit() != null) {
            expr = (ASTLiteral) parserASTMap.get(ctx.lit());
        } else {
            expr = (ASTExpression) parserASTMap.get(ctx.expr());
        }

        expr.setDepth(ctx.depth());

        parserASTMap.put(ctx, expr);
    }

    @Override
    public void exitAssignment(AssignmentContext ctx) {
        BinOp op;
        ASTAssignment assignment;
        ASTVariable variable;
        ASTMemberAccess memberAccess;
        ASTExpression expression;

        op = ASTBinaryExpr.stringToOp(ctx.op.getText());
        variable = (ASTVariable) parserASTMap.get(ctx.var());
        memberAccess = (ASTMemberAccess) parserASTMap.get(ctx.memberAccess());
        expression = (ASTExpression) parserASTMap.get(ctx.expr());


        if (expression instanceof ASTArgList) {
            variable.isCollection = true;
        }

        if (variable != null) {
            assignment = new ASTAssignment(op, variable, expression);
        } else {
            assignment = new ASTAssignment(op, memberAccess, expression);
        }


        assignment.setDepth(ctx.depth());

        parserASTMap.put(ctx, assignment);
    }

    @Override
    public void exitFuncCall(FuncCallContext ctx) {
        ASTFunctionCall funcCall;

        funcCall = new ASTFunctionCall(ctx.ID().getText());

        funcCall.setDepth(ctx.depth());

        parserASTMap.put(ctx, funcCall);

        if (ctx.argList() == null) {
            return;
        }

        for (ExprContext expr : ctx.argList().expr()) {
            funcCall.arguments.add((ASTExpression) parserASTMap.get(expr));
        }
    }

    @Override
    public void exitMethodCall(MethodCallContext ctx) {
        ASTMethodCall methodCall;

        ASTExpression invoker;

        if (ctx.var() != null) {
            invoker = (ASTVariable) parserASTMap.get(ctx.var());
        } else {
            invoker = (ASTFunctionCall) parserASTMap.get(ctx.funcCall().get(0));
        }

        List<ASTFunctionCall> calls = ctx.funcCall().stream().map(c -> parserASTMap.get(c))
                .map(c -> (ASTFunctionCall) c).collect(Collectors.toList());

        ASTFunctionCall funcCall = calls.get(calls.size() - 1);

        methodCall = new ASTMethodCall(invoker, funcCall);

        methodCall.setDepth(ctx.depth());

        parserASTMap.put(ctx, methodCall);
    }

    @Override
    public void exitVar(VarContext ctx) {
        ASTVariable var;

        if (ctx.expr() == null) {
            var = new ASTVariable(ctx.name.getText());
        } else {
            var = new ASTVariable(ctx.name.getText(), (ASTExpression) parserASTMap.get(ctx.expr()));
        }

        var.setDepth(ctx.depth());

        parserASTMap.put(ctx, var);
    }

    @Override
    public void exitMemberAccess(MemberAccessContext ctx) {
        ASTMemberAccess memberAccess;

        String className = ctx.instance.getText();
        String referencedClassName = className;
        ASTVariable var = (ASTVariable) parserASTMap.get(ctx.var());

        if (className.equals("this")) {
            referencedClassName = this.currentClassSignature.className;
        } else if(className.equals("super")) {
            referencedClassName = this.currentClassSignature.superName;
        }

        memberAccess = new ASTMemberAccess(className, referencedClassName, var);

        memberAccess.setDepth(ctx.depth());

        parserASTMap.put(ctx, memberAccess);
    }

    @Override
    public void exitLit(LitContext ctx) {
        ASTLiteral lit;

        if (ctx.BOOL() != null) {
            lit = new ASTLiteral(HMType.BaseDataType.BOOL, Boolean.parseBoolean(ctx.BOOL().getText()));
        } else if (ctx.INT() != null) {
            lit = new ASTLiteral(HMType.BaseDataType.INT, Integer.parseInt(ctx.INT().getText().trim()));
        } else if (ctx.REAL() != null) {
            lit = new ASTLiteral(HMType.BaseDataType.DOUBLE, Double.parseDouble(ctx.REAL().getText().trim()));
        } else {
            assert (ctx.STR() != null);
            lit = new ASTLiteral(HMType.BaseDataType.STR,
                    ctx.STR().getText().substring(1, ctx.STR().getText().length() - 1));
        }

        lit.setDepth(ctx.depth());

        parserASTMap.put(ctx, lit);
    }

    @Override
    public void exitList(ListContext ctx) {
        ASTList list;
        ASTArgList initialElements;

        initialElements = (ASTArgList) parserASTMap.get(ctx.argList());
        list = new ASTList(initialElements);

        list.setDepth(ctx.depth());

        parserASTMap.put(ctx, list);
    }

    @Override
    public void exitSet(SetContext ctx) {
        ASTSet set;
        ASTArgList initialElements;

        initialElements = (ASTArgList) parserASTMap.get(ctx.argList());
        set = new ASTSet(initialElements);

        set.setDepth(ctx.depth());

        parserASTMap.put(ctx, set);
    }

    @Override
    public void exitMap(MapContext ctx) {
        ASTMap map;
        ASTArgList initialElements;

        initialElements = (ASTArgList) parserASTMap.get(ctx.argList());
        map = new ASTMap(initialElements);

        map.setDepth(ctx.depth());

        parserASTMap.put(ctx, map);
    }

    @Override
    public void exitEntry(EntryContext ctx) {
        ASTEntry entry;
        ASTExpression key;
        ASTExpression value;

        key = (ASTExpression) parserASTMap.get(ctx.key);
        value = (ASTExpression) parserASTMap.get(ctx.value);
        entry = new ASTEntry(key, value);

        entry.setDepth(ctx.depth());

        parserASTMap.put(ctx, entry);
    }

    public ASTProgram getAST() {
        return ast;
    }

    public ASTUnaryExpr.UnaryOp findUnaryOp(String op) {
        switch (op) {
            case "!":
                return ASTUnaryExpr.UnaryOp.NOT;
            case "-":
                return ASTUnaryExpr.UnaryOp.NEG;
            default:
                return ASTUnaryExpr.UnaryOp.INVALID;
        }
    }

    /**
     * Add Library functions
     */
    private void addLibraryFunctions() {
        for (ASTFunction f : ASTFunction.libraryFunctions) {
            f.setDepth(2);
            f.setParent(ast);
        }

        for (ASTClass c : ASTClass.libraryClasses) {
            for (ASTMethod m : c.block.methods) {
                m.setParent(c.block);
            }
        }

        ast.functions.addAll(ASTFunction.libraryFunctions);
        ast.classes.addAll(ASTClass.libraryClasses);
    }
}
