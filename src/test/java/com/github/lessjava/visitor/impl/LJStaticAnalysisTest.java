package com.github.lessjava.visitor.impl;

import com.github.lessjava.generated.LJLexer;
import com.github.lessjava.generated.LJParser;
import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.types.ast.ASTClassBlock;
import com.github.lessjava.types.ast.ASTClassSignature;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTProgram;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LJStaticAnalysisTest {
    private static final String CLASS_A =
            "A {\n" +
                    "public intMember = 0\n" +
                    "private privateMember = 1.0\n" +
                    "A(intMember) {\n" +
                        "this.intMember = intMember\n" +
                    "}\n" +
                    "getNumber() {\n" +
                        "return this.intMember\n" +
                    "}" +
            "}\n";

    private static final String CLASS_B =
            "B extends A {\n" +
                    "public strMember = \"\"\n" +
                    "B(intMember, strMember) {\n" +
                        "super(intMember)\n" +
                        "this.strMember = strMember\n" +
                    "}" +
            "}\n";

    private LJStaticAnalysis underTest;

    @BeforeEach
    public void init() {
        this.underTest = new LJStaticAnalysis();
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
    }

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
            return program;
        }
        program.traverse(inferConstructors);

        do {
            program.traverse(buildParentLinks); // Rebuild parent links in case we instantiated new functions
            StaticAnalysis.resetErrors();   // Remove errors found in old iterations
            program.traverse(buildSymbolTables);
            program.traverse(instantiateFunctions);
            program.traverse(inferTypes);
            program.traverse(checkTypesHaveChanged);
        } while (LJASTCheckTypesHaveChanged.typesChanged);

        LJAssignTestVariables assignTestVariables = new LJAssignTestVariables();

        program.traverse(assignTestVariables);
        return program;
    }

    private void assertInvalid(String programText) {
        ASTProgram program = compile(programText);
        program.traverse(underTest);
        if(!StaticAnalysis.getErrors().isEmpty()) {
            System.out.println("Errors:");
            StaticAnalysis.getErrors().stream().forEach(System.out::println);
        }
        assertFalse(StaticAnalysis.getErrors().isEmpty());
    }

    private void assertValid(String programText) {
        ASTProgram program = compile(programText);
        program.traverse(underTest);
        if(!StaticAnalysis.getErrors().isEmpty()) {
            System.out.println("Errors:");
            StaticAnalysis.getErrors().stream().forEach(System.out::println);
        }
        assertTrue(StaticAnalysis.getErrors().isEmpty());
    }

    @Test
    public void testIfInt_invalid() {
        assertInvalid("main() { if(0) {} }");
    }

    @Test
    public void testIfBool_valid() {
        assertValid("main() { if(true) {} }");
    }

    @Test
    public void testAddIntStr_invalid() {
        String program =
                "main() {\n" +
                        "a = 5 + \"test\"\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testAssignIntVarStrVal_invalid() {
        String program =
                "main() {\n" +
                        "a = 0\n" +
                        "a = \"test\"\n" +
                        "}";
        assertInvalid(program);
    }

    @Test
    public void testWhileInt_invalid() {
        assertInvalid("main() { while(5) {} }");
    }

    @Test
    public void testWhileBool_valid() {
        assertValid("main() { while(true) {} }");
    }

    @Test
    public void testIncrementIntByStr_invalid() {
        String program =
                "main() {\n" +
                        "a = 0\n" +
                        "a += \"hi\"\n" +
                        "}";
        assertInvalid(program);
    }

    @Test
    public void testBreakNotInWhile_invalid() {
        String program =
                "main() {\n" +
                        "break\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testContinueNotInWhile_invalid() {
        String program =
                "main() {\n" +
                        "continue\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testBreakInWhile_valid() {
        String program =
                "main() {\n" +
                        "while(true) {\n" +
                            "break\n" +
                        "}\n" +
                "}";
        assertValid(program);
    }

    @Test
    public void testContinueInWhile_valid() {
        String program =
                "main() {\n" +
                        "while(true) {\n" +
                            "continue\n" +
                        "}\n" +
                "}";
        assertValid(program);
    }

    @Test
    public void testIntMemberAccess_valid() {
        String program = CLASS_A +
                "main() {\n" +
                    "a = A()\n" +
                    "b = a.intMember\n" +
                "}";
        assertValid(program);
    }

    @Test
    public void testIntMemberAssignStr_invalid() {
        String program = CLASS_A +
                "main() {\n" +
                    "a = A()\n" +
                    "a.intMember = \"hi\"\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testAssignSuper_invalid() {
        String program =
                "main() {" +
                        "super = 0\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testAssignThis_invalid() {
        String program =
                "main() {\n" +
                        "this = 0\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testBExtendsAButANotDeclared_invalid() {
        String program =
                CLASS_B +
                "main() {\n" +
                        "b = B(a, \"hi\")\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testNoApplicableConstructor_invalid() {
        String program =
                CLASS_A +
                "main() {\n" +
                        "a=A(5, 10)\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testFunctionCall_valid() {
        String program =
                "fun(a) {\n" +
                        "return a\n" +
                "}\n" +
                "main() {\n" +
                        "b = fun(5)\n" +
                "}";
        assertValid(program);
    }

    @Test
    public void testAccessSuperclassMembers_valid() {
        String program = CLASS_A + CLASS_B +
                "main() {\n" +
                    "b = B(5, \"hi\")\n" +
                    "num = b.intMember\n" +
                "}";
        assertValid(program);
    }

    @Test
    public void testForInt_valid() {
        assertValid("main() { for(a: 0 -> 10) {} }");
    }

    @Test
    public void testForDouble_invalid() {
        assertInvalid("main() { for(a: 0.3 -> 5.7) {} }");
    }

    @Test
    public void testFunctionWithMultipleParamBindings_valid() {
        String program =
                "func(a, b) {\n" +
                        "return a\n" +
                "}\n" +
                "main() {\n" +
                        "func(5, 5)\n" +
                        "func(5, \"hi\")\n" +
                "}";
        assertValid(program);
    }

    @Test
    public void testFunctionReturnDifferentTypesWithDifferentParameterBindings_invalid() {
        String program =
                "func(a) {\n" +
                        "return a\n" +
                "}\n" +
                "main() {\n" +
                        "x = func(5)\n" +
                        "y = func(\"hi\")\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testFunctionReturnMultipleTypes_invalid() {
        String program =
                "a() {\n" +
                        "if(true) {\n" +
                            "return 0\n" +
                        "} else {\n" +
                            "return false\n" +
                        "}\n" +
                "}\n" +
                "main() {\n" +
                        "x = a()\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testFunctionMissingArgs_invalid() {
        String program =
                "func(a) {\n" +
                        "return 0\n" +
                "}\n" +
                "main() {" +
                        "x = func()\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testFunctionTooManyArgs_invalid() {
        String program =
                "func(a) {\n" +
                        "return 0\n" +
                "}\n" +
                "main() {\n" +
                        "x = func(0, 1)\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    @Disabled
    public void testConstructorWithWrongTypes_invalid() {
        String program = CLASS_A +
                "main() {\n" +
                    "a = A(\"hi\")\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    @Disabled
    public void testSuperConstructorWithWrongTypes_invalid() {
        String program = CLASS_A + CLASS_B +
                "main() {\n" +
                    "b = B(\"hi\", \"hi\")\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    @Disabled
    public void testOverrideWithDifferentReturnType_invalid() {
        String program = CLASS_A +
                "C {\n" +
                    "C() {\n" +
                        "super(0)\n" +
                    "}\n" +
                    "getNumber() {\n" +
                        "return \"zero\"\n" +
                    "}\n" +
                "}\n" +
                "main() {\n" +
                    "c = C()\n" +
                    "num = c.getNumber()\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testAccessPrivateMembers_invalid() {
        String program = CLASS_A +
                "main() {\n" +
                    "a = A(0)\n" +
                    "p = a.privateMember\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testAccessSuperclassPrivateMembers_invalid() {
        String program = CLASS_A + CLASS_B +
                "main() {\n" +
                    "b = B()\n" +
                    "p = b.privateMember\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testClassNamedObject_invalid() {
        String program =
                "Object {}\n" +
                "main() {" +
                        "o = Object()\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testVarNamedInt_invalid() {
        String program =
                "main() {\n" +
                        "int = 0\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testVarNamedDouble_invalid() {
        String program =
                "main() {\n" +
                        "double = 0.0\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testVarNamedBoolean_invalid() {
        String program =
                "main() {\n" +
                        "boolean = 0.0\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testVarNamedChar_invalid() {
        String program =
                "main() {\n" +
                        "char = 0.0\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testVarNamedClass_invalid() {
        String program =
                "main() {\n" +
                        "class = 0.0\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    @Disabled
    public void testEqualsReturnsInt_invalid() {
        String program =
                "Foo {\n" +
                        "equals(other) {\n" +
                            "return 1\n" +
                        "}\n" +
                "}\n" +
                "main() {\n" +
                        "f = Foo()\n" +
                        "a = f.equals(Foo())\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testConstructorReturnsSomething_invalid() {
        String program =
                "Foo {\n" +
                        "Foo() {\n" +
                            "return 1\n" +
                        "}\n" +
                "}\n" +
                "main() {\n" +
                        "a = Foo()\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testCallMethodDefinedInSuper_valid() {
        String program = CLASS_A + CLASS_B +
                "main() {\n" +
                    "b = B(5, \"hi\")\n" +
                    "c = b.getNumber()\n" +
                "}";
        assertValid(program);
    }

    @Test
    public void testMethodWithMultipleBindings_valid() {
        String program =
                "Foo {\n" +
                        "bar(a) {\n" +
                            "return 1\n" +
                        "}\n" +
                "}\n" +
                "main() {\n" +
                        "a = Foo()\n" +
                        "b = a.bar(true)\n" +
                        "c = a.bar(1)\n" +
                "}";
        assertValid(program);
    }

    @Test
    public void testMethodReturnTypeDependsOnParameterBinding_invalid() {
        String program =
                "Foo {" +
                        "bar(a) {\n" +
                            "return a\n" +
                        "}\n" +
                "}\n" +
                "main() {\n" +
                        "a = Foo()\n" +
                        "b = a.bar(0)\n" +
                        "c = a.bar(true)\n" +
                "}";
        assertInvalid(program);
    }

    @Test
    public void testRebindInheritedMethods_valid() {
        String program =
                "Foo {\n" +
                        "bar(a) {\n" +
                            "return 0\n" +
                        "}\n" +
                "}\n" +
                "Baz extends Foo {}\n" +
                "main() {\n" +
                        "a = Baz()\n" +
                        "b = a.bar(0)\n" +
                        "c = a.bar(true)\n" +
                "}";
        assertValid(program);
    }

}