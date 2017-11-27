package com.github.lessjava.types.ast;

import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.HMType.BaseDataType;
import com.github.lessjava.types.inference.impl.HMTypeBase;

/**
 * Decaf literal value.
 *
 * <p>
 * Here are the Decaf data types and their corresponding Java data types:
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
        if (this.value instanceof String) {
            return "\"" + addEscapeCodes(value.toString()) + "\"";
        } else if (this.value instanceof Boolean) {
            return String.format("Boolean.valueOf(%s)", this.value);
        } else if (this.value instanceof Integer) {
            return String.format("Integer.valueOf(%s)", this.value);
        } else if (this.value instanceof Double) {
            return String.format("Double.valueOf(%s)", this.value);
        } else {
            return this.value.toString();
        }
    }
}
