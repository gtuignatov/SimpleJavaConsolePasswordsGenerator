package com.beginwithsoftware.simplepsw;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public class SimpleConsolePasswordsGenerator {

    private final static String symbolsToUse = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    private final static int symbolsToUseLength = symbolsToUse.length();
    private final static char [] charsArray = fillCharsArrayFromString(symbolsToUse);
    private final static int passwordLength = 16;
    private final static int passwordsCount = 15000;
    private final static int partialPasswordsCount = passwordsCount / 10;
    private final static String outputFile = "output.csv";

    private static char [] fillCharsArrayFromString(String symbols) {
        char [] charsMethodArray = new char[symbols.length()];
        for (int i = 0; i < symbols.length(); i++) {
            charsMethodArray[i] = symbols.charAt(i);
        }
        return charsMethodArray;
    }

    private static char randomChar(char [] chars, int charsArrayLength) {
        SecureRandom secureRandom = new SecureRandom();
        return chars[secureRandom.nextInt(charsArrayLength)];
    }

    private static LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }

    // https://stackoverflow.com/questions/25747499/java-8-difference-between-two-localdatetime-in-multiple-units
    private static void printTimeDifference(LocalDateTime startTime, LocalDateTime stopTime) {
        LocalDateTime tempDateTime = LocalDateTime.from( startTime );

        long years = tempDateTime.until( stopTime, ChronoUnit.YEARS );
        tempDateTime = tempDateTime.plusYears( years );

        long months = tempDateTime.until( stopTime, ChronoUnit.MONTHS );
        tempDateTime = tempDateTime.plusMonths( months );

        long days = tempDateTime.until( stopTime, ChronoUnit.DAYS );
        tempDateTime = tempDateTime.plusDays( days );

        long hours = tempDateTime.until( stopTime, ChronoUnit.HOURS );
        tempDateTime = tempDateTime.plusHours( hours );

        long minutes = tempDateTime.until( stopTime, ChronoUnit.MINUTES );
        tempDateTime = tempDateTime.plusMinutes( minutes );

        long seconds = tempDateTime.until( stopTime, ChronoUnit.SECONDS );

        System.out.println("Time Spend: " +
                days + " days " +
                hours + " hours " +
                minutes + " minutes " +
                seconds + " seconds.");
    }

    public static void exportStringSetToFile(Set<String> stringSet) {
        try (PrintWriter printWriter = new PrintWriter(outputFile)) {
            for(String psw: stringSet) {
                printWriter.println(psw);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        System.out.println("Simple Console Passwords Generator Start");
        System.out.println("Passwords Length: " + passwordLength);
        System.out.println("Passwords Count: " + passwordsCount);
        LocalDateTime startTime = getLocalDateTime();

        StringBuilder builder = new StringBuilder("");
        Set<String> passwordSet = new HashSet<>();
        int passwordSetSize = passwordSet.size();

        while (passwordSetSize < passwordsCount) {
            builder.setLength(0);
            for (int j = 0; j < passwordLength; j++) {
                builder.append(randomChar(charsArray, symbolsToUseLength));
            }
            passwordSet.add(builder.toString());
            passwordSetSize = passwordSet.size();
            if (passwordSetSize % partialPasswordsCount == 0) {
                System.out.println("Now Generated: " + passwordSetSize);
            }
        }

        System.out.println("Total Passwords Generated: " + passwordSet.size());
        LocalDateTime stopTime = getLocalDateTime();
        printTimeDifference(startTime, stopTime);

        // Export Results
        exportStringSetToFile(passwordSet);
        System.out.println("All Passwords Are Exported to " + outputFile + " File");
    }
}
