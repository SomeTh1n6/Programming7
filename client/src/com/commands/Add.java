package com.commands;

import com.worker.*;
import com.worker.properties.*;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Add {
    private Integer id;
    private IsNumber checkNumber;
    private Status status;
    private String line;
    private Scanner scanner;
    private LocalDateTime localDateTime;

    /**
     * Подготовка к отправке информации о новом сотруднике на сервер
     * @return готовый к отправке Worker*/
    public Worker execute() {
        checkNumber = new IsNumber();
        String name = "NoName";
        int x = 0;
        float y = 0;
        double salary = 1;
        Position position = null;
        scanner = new Scanner(System.in);
        System.out.println("Введите имя");
        if (scanner.hasNextLine()) {
            line = scanner.nextLine();
            String[] finalUserCommand = line.trim().split(" ");
            while (line.trim().equals("") || finalUserCommand.length > 1) {
                System.out.println("Имя - непустое значение и одно слово");
                if (scanner.hasNextLine()){
                    line = scanner.nextLine();
                    finalUserCommand = line.trim().split(" ");
                }
                else {
                    System.out.println("Непредвиденная ошибка");
                    System.exit(1);
                }
            }
            name = line;
        } else {
            System.out.println("Неверное имя");
            System.exit(1);
        }

        System.out.println("Введите координату x (целое число):");
        if (scanner.hasNextLine()) {
            line = scanner.nextLine();
            // проверка на число int
            while (!checkNumber.isNumberInteger(line)) {
                System.out.println("Введенная строка не соответствует требованиям" +
                        " (целое числовое значение большее -2147483649 и меньшее 2147483647");
                if (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                }
                else {
                    System.out.println("Неверное значение координаты");
                    System.exit(1);
                }
            }
            x = Integer.parseInt(line);
        } else {
            System.out.println("Неверное имя");
            System.exit(1);
        }


        System.out.println("Введите координату y (с точкой):");
        if (scanner.hasNextLine()) {
            line = scanner.nextLine();
            // проверка на число float
            while (!checkNumber.isNumberFloat(line)) {
                System.out.println("Введенная строка не соответствует требованиям" +
                        " (число от 1.4*10^-45 до 3.4028235*10^38(число с точкой))");
                if (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                } else {
                    System.out.println("Неверное значение координаты");
                    System.exit(1);
                }
            }
            y = Float.parseFloat(line);
        } else {
            System.out.println("Неверное значение координаты");
            System.exit(1);
        }

        Coordinates coordinates = new Coordinates(x, y);


        System.out.println("Введите назначенную зарплату");
        if (scanner.hasNextLine()) {
            line = scanner.nextLine();
            while (!checkNumber.isNumberDouble(line) || Double.parseDouble(line) <= 0 || line.length()  >= 16) {
                System.out.println("Введенная строка не соответствует требованиям" +
                        " (число от 4.9*10^−324 до 1.7976931348623157^308(число с точкой))\nИли вводится неположительное число для зарплаты" +
                        "\nИли слишком большое число");
                if (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                } else {
                    System.out.println("Неверное значение зарплаты");
                    System.exit(1);
                }

            }
            salary = Double.parseDouble(line);
            System.out.println(salary);
        } else {
            System.out.println("Неверное значение зарплаты");
            System.exit(1);
        }
        System.out.println("Выберите позицию , которую занимает сотрудник в кампании (введите число):\n" +
                " 1 - Инженер\n" + " 2 - Начальник отдела\n 3 - Ведущий разработчик\n 4 - Пекарь\n Любое другое число - нет позиции");
        if (scanner.hasNextLine()) {
            line = scanner.nextLine();
            switch (line.trim()) {
                case "1":
                    position = Position.ENGINEER;
                    break;
                case "2":
                    position = Position.HEAD_OF_DIVISION;
                    break;
                case "3":
                    position = Position.LEAD_DEVELOPER;
                    break;
                case "4":
                    position = Position.BAKER;
                    break;
                default:
                    position = Position.UNDEFINED;
            }
            /*if (position != null){
                System.out.println(position.getPosition());
            }*/
        } else {
            System.out.println("Неверное значение");
            System.exit(1);
        }

        System.out.println("Выберите его нынешний статус:\n" +
                " 1 - Уволен\n" + " 2 - Нанят\n 3 - Рекомендуется для продвижения по службе" +
                "\n 4 - Основной\n 5 - На испытательном сроке\n Любое другое число - нет статуса");
        if (scanner.hasNextLine()) {
            line = scanner.nextLine();
            switch (line.trim()) {
                case "1":
                    status = Status.FIRED;
                    break;
                case "2":
                    status = Status.HIRED;
                    break;
                case "3":
                    status = Status.RECOMMENDED_FOR_PROMOTION;
                    break;
                case "4":
                    status = Status.REGULAR;
                    break;
                case "5":
                    status = Status.PROBATION;
                    break;
                default:
                    status = Status.UNDEFINED;
                    break;
            }
        } else {
            System.out.println("Неверное значение");
            System.exit(1);
        }
        Organization organization;
        OrganizationType organizationType = null;
        Address address = new Address("");
        double annualTurnover = 0;
        System.out.println("Введите информацию об организации\n\nВведите годовой оборот (ввод числа с точкой)");
        if (scanner.hasNextLine()) {
            line = scanner.nextLine();
            while (!checkNumber.isNumberDouble(line) || Double.parseDouble(line) <= 0) {
                System.out.println("Введенная строка не соответствует требованиям" +
                        " (число от 4.9*10^−324 до 1.7976931348623157^308 (число с точкой))\nГодовой оборот - число больше 0");
                if (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                } else {
                    System.out.println("Неверное значение");
                    System.exit(1);
                }
            }
            annualTurnover = Double.parseDouble(line);
        } else {
            System.out.println("Неверное значение");
            System.exit(1);
        }

        System.out.println("Введите число, соответствующее типу организации:\n 1 - Коммерческая\n" +
                " 2 - Частная компания с ограниченной ответственностью\n" +
                " 3 - Открытое акционерное общество\n Любое другое - нет типа");
        if (scanner.hasNextLine()) {
            line = scanner.nextLine();
            switch (line.trim()) {
                case "1":
                    organizationType = OrganizationType.COMMERCIAL;
                    break;
                case "2":
                    organizationType = OrganizationType.PRIVATE_LIMITED_COMPANY;
                    break;
                case "3":
                    organizationType = OrganizationType.OPEN_JOINT_STOCK_COMPANY;
                    break;
                default:
                    organizationType = OrganizationType.UNDEFINED;
                    break;
            }
        }
        else {
            System.out.println("Неверное значение");
            System.exit(1);
        }

        System.out.println("Введите адрес организации:");

        if (scanner.hasNextLine()) {
            line = scanner.nextLine();
            while (line.trim().equals("")){
                if (scanner.hasNextLine()) {
                    System.out.println("Введите корректные данные");
                    line = scanner.nextLine();
                }
                else {
                    System.out.println("Неверное значение");
                    System.exit(1);
                }
            }
            address.setStreet(line);
        }
        else{
            System.out.println("Неверное значение");
            System.exit(1);
        }
        organization = new Organization(annualTurnover,organizationType,address);
        return new Worker(id,name,coordinates,localDateTime,salary,position,status,organization);
    }
}
