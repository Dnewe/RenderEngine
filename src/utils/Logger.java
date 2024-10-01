package utils;

import java.util.Date;

import config.Config;

public class Logger {


    public static void info(String message) {
        System.out.println("INFO - " + getTime() + " : " + message);
    }

    public static void warning(String message) {
        if (Config.DETAILED_LOGS) {
            System.out.println("WARNING - " + getTime() + " : " + message);
        }
    }

    public static void error(Exception exception) {
        StackTraceElement[] stackTrace = exception.getStackTrace();
        StackTraceElement element = stackTrace[0];
        String className = element.getClassName();
        int lineNumber = element.getLineNumber();
        System.out.println("ERROR - " + getTime() + " : " + className + " (line " + lineNumber + ")" + " - " + exception.getMessage());

        if (Config.DETAILED_LOGS) {
            System.out.println("  errror details :");
            for (StackTraceElement el : stackTrace) {
                System.out.println("   -" + el.getClassName() + " (line " + el.getLineNumber() + ")");
            }
        }
    }



    private static String getTime() {
        Date d = new Date();
        String h = Long.toString((d.getTime() / 1000 / 60 / 60) % 24);
        String m = Long.toString((d.getTime() / 1000 / 60) % 60);
        String s = Long.toString((d.getTime() / 1000) % 60);
        h = "0".repeat(2-h.length()) + h;
        m = "0".repeat(2-m.length()) + m;
        s = "0".repeat(2-s.length()) + s;
        return h + ":" + m + ":" + s ;
    } 
}
