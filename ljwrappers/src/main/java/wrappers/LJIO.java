package test.wrappers;

import java.util.Scanner;

public class LJIO {
    private static Scanner inputScn = new Scanner(System.in);
    private static Scanner lineScn = null;

    public static void print(Object o) {
        System.out.print(o);
    }

    public static void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    public static void println(Object o) {
        System.out.println(o);
    }

    public static int readInt() {
        buffer();
        return lineScn.nextInt();
    }

    public static double readDouble() {
        buffer();
        return lineScn.nextDouble();
    }

    public static String readChar() {
        buffer();
        return lineScn.useDelimiter("").next();
    }

    public static String readWord() {
        buffer();
        return lineScn.useDelimiter("\\s+").next();
    }

    public static String readLine() {
        buffer();
        return lineScn.nextLine();
    }

    public static void buffer() {
        if (lineScn == null || !lineScn.hasNext()) {
            lineScn = new Scanner(inputScn.nextLine());
        }
    }
}
