package com.github.lessjava.visitor.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.lessjava.types.ast.ASTAbstractFunction.Parameter;
import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTAttribute;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTBreak;
import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.types.ast.ASTClassBlock;
import com.github.lessjava.types.ast.ASTClassSignature;
import com.github.lessjava.types.ast.ASTConditional;
import com.github.lessjava.types.ast.ASTContinue;
import com.github.lessjava.types.ast.ASTForLoop;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTGlobalAssignment;
import com.github.lessjava.types.ast.ASTMethod;
import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.ast.ASTTest;
import com.github.lessjava.types.ast.ASTVoidAssignment;
import com.github.lessjava.types.ast.ASTVoidFunctionCall;
import com.github.lessjava.types.ast.ASTVoidMethodCall;
import com.github.lessjava.types.ast.ASTWhileLoop;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeCollection;
import com.github.lessjava.visitor.LJDefaultASTVisitor;

public class LJGenerateJava extends LJDefaultASTVisitor {

    // Create generated directory if it doesn't exist
    static {
        try {
            Files.createDirectories(Paths.get("generated"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Path mainFile = Paths.get("generated/Main.java");

    private static String imports = "import static org.junit.Assert.*;\n"
                                  + "import static wrappers.LJString.*;\n"
                                  + "\n"
                                  + "import org.junit.Test;\n"
                                  + "import java.util.*;\n"
                                  + "import java.io.*;\n"
                                  + "\n"
                                  + "import wrappers.*;\n";

    private List<String> lines = new ArrayList<>();
    private List<String> globalLines = new ArrayList<>();
    private List<String> testLines = new ArrayList<>();
    private List<String> functionLines = new ArrayList<>();
    private List<String> classLines = new ArrayList<>();
    private List<String> testDeclarationLines = new ArrayList<>();
    private Set<String> functionDeclarationLines = new HashSet<>();
    private Set<String> functionVariables = new HashSet<>();
    private int indent = 1;
    private int testIndex = 0;

    private ASTFunction currentFunction;
    private ASTClass currentClass;

    private ASTMethod currentMethod;

    @Override
    public void preVisit(ASTProgram node) {
        this.currentFunction = null;

        lines.add(imports);

        lines.add("public class Main");
        lines.add("{");
    }

    @Override
    public void postVisit(ASTProgram node) {
        lines.addAll(globalLines);
        lines.addAll(testDeclarationLines);
        lines.addAll(testLines);
        lines.addAll(classLines);
        lines.add("}");

        try {
            Files.write(mainFile, lines, Charset.forName("UTF-8"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void preVisit(ASTFunction node) {
        this.currentFunction = node;

        this.functionLines.clear();
        this.functionVariables.clear();
        this.functionDeclarationLines.clear();

        // Library function
        if (inLibrary(node)) {
            return;
        }

        // Prototype
        if (!node.concrete) {
            return;
        }

        // Add parameters so they don't get declared
        this.functionVariables.addAll(node.parameters.stream()
                                                     .map(Parameter::getName)
                                                     .collect(Collectors.toList()));

        String line = "";

        String parameterString;

        // Inject main arguments
        if (node.name.equals("main")) {
            parameterString = "String[] args";
        } else {
            parameterString = node.parameters.toString().substring(1,
                    node.parameters.toString().length() - 1);
        }

        String returnType = ASTClass.nameClassMap.containsKey(node.name) ? "" : node.returnType.toString() + " ";
        String name = node.name;
        String scope = this.currentMethod == null ? ASTClass.PUBLIC : this.currentMethod.scope;
        String _static = this.currentMethod == null ? "static" : "";

        String signature = String.format("%s %s %s%s(%s)", scope, _static, returnType, name, parameterString);

        line = String.format("%s", signature);

        addLine(node, line);
    }

    @Override
    public void postVisit(ASTFunction node) {
        // Library function
        if (inLibrary(node)) {
            this.currentFunction = null;
            return;
        }

        // Prototype
        if (!node.concrete) {
            this.currentFunction = null;
            return;
        }
        try {
            functionLines.addAll(2, functionDeclarationLines);
        } catch(IndexOutOfBoundsException ioobe) {
            functionLines.addAll(functionDeclarationLines);
        }

        lines.addAll(functionLines);
    }

    @Override
    public void preVisit(ASTClass node) {
        this.currentClass = node;

        // Library class
        if (inLibrary(node)) {
            this.currentClass = null;
            return;
        }

    }

    @Override
    public void postVisit(ASTClass node) {
        this.currentClass = null;
    }

    @Override
    public void preVisit(ASTClassSignature node) {
        // Library class
        if (this.currentClass == null) {
            return;
        }

        String className = node.className;
        String superName = node.superName;

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("private static class %s", className));
        if (superName != null) {
            sb.append(String.format(" extends %s", superName));
        }

        String line = sb.toString();

        addLine(node, line);
    }

    @Override
    public void preVisit(ASTClassBlock node) {
        // Library class
        if (this.currentClass == null) {
            return;
        }

        String line = "{";

        addLine(node, line);

        indent++;
    }

    @Override
    public void postVisit(ASTClassBlock node) {
        // Library class
        if (this.currentClass == null) {
            return;
        }

        indent--;

        String line = "}";
        addLine(node, line);
    }

    @Override
    public void preVisit(ASTAttribute node) {
        String line = String.format("%s", node);

        addLine(node, line);
    }

    @Override
    public void preVisit(ASTMethod node) {
        this.currentMethod = node;
    }

    @Override
    public void postVisit(ASTMethod node) {
        this.currentMethod = null;
    }

    @Override
    public void preVisit(ASTGlobalAssignment node) {
        String line = String.format("public static final %s %s;", node.assignment.type, node.assignment.toString());

        addLine(node, line);
    }

    @Override
    public void preVisit(ASTBlock node) {
        ASTFunction parent = node.getParent() instanceof ASTFunction ? (ASTFunction) node.getParent() : null;

        // Ignore non-concrete function's blocks
        if (parent != null && !parent.concrete) {
            return;
        }

        String line = String.format("{");
        addLine(node, line);
        indent++;
    }

    @Override
    public void postVisit(ASTBlock node) {
        ASTFunction parent = node.getParent() instanceof ASTFunction ? (ASTFunction) node.getParent() : null;

        // Ignore non-concrete function's blocks
        if (parent != null && !parent.concrete) {
            return;
        }

        indent--;
        String line = String.format("}");
        addLine(node, line);

        if (parent != null) {
        }
    }

    @Override
    public void preVisit(ASTVoidAssignment node) {
        String line;

        line = String.format("%s;", node.assignment.toString());

        addLine(node, line);
    }

    @Override
    public void preVisit(ASTAssignment node) {
        if (node.getParent() instanceof ASTGlobalAssignment) {
            return;
        }

        // Emit declarations
        if (!functionVariables.contains(node.variable.name)) {
            String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");

            String declaration = String.format("%s%s %s;", spaces, node.variable.type, node.variable.name);

            functionVariables.add(node.variable.name);
            functionDeclarationLines.add(declaration);
        }
    }

    @Override
    public void preVisit(ASTConditional node) {
        String line = String.format("if (%s)", node.condition);
        addLine(node, line);
    }

    @Override
    public void inVisit(ASTConditional node) {
        String line = String.format("else ", node.condition);
        addLine(node, line);
    }

    @Override
    public void preVisit(ASTWhileLoop node) {
        String line = String.format("while (%s)", node.guard);
        addLine(node, line);
    }

    @Override
    public void preVisit(ASTForLoop node) {
        if (node.lowerBound == null) {
            HMType cType = ((HMTypeCollection) node.upperBound.type).elementType;
            String line = String.format("for (%s i : %s)", cType, node.upperBound);
            addLine(node, line);
        } else {
            String lowerBound = node.lowerBound == null ? "0" : node.lowerBound.toString();
            String upperBound = node.upperBound.toString();
            String line = String.format("for (Integer %s = %s; %s < %s; %s++)", node.var, lowerBound, node.var,
                    upperBound, node.var);
            addLine(node, line);
        }
    }

    @Override
    public void preVisit(ASTReturn node) {
        String line = String.format("return %s;", node.value);
        addLine(node, line);
    }

    @Override
    public void preVisit(ASTBreak node) {
        String line = String.format("break;", node);
        addLine(node, line);
    }

    @Override
    public void preVisit(ASTContinue node) {
        String line = String.format("continue;", node);
        addLine(node, line);
    }

    @Override
    public void preVisit(ASTTest node) {
        String line = String.format("@Test");
        addLine(node, line);

        line = String.format("public void test%d() {", testIndex++, "", node.expr);
        addLine(node, line);

        if (node.expr instanceof ASTBinaryExpr) {
            ASTBinaryExpr expr = (ASTBinaryExpr) node.expr;

            if (expr.leftChild.type.equals(HMTypeBase.INT) || expr.leftChild.type.equals(HMTypeBase.REAL)) {
                line = String.format("%4sassertEquals(%s, %s);", "", expr.rightChild, expr.leftChild);
            } else {
                line = String.format("%4sassertTrue(%s);", "", node.expr);
            }

        } else {
            line = String.format("%4sassertTrue(%s);", "", node.expr);
        }

        addLine(node, line);

        line = String.format("}");
        addLine(node, line);
    }

    @Override
    public void preVisit(ASTVoidFunctionCall node) {
        String line;

        line = String.format("%s;", node.functionCall.toString());

        addLine(node, line);
    }

    @Override
    public void preVisit(ASTVoidMethodCall node) {
        String line;

        line = String.format("%s;", node.methodCall.toString());

        addLine(node, line);
    }

    private boolean inLibrary(ASTNode node) {
        return ASTClass.libraryClasses.contains(node) || ASTFunction.libraryFunctions.contains(node);
    }

    private void addLine(ASTNode node, String line) {
        // Don't emit prototypes
        if (this.currentFunction != null && !this.currentFunction.concrete) {
            return;
        }

        String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");

        boolean inTest = node instanceof ASTTest;
        boolean inGlobal = node instanceof ASTGlobalAssignment;
        boolean inClass = this.currentClass != null;

        if (inTest) {
            testLines.add(String.format("%s%s", spaces, line));
        } else if (inGlobal) {
            globalLines.add(String.format("%s%s", spaces, line));
        } else if (inClass) {
            classLines.add(String.format("%s%s", spaces, line));
        } else {
            functionLines.add(String.format("%s%s", spaces, line));
        }
    }

}
