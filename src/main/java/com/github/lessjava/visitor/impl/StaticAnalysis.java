package com.github.lessjava.visitor.impl;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.github.lessjava.exceptions.InvalidProgramException;
import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.visitor.LJDefaultASTVisitor;

/**
 * Provides a mechanism for aggregating {@link InvalidProgramException} errors
 * from any AST static analysis pass. Errors found during static analysis are
 * usually not fatal to further analysis, and can be gathered and reported at
 * the end of the static analysis phase of compilation.
 */
public class StaticAnalysis extends LJDefaultASTVisitor {
    protected static Set<StaticAnalysisError> errors = new TreeSet<>();
    public static boolean collectErrors = true;

    /**
     * Report an {@link InvalidProgramException} error. This error is saved for
     * later aggregation. This method is typically called from within a
     * {@code catch} block in a method of a {@link StaticAnalysis} subclass.
     * 
     * @param ex
     */
    public static void addError(InvalidProgramException ex) {
        errors.add(new StaticAnalysisError(ex.getMessage()));
    }

    /**
     * Report an error message to be saved for later aggregation.
     * 
     * @param msg
     */
    public static void addError(ASTNode node, String msg) {
        if(collectErrors) {
            errors.add(new StaticAnalysisError(node.lineNumber, msg));
        }
    }

    /**
     * Clear all existing errors
     */
    public static void resetErrors() {
        errors = new TreeSet<>();
    }

    /**
     * Retrieve a list of all errors encountered by any {@link StaticAnalysis}
     * subclass thus far.
     * 
     * @return List of error strings
     */
    public static List<String> getErrors() {
        return errors.stream().map(StaticAnalysisError::toString).collect(Collectors.toList());
    }

    /**
     * Retrieve a message including all errors encountered by any
     * {@link StaticAnalysis} subclass thus far.
     * 
     * @return String of all error messages
     */
    public static String getErrorString() {
        StringBuffer str = new StringBuffer();
        for (StaticAnalysisError error : errors) {
            str.append(error.toString());
            str.append("\n");
        }

        return str.toString();
    }

    /**
     * A class to represent static analysis errors in a sortable fashion.
     */
    private static class StaticAnalysisError implements Comparable<StaticAnalysisError> {
        private int lineNumber;
        private String errorString;

        /**
         * Generate a static analysis error assiciated with a line number
         *
         * @param lineNumber the line the error was found in
         * @param errorString a description of the error
         */
        public StaticAnalysisError(int lineNumber, String errorString) {
            this.lineNumber = lineNumber;
            this.errorString = errorString;
        }

        /**
         * Generate a static analysis error that is not associated with a line number
         *
         * @param errorString a description of the error
         */
        public StaticAnalysisError(String errorString) {
            this.lineNumber = -1;
            this.errorString = errorString;
        }

        /**
         * Generate a String representation of the error
         *
         * @return a string representing the error
         */
        @Override
        public String toString() {
            if(this.lineNumber == -1) {
                return errorString;
            }
            return String.format("Line %d: %s", lineNumber, errorString);
        }

        /**
         * Sort errors by line number first, then alphabetically if the line number is the same.
         *
         * @param other the error to compare to
         * @return an integer reflecting the ordering of the two errors
         */
        @Override
        public int compareTo(StaticAnalysisError other) {
            if(this.lineNumber == other.lineNumber) {
                return this.errorString.compareTo(other.errorString);
            }
            return this.lineNumber - other.lineNumber;
        }
    }
}
