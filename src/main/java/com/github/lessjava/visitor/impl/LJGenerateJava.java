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
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTBinaryExpr.BinOp;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTBreak;
import com.github.lessjava.types.ast.ASTConditional;
import com.github.lessjava.types.ast.ASTContinue;
import com.github.lessjava.types.ast.ASTExpression;
import com.github.lessjava.types.ast.ASTForLoop;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTGlobalAssignment;
import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.ast.ASTTest;
import com.github.lessjava.types.ast.ASTVariable;
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

    public Path mainFile = Paths.get("generated/Main.java");

    private List<String> lines = new ArrayList<>();
    private List<String> globalLines = new ArrayList<>();
    private List<String> testLines = new ArrayList<>();
    private List<String> mainLines = new ArrayList<>();
    private List<String> functionLines = new ArrayList<>();
    private List<String> testDeclarationLines = new ArrayList<>();
    private List<String> mainDeclarationLines = new ArrayList<>();
    private Set<String> functionDeclarationLines = new HashSet<>();
    private Set<String> mainVariables = new HashSet<>();
    private Set<String> functionVariables = new HashSet<>();
    private int indent = 1;
    private int testIndex = 0;

    private ASTFunction currentFunction;

    @Override
    public void preVisit(ASTProgram node) {
        this.currentFunction = null;

        String imports = "import static org.junit.Assert.*;\n\n" + "import org.junit.Test;\n" + "import java.util.*;\n"
                + "import java.io.*;";

        lines.add(imports);

        lines.add("public class Main");
        lines.add("{");

        String spaces = String.format("%" + (indent * 4) + "s", "");
        mainLines.add(String.format("\n%spublic static void main(String[] args)", spaces));
        mainLines.add(String.format("%s{", spaces));

        spaces = String.format("%" + (indent * 8) + "s", "");
        mainLines.add(String.format("%sScanner scn = new Scanner(System.in);", spaces));
    }

    @Override
    public void postVisit(ASTProgram node) {
        String spaces = String.format("%" + (indent * 4) + "s", "");


        lines.addAll(globalLines);
        lines.addAll(testDeclarationLines);
        mainLines.add(String.format("%s}", spaces));
        mainLines.addAll(2, mainDeclarationLines);
        lines.addAll(testLines);
        lines.addAll(mainLines);
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

        // Skip main
        if (node.name.equals("main")) {
            return;
        }

        // Library function
        if (node.body == null) {
            return;
        }

        // Prototype
        if (!node.concrete) {
            return;
        }

        // Add parameters so they don't get declared
        this.functionVariables.addAll(node.parameters.stream().map(Parameter::getName).collect(Collectors.toList()));

        String line = "";
        String parameterString = this.currentFunction.parameters.toString().substring(1,
                this.currentFunction.parameters.toString().length() - 1);
        String functionHeader = String.format("public static %s %s(%s)", this.currentFunction.returnType.toString(),
                this.currentFunction.name, parameterString);

        line = String.format("%s", functionHeader);

        addLine(node, line);
    }

    @Override
    public void postVisit(ASTFunction node) {
        this.currentFunction = null;
    }

    @Override
    public void preVisit(ASTGlobalAssignment node) {
        String line = String.format("public static final %s %s = %s;", node.variable.type, node.variable, node.value);
        addLine(node, line);
    }

    @Override
    public void postVisit(ASTGlobalAssignment node) {
    }

    @Override
    public void preVisit(ASTBlock node) {
        ASTFunction parent = node.getParent() instanceof ASTFunction ? (ASTFunction) node.getParent() : null;

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

        if (parent != null && !parent.name.equals("main")) {
            functionLines.addAll(2, functionDeclarationLines);
            lines.addAll(functionLines);
        }
    }

    @Override
    public void preVisit(ASTVoidAssignment node) {
        // Emit declarations
        if (this.currentFunction == null || this.currentFunction.name.equals("main")) {
            if (node.variable.name.startsWith("__")) {
                String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");
                String declaration = String.format("%sprivate static %s %s;", spaces, node.variable.type,
                        node.variable.name);

                testDeclarationLines.add(declaration);
            } else if (!mainVariables.contains(node.variable.name)) {
                String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");

                String declaration = String.format("%s%s%s %s;", spaces, spaces, node.variable.type,
                        node.variable.name);

                mainDeclarationLines.add(declaration);
                mainVariables.add(node.variable.name);
            }
        } else if (!functionVariables.contains(node.variable.name) && node.variable.type.isConcrete) {
            String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");

            String declaration = String.format("%s%s %s;", spaces, node.variable.type, node.variable.name);

            functionVariables.add(node.variable.name);
            functionDeclarationLines.add(declaration);
        }
        String line;

        if (node.op.equals(BinOp.ASGN)) {
            if (node.variable.isCollection && node.variable.index != null) {
                line = String.format("%s.set(%s, %s);", node.variable.name, node.variable.index, node.value);
            } else {
                line = String.format("%s = %s;", node.variable.name, node.value);
            }
        } else if (node.op.equals(BinOp.ADDASGN)) {
            if (node.variable.isCollection && node.variable.index == null) {
                line = String.format("%s.add(%s);", node.variable.name, node.value);
            } else {
                line = String.format("%s += %s;", node.variable.name, node.value);
            }
        } else if (node.op.equals(BinOp.SUBASGN)) {
            if (node.variable.isCollection && node.variable.index == null) {
                line = String.format("%s.remove(%s);", node.variable.name, node.value);
            } else {
                line = String.format("%s -= %s;", node.variable.name, node.value);
            }
        } else {
            line = "---Assignment Error---";
        }

        addLine(node, line);
    }

    @Override
    public void preVisit(ASTAssignment node) {
        // Emit declarations
        if (this.currentFunction == null) {
            if (node.variable.name.startsWith("__")) {
                String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");
                String declaration = String.format("%sprivate static %s %s;", spaces, node.variable.type,
                        node.variable.name);

                testDeclarationLines.add(declaration);
            } else if (!mainVariables.contains(node.variable.name)) {
                String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");

                String declaration = String.format("%s%s%s %s;", spaces, spaces, node.variable.type,
                        node.variable.name);

                mainDeclarationLines.add(declaration);
                mainVariables.add(node.variable.name);
            }
        } else if (!functionVariables.contains(node.variable.name)) {
            String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");

            String declaration = String.format("%s%s %s;", spaces, node.variable.type, node.variable.name);

            functionVariables.add(node.variable.name);
            functionDeclarationLines.add(declaration);
        }
    }

    @Override
    public void preVisit(ASTBinaryExpr node) {
        if (!node.operator.equals(BinOp.ASGN)) {
            return;
        }

        ASTVariable var = (ASTVariable) node.leftChild;

        // Emit declarations
        if (this.currentFunction == null) {
            if (var.name.startsWith("__")) {
                String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");
                String declaration = String.format("%sprivate static %s %s;", spaces, var.type, var.name);

                testDeclarationLines.add(declaration);
            } else if (!mainVariables.contains(var.name)) {
                String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");

                String declaration = String.format("%s%s%s %s;", spaces, spaces, var.type, var.name);

                mainDeclarationLines.add(declaration);
                mainVariables.add(var.name);
            }
        } else if (!functionVariables.contains(var.name)) {
            String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");

            String declaration = String.format("%s%s %s;", spaces, var.type, var.name);

            functionVariables.add(var.name);
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

        line = "    PrintStream originalStream = System.out;";
        addLine(node, line);
        line = "    System.setOut(new PrintStream(new OutputStream() { public void write(int i) {} }));";
        addLine(node, line);

        line = String.format("    main(null);");
        addLine(node, line);

        if (node.expr instanceof ASTBinaryExpr) {
            ASTBinaryExpr expr = (ASTBinaryExpr) node.expr;

            if (expr.leftChild.type.equals(HMTypeBase.INT) ||  expr.leftChild.type.equals(HMTypeBase.REAL)) {
                line = String.format("%4sassertEquals(%s, %s);", "", expr.rightChild, expr.leftChild);
            } else {
                line = String.format("%4sassertTrue(%s);", "", node.expr);
            }

        } else {
            line = String.format("%4sassertTrue(%s);", "", node.expr);
        }

        addLine(node, line);

        line = "    System.setOut(originalStream);";
        addLine(node, line);

        line = String.format("}");
        addLine(node, line);
    }

    @Override
    public void preVisit(ASTVoidFunctionCall node) {
        String line;

        String arguments = node.arguments.stream().map(ASTExpression::toString).collect(Collectors.joining(","))
                .replaceAll("\\\\\"", "");

        List<String> printArgs = new ArrayList<>();
        if (ASTFunction.libraryFunctionStrings.containsKey(node.name)) {
            switch (node.name) {
                case "print":
                case "println":
                case "printf":
                    for (ASTExpression e : node.arguments) {
                        printArgs.add(e.type instanceof HMTypeCollection ? e.toString() + ".toString()" : e.toString());
                    }

                    arguments = String.join(",", printArgs).replaceAll("\\\\\"", "");
                    line = String.format(ASTFunction.libraryFunctionStrings.get(node.name), arguments);
                    break;
                default:
                    line = String.format("%s;", ASTFunction.libraryFunctionStrings.get(node.name));
            }
        } else {
            line = String.format("%s(%s);", node.name, arguments);
        }

        addLine(node, line);
    }

    @Override
    public void preVisit(ASTVoidMethodCall node) {
        String line;

        line = String.format("%s.%s;", node.invoker, node.funcCall);

        addLine(node, line);
    }

    private void addLine(ASTNode node, String line) {
        // Don't emit prototypes
        if (this.currentFunction != null && !this.currentFunction.concrete) {
            return;
        }

        String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");

        if (node instanceof ASTTest) {
            testLines.add(String.format("%s%s", spaces, line));
        } else if (node instanceof ASTGlobalAssignment) {
            globalLines.add(String.format("%s%s", spaces, line));
        } else if (currentFunction == null || currentFunction.name.equals("main")) {
            mainLines.add(String.format("%s%s%s", spaces, spaces, line));
        } else {
            functionLines.add(String.format("%s%s", spaces, line));
        }
    }

}
