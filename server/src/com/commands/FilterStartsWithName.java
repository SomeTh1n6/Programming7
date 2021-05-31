package com.commands;

import com.worker.Worker;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FilterStartsWithName {
    /** Выводит на печать только те элементы коллекци в которых поле name начинается с заданной подстроки
     * Игнорирует верхний и нижний регистр
     * Если ничего не найдено, выводится сообщение об этом
     * @param name параметр по которому проводится поиск
     * @param workers коллекция, с которой ведется работа
     */
    public String execute(List<Worker> workers, String name){

        Pattern pattern = Pattern.compile("^" + name,Pattern.CASE_INSENSITIVE); // почему то не игнорируется регистр у русского алфавита. С правильным регистром поиск работает

        StringBuffer sb = new StringBuffer();
        workers.forEach(worker ->
        {
            if (pattern.matcher(worker.getName()).find()) {
                sb.append(worker.toString());
            }
        });
        if (sb.toString().equals("")){
            return "Ничего не найдено";
        }
        return sb.toString();
    }
}
