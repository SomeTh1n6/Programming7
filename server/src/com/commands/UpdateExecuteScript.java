package com.commands;

import com.utility.DataBaseConnection;
import com.worker.Worker;
import com.worker.properties.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class UpdateExecuteScript {
    IsNumber checkNumber = new IsNumber();
    public String  execute(List<Worker> workers, int id, Scanner scanner, String sender) throws SQLException {

        Worker workerDelete = null;
        LocalDateTime localDateTime = null;
        String line;
        boolean flag = false;
        String name = null;
        Position position = null;
        Status status = null;
        int x = 0;
        float y = 0;
        double salary = 0;

        // Нахождение работника , которого надо удалить
        for (Worker worker1: workers) {
            if (worker1.getId().equals(id)) {
                localDateTime = worker1.getCreationDate();
                workerDelete = worker1;
                flag = true;
                break;
            }
        }
        if (!flag){
            return "С заданным id не найдено работника";
        }
        workers.remove(workerDelete);
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
        PreparedStatement st = DataBaseConnection.getConnection().prepareStatement
                ("SELECT owner_ AS owner_ FROM workers WHERE id = '"+id+"'");
        ResultSet rs = st.executeQuery();
        rs.next();
        if (rs.getString("owner_").equals(sender)){
            try {
                PreparedStatement prst = DataBaseConnection.getConnection().prepareStatement("UPDATE workers " +
                        "SET name = '"+worker.getName()+"'," +
                        "x = '"+worker.getX()+"'," +
                        "y = '"+worker.getY()+"'," +
                        "salary = '"+worker.getSalary()+"'," +
                        "position  = '"+worker.getPosition()+"'," +
                        "status  = '"+worker.getStatus()+"'," +
                        "organizationannualturnover  = '"+worker.getOrganizationAnnualTurnover()+"'," +
                        "organizationtype  = '"+worker.getOrganizationType()+"'," +
                        "organizationadress  = '"+worker.getOrganizationAdress()+"'" +
                        " WHERE id = '"+id+"'"
                );
                prst.executeUpdate();
                prst.close();
                workers.remove(workerDelete); // удаление работника
                worker.setId(id);
                worker.setCreationDate(localDateTime);
                workers.remove(workerDelete); // удаление работника
                workers.add(worker);
                workers.sort(Worker::compareTo);
                return "Элемент успешно изменен";
            }catch (SQLException e){
                System.out.println(e.getMessage());
                return "Непредвиденная ошибка";
            }
        }else{
            return "Отказано в доступе";
        }

        //return new AddExecuteScript().execute(workers,scanner);
    }


}
