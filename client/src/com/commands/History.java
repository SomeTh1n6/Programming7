package com.commands;

import java.util.Queue;

public class History {

    /**
     * Вывод истории последних 6-ти команд
     * @param history очередь в которой находятся команды
     * @return строку - история
     */
    public String execute(Queue<String> history){
        if (history.size() == 0)
            return "История команд пуста!";
        StringBuffer sb = new StringBuffer();
        history.forEach(p -> sb.append(p).append(" ").append("\n"));
        return sb.toString();
    }
}
