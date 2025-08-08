package com.kenny.conversor;

public final class Console {
    private static final String RESET = "\u001B[0m";
    private static final String RED   = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String BLUE  = "\u001B[34m";
    private static final String YELLOW= "\u001B[33m";

    private Console() {}

    public static void info(String msg)  { System.out.println(BLUE + msg + RESET); }
    public static void ok(String msg)    { System.out.println(GREEN + msg + RESET); }
    public static void warn(String msg)  { System.out.println(YELLOW + msg + RESET); }
    public static void err(String msg)   { System.out.println(RED + msg + RESET); }
}
