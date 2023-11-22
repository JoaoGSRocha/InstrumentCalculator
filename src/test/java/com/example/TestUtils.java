package com.example;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TestUtils {
    public static String captureSystemOut(Runnable runnable) {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            runnable.run();
        } finally {
            System.setOut(originalOut);
        }

        return outContent.toString();
    }
}
