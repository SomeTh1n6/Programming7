package com.commands;

import java.util.*;
import java.util.stream.Collectors;

public class Help {
    public String execute(HashMap<String,String> manual) {
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_PURPLE = "\u001B[35m";
        final String ANSI_RESET = "\u001B[0m";
        return manual
                .entrySet()
                .stream()
                .map(key -> ANSI_GREEN + key.getKey() + " - " +
                        ANSI_PURPLE + key.getValue() + ANSI_RESET + "\n")
                .collect(Collectors.joining());
    }
}
