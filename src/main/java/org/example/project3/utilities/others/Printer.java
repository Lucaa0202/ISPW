package org.example.project3.utilities.others;

@SuppressWarnings("squid:S106") // Zittisce l'avviso sull'uso di System.out/System.err
public class Printer {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    private Printer(){}

    public static void print(String s){System.out.print(s);}

    public static void println(String s){System.out.println(s);}

    public static void errorPrint(String s){
        System.out.println(ANSI_RED+s+ANSI_RESET);
    }

}