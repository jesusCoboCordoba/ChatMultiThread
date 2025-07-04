package com.serversAndClients;

import java.io.PrintWriter;

public class ServerUtils {
    private static final String CLEAR_CHARACTER = "\033[H\033[2J";
    private static final int DEFAULT_WAITING_TIME = 1000;

    public static void clearConsole(PrintWriter out) {
    out.print(CLEAR_CHARACTER);
    }

    public static void welcomeMessage(PrintWriter out) throws InterruptedException {
        out.println("Please enter \"exit\" when you are done");
    }

}
