import java.util.*;

/**
 * Single Decaf symbol. Might represent a scalar variable, an array, or a
 * formal function parameter.
 */
public class Symbol
{
    /**
     * Memory access locations.
     *
     * May be one of the following:
     *
     * <ul>
     * <li> STATIC_VAR  - static variable w/ global symbol </li>
     * <li> STATIC_FUNC - function w/ global symbol </li>
     * <li> STACK_PARAM - stack-dynamic w/ positive offset from base pointer </li>
     * <li> STACK_LOCAL - stack-dynamic w/ negative offset from stack pointer </li>
     * </ul>
     */
    public enum MemLoc {
        UNKNOWN,
        STATIC_VAR,
        STATIC_FUNC,
        STACK_PARAM,
        STACK_LOCAL
    };

    /**
     * Name in source code
     */
    public String name;

    /**
     * Data type
     */
    public ASTNode.DataType type;

    /**
     * Data types of formal parameters (for function symbols, empty for others)
     */
    public List<ASTNode.DataType> paramTypes;

    /**
     * Flag indicating this symbol represents an array
     */
    public boolean isArray;

    /**
     * Length of array (for array symbols; should be 1 for others)
     */
    public int length;

    /**
     * Size of individual elements (in bytes)
     */
    public int elementSize;

    /**
     * Size of overall structure (in bytes); for scalar variables,
     * elementSize == totalSize
     */
    public int totalSize;

    /**
     * Memory location information
     */
    public MemLoc location;

    /**
     * Memory offset (if needed)
     */
    public int offset;

    /**
     * Create a new scalar symbol
     * @param name Name in source code
     * @param type Data type ({@link ASTNode.DataType})
     */
    public Symbol(String name, ASTNode.DataType type)
    {
        this(name, type, false, 1);
    }

    /**
     * Create a new array symbol
     * @param name Name in source code
     * @param isArray Flag indicating whether the symbol represents an array or scalar
     * @param type Data type ({@link ASTNode.DataType})
     * @param length Array length (should be 1 for scalar)
     */
    public Symbol(String name, ASTNode.DataType type, boolean isArray, int length)
    {
        this.name = name;
        this.type = type;
        this.paramTypes = new ArrayList<ASTNode.DataType>();
        this.isArray = isArray;
        this.length = length;
        this.elementSize = 4;       // may depend on 'type' if we ever add data
                                    // types with differing sizes
        this.totalSize = this.elementSize * this.length;
        this.location = MemLoc.UNKNOWN;
        this.offset = 0;
    }

    /**
     * Create a new function symbol
     * @param name Name in source code
     * @param returnType Function return type ({@link ASTNode.DataType})
     * @param paramTypes List of formal parameter data types
     */
    public Symbol(String name, ASTNode.DataType returnType,
            List<ASTNode.DataType> paramTypes)
    {
        this.name = name;
        this.type = returnType;
        this.paramTypes = paramTypes;
        this.length = 1;
        this.elementSize = 8;
        this.totalSize = 8;
        this.location = MemLoc.STATIC_FUNC;
        this.offset = 0;
    }

    /**
     * Simplified string representation
     */
    @Override
    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append(name);
        if (isArray) {
            str.append("[" + length + "]");
        }
        str.append(" : ");
        str.append(ASTNode.typeToString(type));
        switch (location) {
        case STATIC_VAR:
            str.append(" (0x" + Long.toHexString(offset) + ")");
            break;
        case STATIC_FUNC:
            boolean comma = false;
            str.append(" (");
            for (ASTNode.DataType t : paramTypes) {
                if (comma) {
                    str.append(",");
                }
                str.append(ASTNode.typeToString(t));
                comma = true;
            }
            str.append(")");
            break;
        case STACK_PARAM:
            str.append(" (BP+0x" + Long.toHexString(offset) + ")");
            break;
        case STACK_LOCAL:
            str.append(" (BP-0x" + Long.toHexString(offset) + ")");
            break;
        default:
            break;
        }
        //str.append(" {total=" + Integer.toString(totalSize) + "}");
        return str.toString();
    }
}
