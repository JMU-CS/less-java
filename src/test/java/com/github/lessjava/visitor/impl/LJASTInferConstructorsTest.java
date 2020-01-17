package com.github.lessjava.visitor.impl;

import java.util.Optional;

import com.github.lessjava.generated.LJLexer;
import com.github.lessjava.generated.LJParser;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.types.ast.ASTClassBlock;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTMethod;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.ast.ASTVoidFunctionCall;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LJASTInferConstructorsTest {

    private ASTProgram compile(String programText) {
        LJLexer lexer = new LJLexer(new ANTLRInputStream(programText));
        ParseTreeWalker walker = new ParseTreeWalker();

        LJParser parser = new LJParser(new CommonTokenStream(lexer));
        LJASTConverter converter = new LJASTConverter();

        BuildParentLinks buildParentLinks = new BuildParentLinks();
        LJASTBuildClassLinks buildClassLinks = new LJASTBuildClassLinks();
        BuildSymbolTables buildSymbolTables = new BuildSymbolTables();
        LJASTInferTypes inferTypes = new LJASTInferTypes();
        // PrintDebugTree printTree = new PrintDebugTree();
        LJASTCheckTypesHaveChanged checkTypesHaveChanged = new LJASTCheckTypesHaveChanged();
        LJInstantiateFunctions instantiateFunctions = new LJInstantiateFunctions();
        LJASTInferConstructors inferConstructors = new LJASTInferConstructors();

        // ANTLR Parsing
        ParseTree parseTree = parser.program();

        // Convert to AST
        walker.walk(converter, parseTree);
        ASTProgram program = converter.getAST();

        // Apply visitors to AST
        program.traverse(buildParentLinks);
        program.traverse(buildClassLinks);
        if(!StaticAnalysis.getErrors().isEmpty()) {
            fail("Errors building class links:\n" + StaticAnalysis.getErrorString());
            return program;
        }
        program.traverse(inferConstructors);

        return program;
    }

    @BeforeEach
    public void init() {
        ASTClass.nameClassMap.clear();
        ASTClassBlock.nameAttributeMap.clear();
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
    }

    @Test
    public void testEmptyConstructorGenerated() {
        String programText =
                "A {\n" +
                "}";
        compile(programText);
        ASTClass a = ASTClass.nameClassMap.get("A");
        assertEquals(1, a.block.constructors.size());
        assertTrue(a.block.constructors.stream().anyMatch(constructor -> constructor.parameters.isEmpty()));
    }

    @Test
    public void testSuperConstructors() {
        String programText =
                "A {\n" +
                        "public num = 0\n" +
                        "A(num) {\n" +
                            "this.num = num\n" +
                        "}" +
                "}\n" +
                "B extends A {\n" +
                "}";
        compile(programText);
        ASTClass b = ASTClass.nameClassMap.get("B");
        assertEquals(2, b.block.constructors.size());
        Optional<ASTMethod> optionalConstructor = b.block.constructors.stream().filter(constructor -> constructor.parameters.size() == 1).findFirst();
        assertTrue(optionalConstructor.isPresent());
        ASTMethod constructor = optionalConstructor.get();
        ASTFunctionCall callToSuper = ((ASTVoidFunctionCall)constructor.function.body.statements.get(0)).functionCall;
        assertEquals("super", callToSuper.name);
        assertEquals(1, callToSuper.arguments.size());
        assertEquals("num", ((ASTVariable)callToSuper.arguments.get(0)).name);
    }

    @Test
    public void testConstructorsNotReplaced() {
        String programText =
                "A {\n" +
                        "public num = 0\n" +
                        "A(num) {\n" +
                            "this.num = num\n" +
                        "}" +
                "}\n" +
                "B extends A {\n" +
                        "B(num) {\n" +
                            "super(num+1)\n" +
                        "}" +
                "}";
        ASTProgram program = compile(programText);
        ASTClass b = ASTClass.nameClassMap.get("B");
        assertEquals(2, b.block.constructors.size());
        Optional<ASTMethod> optionalConstructor = b.block.constructors.stream().filter(constructor -> constructor.parameters.size() == 1).findFirst();
        assertTrue(optionalConstructor.isPresent());
        ASTMethod constructor = optionalConstructor.get();
        ASTFunctionCall callToSuper = ((ASTVoidFunctionCall)constructor.function.body.statements.get(0)).functionCall;
        assertEquals("super", callToSuper.name);
        assertEquals(1, callToSuper.arguments.size());
        assertTrue(callToSuper.arguments.get(0) instanceof ASTBinaryExpr);
    }

}