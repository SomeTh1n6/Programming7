package com.commands;

import com.CollectionManager;
import com.worker.Worker;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

public class Clear {

    /** Очищает коллекцию
     * @param workers коллекция, оторая очищается
     */
    public void execute(List<Worker> workers) {
        workers.clear();
    }
}

