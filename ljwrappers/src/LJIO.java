package wrappers;

import java.util.Scanner;

public class LJIO {
    private static Scanner scn = new Scanner(System.in);

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
        return scn.nextInt();
    }

    public static double readDouble() {
        return scn.nextDouble();
    }

    public static String readChar() {
        return scn.useDelimiter("").next();
    }

    public static String readWord() {
        return scn.useDelimiter("\\s+").next();
    }

    public static String readLine() {
        return scn.nextLine();
    }
}
