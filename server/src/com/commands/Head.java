package com.commands;

import com.worker.Worker;

import java.util.LinkedList;
import java.util.List;

public class Head {
    /** Выводит на печать первый элемент в коллекции
     * @param workers коллекция, с которой ведется работа
     */
    public String execute(List<Worker> workers) {
        return workers.stream().findFirst()
                .map(worker -> "Первый элемент - " + worker).orElse("Ошибка. Коллекция пуста.");
    }
}
