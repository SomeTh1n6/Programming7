package com.commands;

import com.worker.Worker;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Show {
    private List<Worker> workers;
    private String information = "";
    public Show(List<Worker> workers){
        this.workers = workers;
    }

    public Show(List<Worker> workers, String information){
        this.workers = workers;
        this.information = information;
    }

    /** Выводит на печать все элементы коллекции
     */
    public String execute(){
        /*StringBuffer sb = new StringBuffer();
        if (!information.equals("")){
            sb.append(information+"\n");
        }*/

        if (!workers.isEmpty()){
            //workers.forEach(worker -> sb.append(worker.toString()));
            return workers.stream().map(Worker::toString).collect(Collectors.joining());
        }
        else{
            return ("Коллекция пуста");
        }
        //return sb.toString();
    }
}
