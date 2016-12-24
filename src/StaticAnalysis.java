import java.util.*;

/**
 * Provides a mechanism for aggregating {@link InvalidProgramException} errors
 * from any AST static analysis pass. Errors found during static analysis are
 * usually not fatal to further analysis, and can be gathered and reported at
 * the end of the static analysis phase of compilation.
 */
public class StaticAnalysis extends DefaultASTVisitor
{
    protected static List<String> errors = new ArrayList<String>();

    /**
     * Report an {@link InvalidProgramException} error. This error is saved
     * for later aggregation. This method is typically called from within
     * a {@code catch} block in a method of a {@link StaticAnalysis} subclass.
     * @param ex
     */
    public static void addError(InvalidProgramException ex)
    {
        errors.add(ex.getMessage());
    }

    /**
     * Report an error message to be saved for later aggregation.
     * @param msg
     */
    public static void addError(String msg)
    {
        errors.add(msg);
    }

    /**
     * Clear all existing errors
     */
    public static void resetErrors()
    {
        errors = new ArrayList<String>();
    }

    /**
     * Retrieve a list of all errors encountered by any {@link StaticAnalysis}
     * subclass thus far.
     * @return List of error strings
     */
    public static List<String> getErrors()
    {
        return errors;
    }

    /**
     * Retrieve a message including all errors encountered by any {@link
     * StaticAnalysis} subclass thus far.
     * @return String of all error messages
     */
    public static String getErrorString()
    {
        StringBuffer str = new StringBuffer();
        for (String s : errors) {
            str.append(s);
            str.append("\n");
        }
        // BEGIN_SOLUTION
        // uncomment for PA4 grading
        /*
         *if (str.length() > 0) {
         *    try {
         *        PrintWriter out = new PrintWriter(new FileWriter("errors.txt", true));
         *        out.print(str.toString());
         *        out.close();
         *    } catch (IOException ex) { }
         *}
         */
        // END_SOLUTION
        return str.toString();
    }
}
