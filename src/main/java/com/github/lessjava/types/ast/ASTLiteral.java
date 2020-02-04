package com.github.lessjava.types.ast;

import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.HMType.BaseDataType;
import com.github.lessjava.types.inference.impl.HMTypeBase;

/**
 * Less-Java literal value.
 *
 * <p>
 * Here are the Less-Java data types and their corresponding Java data types:
 *
 * <table border="1">
 * <tr>
 * <th>{@link ASTNode.DataType}</th>
 * <th>Java type</th>
 * </tr>
 * <tr>
 * <td>{@code INT}</td>
 * <td>{@code Integer}</td>
 * <tr>
 * <td>{@code BOOL}</td>
 * <td>{@code Boolean}</td>
 * <tr>
 * <td>{@code STR}</td>
 * <td>{@code String}</td>
 * <tr>
 * <td>{@code VOID}</td>
 * <td>{@code null}</td> <caption></caption>
 * </table>
 */
public class ASTLiteral extends ASTExpression {
    /**
     * Remove escape codes from string literals and replace them with the
     * corresponding special character (quotes, newlines, or tabs) Removes carriage
     * returns ("\r") entirely.
     *
     * @param str
     *            String to manipulate
     * @return String with escape codes replaced by special characters
     */
    public static String removeEscapeCodes(String str) {
        return str.replaceAll("\\\\\"", "\"").replaceAll("\\\\n", "\n").replaceAll("\\\\t", "\t").replaceAll("\r", "");
    }

    /**
     * Remove quotes, newlines, and tabs from string literals and replace them with
     * their escape codes.
     *
     * @param str
     *            String to manipulate
     * @return String with special characters replaced by escape codes
     */
    public static String addEscapeCodes(String str) {
        return str.replaceAll("\"", "\\\\\"").replaceAll("\n", "\\\\n").replaceAll("\t", "\\\\t").replaceAll("\r", "");
    }

    public Object value;
    public HMType hmType;

    public ASTLiteral(BaseDataType type, Object value) {
        super.type = new HMTypeBase(type);
        this.value = value;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        visitor.postVisit(this);
    }

    @Override
    public String toString() {
        String s = String.format("%%s.valueOf(%s)", this.value.toString());

        if (this.value instanceof String) {
            s = "\"" + addEscapeCodes(value.toString()) + "\"";
        } else if (this.value instanceof Boolean) {
            s = String.format(s, "Boolean");
        } else if (this.value instanceof Integer) {
            s = String.format(s, "Integer");
        } else if (this.value instanceof Double) {
            s = String.format(s, "Double");
        }

        return s;
    }
}
