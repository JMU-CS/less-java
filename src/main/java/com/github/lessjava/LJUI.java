package com.github.lessjava;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.JSplitPane;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.github.lessjava.generated.LJLexer;
import com.github.lessjava.generated.LJParser;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.types.ast.ASTClassBlock;
import com.github.lessjava.visitor.impl.BuildParentLinks;
import com.github.lessjava.visitor.impl.BuildSymbolTables;
import com.github.lessjava.visitor.impl.PrintDebugTree;
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

public class LJUI extends JFrame
{
    public static void main(String args[])
    {
        (new LJUI()).setVisible(true);
    }

    public LJUI()
    {
        // main text fields in a split pane
        JTextArea editField = new JTextArea();
        editField.setText("main() {\n    println(\"Hello, world!\")\n}\n" +
                          "test 1+2 == 3\n");
        editField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JTextArea outputField = new JTextArea();
        editField.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
        JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                editField, outputField);
        splitPanel.setResizeWeight(0.33);

        // toolbar
        JButton testButton = new JButton();
        testButton.setLabel("Test");
        testButton.setToolTipText("Run tests");
        testButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            { outputField.setText(compile(editField.getText()) + test()); }
        });
        JButton runButton = new JButton();
        runButton.setLabel("Run");
        runButton.setToolTipText("Run program");
        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            { outputField.setText(compile(editField.getText()) + run()); }
        });
        JToolBar toolbar = new JToolBar();
        toolbar.add(testButton);
        toolbar.add(runButton);

        // top-level panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(toolbar, BorderLayout.NORTH);
        mainPanel.add(splitPanel, BorderLayout.CENTER);
        add(mainPanel);
        pack();

        // main window
        setTitle("Less-Java");
        setSize(500,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public String compile(String code)
    {
        // add newline to avoid EOL/EOF issues w/ ANTLR:
        //   https://www.antlr3.org/pipermail/antlr-interest/2011-January/040642.html
        code += "\n";

        // lexing and parsing (TODO: report errors in UI)
        LJLexer lexer = new LJLexer(new ANTLRInputStream(code));
        LJParser parser = new LJParser(new CommonTokenStream(lexer));
        ParseTree parseTree = parser.program();
        if (parser.getNumberOfSyntaxErrors() != 0) {
            return "";
        }

        // conversion to AST
        ParseTreeWalker walker = new ParseTreeWalker();
        LJASTConverter converter = new LJASTConverter();
        walker.walk(converter, parseTree);
        ASTProgram program = converter.getAST();

        // visitors
        BuildParentLinks buildParentLinks = new BuildParentLinks();
        BuildSymbolTables buildSymbolTables = new BuildSymbolTables();
        LJInstantiateFunctions instantiateFunctions = new LJInstantiateFunctions();
        LJASTInferTypes inferTypes = new LJASTInferTypes();
        LJASTInferConstructors inferConstructors = new LJASTInferConstructors();
        LJASTCheckTypesHaveChanged checkTypesHaveChanged = new LJASTCheckTypesHaveChanged();

        // reset static analysis (TODO: use fewer static data structures to
        // avoid having to do this)
        StaticAnalysis.resetErrors();
        BuildSymbolTables.nodeSymbolTableMap.clear();
        LJASTBuildClassLinks.nameClassMap.clear();
        LJASTBuildClassLinks.nameClassMap.put("Object",null);
        LJASTBuildClassLinks.nameClassMap.put("String", null);
        LJASTBuildClassLinks.nameClassMap.put("Integer", null);
        LJASTBuildClassLinks.nameClassMap.put("Double", null);
        LJASTBuildClassLinks.nameClassMap.put("Boolean", null);
        LJASTBuildClassLinks.nameClassMap.put("Char", null);
        LJASTBuildClassLinks.nameClassMap.put("LJList", null);
        LJASTBuildClassLinks.nameClassMap.put("LJSet", null);
        LJASTBuildClassLinks.nameClassMap.put("LJMap", null);
        ASTClass.nameClassMap.clear();
        ASTClassBlock.nameAttributeMap.clear();
        StaticAnalysis.collectErrors = true;

        // initial processing
        program.traverse(buildParentLinks);
        program.traverse(new LJASTBuildClassLinks());
        if(!StaticAnalysis.getErrors().isEmpty()) {
            return StaticAnalysis.getErrorString();
        }
        program.traverse(new LJASTInferConstructors());
        program.traverse(buildSymbolTables);

        // iterative type inference
        do {
            program.traverse(buildParentLinks);
            StaticAnalysis.resetErrors();
            program.traverse(buildSymbolTables);
            program.traverse(instantiateFunctions);
            program.traverse(inferTypes);
            program.traverse(checkTypesHaveChanged);
        } while (LJASTCheckTypesHaveChanged.typesChanged);

        // type checking
        program.traverse(new LJAssignTestVariables());
        program.traverse(new LJStaticAnalysis());
        if (!StaticAnalysis.getErrors().isEmpty()) {
            return StaticAnalysis.getErrorString();
        }

        // code generation
        program.traverse(new LJGenerateJava());

        // run Java compiler (TODO: report error messages in UI)
        LJCompiler.JCompiler.compile();

        return "";
    }

    private String test()
    {
        // TODO: fix output formatting
        return runShellCommand("java -jar ../libs/junit-platform-console-standalone-1.4.2.jar"
                + " --class-path generated:lj-ui.jar --include-classname='.*' --disable-banner -c LJTmp");
    }

    private String run()
    {
        return "Output:\n" + runShellCommand("java -cp generated:lj-ui.jar LJTmp");
    }

    /**
     * Runs a command as if it was executed from a command-line shell.
     *
     * @param command Text of command to execute
     * @return All output as a String
     */
    private static String runShellCommand(String command)
    {
        StringBuilder str = new StringBuilder();

        try {
            Process cmdProc = Runtime.getRuntime().exec(command);
            String line;

            BufferedReader stdoutReader = new BufferedReader(
                    new InputStreamReader(cmdProc.getInputStream()));
            while ((line = stdoutReader.readLine()) != null) {
                str.append(line + "\n");
            }

            BufferedReader stderrReader = new BufferedReader(
                    new InputStreamReader(cmdProc.getErrorStream()));
            while ((line = stderrReader.readLine()) != null) {
                str.append(line + "\n");
            }
        } catch (IOException ex) {
            str.append("ERROR: " + ex.getMessage());
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            str.append("ERROR: " + ex.getMessage());
        }

        return str.toString();
    }
}
