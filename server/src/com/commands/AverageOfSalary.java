package com.commands;

import com.worker.Worker;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class AverageOfSalary {
    /**Выводится среднее значение по зарплате
     * @param workers коллекция с которой ведется работа
     * */
    public String execute(List<Worker> workers){
        if (workers.isEmpty()){
            return ("Не добавлено ни одного сотрудника");
        }
        double result = workers.stream().mapToDouble(Worker::getSalary).sum()/workers.size();
        //result = result/(double)(workers.size());

        return ("Среднее значение по зарплате среди всех сотрудников: " + String.format("%.2f",result));

    }
}
