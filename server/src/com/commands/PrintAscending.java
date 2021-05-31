package com.commands;

import com.worker.Worker;

import java.util.*;

public class PrintAscending{

    /** Сортирует коллекцию по заданному параметру
     * @param workers коллекция, с которой ведется работа
     * @param condition параметр по которому сортируем
     * */
    public String execute(List<Worker> workers, String condition) {
        String information = null;
        if (!workers.isEmpty()) {
            switch (condition) {
                case "id":
                    workers.sort(Worker::compareTo);
                    information = "Коллекция отсортирована по id\n";
                    Show show = new Show(workers, information);
                    return show.execute();
                case "name":
                    workers.sort(nameComparator);
                    information = "Коллекция отсортирована по имени , в алфавитном порядке";
                    show = new Show(workers, information);
                    return show.execute();
                case "salary":
                    workers.sort(salaryComparator);
                    information = "Коллекция отсортирована по заработной плате";
                    show = new Show(workers, information);
                    return show.execute();
                case "creation_date":
                    workers.sort(dateComparator);
                    information = "Коллекция отсортирована по дате создания";
                    show = new Show(workers, information);
                    return show.execute();
                case "organization_annual_turnover":
                    workers.sort(annualTurnoverComparator);
                    information = "Коллекция отсортирована по годовому обороту организаций";
                    show = new Show(workers, information);
                    return show.execute();
                case "organization_adress":
                    workers.sort(annualTurnoverComparator);
                    information = "Коллекция отсортирована по адресу организаций";
                    show = new Show(workers, information);
                    return show.execute();
                default:
                    return "Неопознанный аргумент. Доступные параметры для сортировки коллекции:\nid - сортировка по id" +
                            "\nname - сортировка по имени\nsalary - сортировка по зарплате\ncreation_date - сортировка по дате создания\n" +
                            "organization_annual_turnover - сортировка по годовому обороту организации\norganization_adress - сортировка по адресу организации";
            }
        } else
            return "Коллекция пуста";
    }


    public static Comparator<Worker> nameComparator = new Comparator<Worker>() {
        @Override
        public int compare(Worker w1, Worker w2) {
            return w1.getName().compareTo(w2.getName());
        }
    };

    public static Comparator<Worker> salaryComparator = new Comparator<Worker>() {
        @Override
        public int compare(Worker w1, Worker w2) {
            return (int) (w1.getSalary() - w2.getSalary());
        }
    };

    public static Comparator<Worker> dateComparator = new Comparator<Worker>() {
        @Override
        public int compare(Worker w1, Worker w2) {
            String creationDate1 = w1.getCreationDate().toString();
            String creationDate2 = w2.getCreationDate().toString();
            return creationDate1.compareTo(creationDate2);
        }
    };

    public static Comparator<Worker> annualTurnoverComparator = new Comparator<Worker>() {
        @Override
        public int compare(Worker w1, Worker w2) {
            return (int) (w1.getOrganizationAnnualTurnover() - w2.getOrganizationAnnualTurnover());
        }
    };

    public static Comparator<Worker> adressComparator = new Comparator<Worker>() {
        @Override
        public int compare(Worker w1, Worker w2) {
            return w1.getOrganizationAdress().compareTo(w2.getOrganizationAdress());
        }
    };
}
