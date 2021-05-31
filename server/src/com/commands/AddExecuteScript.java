package com.commands;

import com.utility.DataBaseConnection;
import com.worker.Worker;
import com.worker.properties.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

public class AddExecuteScript {

    IsNumber checkNumber = new IsNumber();
    RandomLocalDateTime randomLocalDateTime = new RandomLocalDateTime();
    Status status = null;

    /** Добавление нового сотрудника в коллекцию
     * @param workers коллекция, куда мы добавляем работника
     * */
    public String execute(List<Worker> workers, Scanner scanner, String sender) {

        String line = null;
        String name = "NoName";
        Integer id = null;
        int x = 0;
        float y = 0;
        double salary = 1;
        Position position = null;

        if (scanner.hasNextLine()) {
            line = scanner.nextLine();
            String[] finalUserCommand = line.trim().split(" ");
            while (line.trim().equals("") || finalUserCommand.length > 1) {
                if(scanner.hasNextLine())
                    line = scanner.nextLine();
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

        if (scanner.hasNextLine()) {
            line = scanner.nextLine();
            // проверка на число int
            while (!checkNumber.isNumberInteger(line)) {
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

        if (scanner.hasNextLine()) {
            line = scanner.nextLine();
            // проверка на число float
            while (!checkNumber.isNumberFloat(line)) {
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

        LocalDateTime localDateTime = randomLocalDateTime.randomLocalDateTime();

        if (scanner.hasNextLine()) {
            line = scanner.nextLine();
            while (!checkNumber.isNumberDouble(line) || Double.parseDouble(line) <= 0) {
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
                    break;
            }
        } else {
            System.out.println("Неверное значение");
            System.exit(1);
        }

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
        if (scanner.hasNextLine()) {
            line = scanner.nextLine();
            while (!checkNumber.isNumberDouble(line) || Double.parseDouble(line) <= 0) {
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

        Worker worker = new Worker(id,name,coordinates,localDateTime,salary,position,status,organization);
        worker.setCreationDate(new RandomLocalDateTime().randomLocalDateTime());

        try {
            PreparedStatement prst = DataBaseConnection.getConnection().prepareStatement("INSERT INTO workers " +
                    "(owner_, name, x, y, creation_date," +
                    " salary, position, status, organizationannualturnover, organizationtype, organizationadress) " +
                    "VALUES (?, ?, ?, ?, ?, ?, CAST (? AS positions), CAST (? AS status), ?, CAST (? AS organizationtype), ?);");

            prst.setString(1,sender);
            prst.setString(2, worker.getName());
            prst.setInt(3,worker.getX());
            prst.setFloat(4,worker.getY());
            prst.setTimestamp(5, Timestamp.valueOf(worker.getCreationDate()));
            prst.setDouble(6, worker.getSalary());
            prst.setString(7, worker.getPosition());
            prst.setString(8, worker.getStatus());
            prst.setDouble(9, worker.getOrganizationAnnualTurnover());
            prst.setString(10, worker.getOrganizationType());
            prst.setString(11,worker.getOrganizationAdress());
            prst.executeUpdate();
            prst.close();
            PreparedStatement st = DataBaseConnection.getConnection().prepareStatement
                    ("SELECT MAX(id) AS id " + "FROM workers WHERE owner_=\'"+ sender +"\';");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int id1 = rs.getInt("id");
                worker.setId(id1);
                workers.add(worker);
                System.out.println(worker.toString());
                workers.sort(Worker::compareTo);
            }

        }catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        workers.add(worker); // добавляем в коллекцию только что созданный id
        workers.sort(Worker::compareTo);
        return "Успешно!";
    }
}
