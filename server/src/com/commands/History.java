package com.commands;

import java.util.Queue;
import java.util.stream.Collectors;

public class History {
    public String execute(Queue<String> history){
        if (history.size() == 0)
            return "История команд пуста!";
        return history.stream().map(p -> p+"\n").collect(Collectors.joining());
    }
}
