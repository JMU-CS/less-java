package com.github.lessjava;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.PrintWriter;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.github.lessjava.generated.LJLexer;
import com.github.lessjava.generated.LJParser;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.visitor.impl.BuildParentLinks;
import com.github.lessjava.visitor.impl.BuildSymbolTables;
import com.github.lessjava.visitor.impl.LJASTBuildClassLinks;
import com.github.lessjava.visitor.impl.LJASTCheckTypesHaveChanged;
import com.github.lessjava.visitor.impl.LJASTConverter;
import com.github.lessjava.visitor.impl.LJASTInferConstructors;
import com.github.lessjava.visitor.impl.LJASTInferTypes;
import com.github.lessjava.visitor.impl.LJAssignTestVariables;
import com.github.lessjava.visitor.impl.LJGenerateJava;
import com.github.lessjava.visitor.impl.LJInstantiateFunctions;
import com.github.lessjava.visitor.impl.LJStaticAnalysis;
import com.github.lessjava.visitor.impl.StaticAnalysis;

public class LJUI extends JFrame implements ActionListener
{
    JButton runButton;
    JTextArea editField;
    JTextArea outputField;

    public LJUI()
    {
        setTitle("Less-Java");

        editField = new JTextArea();
        editField.setText("main() {\n    println(\"Hello, world!\")\n}");
        editField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        outputField = new JTextArea();
        editField.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));

        JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                editField, outputField);
        splitPanel.setResizeWeight(0.75);

        JToolBar toolbar = new JToolBar();
        JButton runButton = new JButton();
        runButton.setActionCommand("Run");
        runButton.setLabel("Run");
        runButton.setToolTipText("Run program");
        runButton.addActionListener(this);
        toolbar.add(runButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(toolbar, BorderLayout.NORTH);
        mainPanel.add(splitPanel, BorderLayout.CENTER);

        add(mainPanel);
        pack();
        setSize(400,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand() == "Run") {
            outputField.setText("Output:");
            run();
        }
    }

    public void run2()
    {
        String args[] = new String[] { ".tmp.lj" };
        try {
            PrintWriter out = new PrintWriter(".tmp.lj");
            out.println(editField.getText());
            out.close();
            LJCompiler.main(args);
        } catch (IOException e) {
            outputField.setText(e.getMessage());
            return;
        }
    }

    public void run()
    {
        // lex and parse
        LJLexer lexer = new LJLexer(new ANTLRInputStream(editField.getText()));
        LJParser parser = new LJParser(new CommonTokenStream(lexer));
        ParseTree parseTree = parser.program();
        if (parser.getNumberOfSyntaxErrors() != 0) {
            return;
        }

        // convert to AST
        ParseTreeWalker walker = new ParseTreeWalker();
        LJASTConverter converter = new LJASTConverter();
        walker.walk(converter, parseTree);
        ASTProgram program = converter.getAST();

        // initial processing
        program.traverse(new BuildParentLinks());
        program.traverse(new LJASTBuildClassLinks());
        if(!StaticAnalysis.getErrors().isEmpty()) {
            outputField.setText(StaticAnalysis.getErrorString());
            return;
        }

        // TODO: finish type checking and other static analysis
        program.traverse(new LJASTInferConstructors());
        program.traverse(new BuildSymbolTables());
        program.traverse(new LJAssignTestVariables());
        program.traverse(new LJStaticAnalysis());

        if (!StaticAnalysis.getErrors().isEmpty()) {
            outputField.setText(StaticAnalysis.getErrorString());
            return;
        }

        // code generation
        program.traverse(new LJGenerateJava(".Tmp.java"));

        // TODO: run Java compiler
        //JCompiler.compile();

        // TODO: run tests
        // TODO: run program

        outputField.setText(program.toString());
    }

    public static void main(String args[])
    {
        LJUI ui = new LJUI();
    }
}
