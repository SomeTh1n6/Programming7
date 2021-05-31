package com.utility;

import java.sql.*;
import java.util.Scanner;

public class DataBaseConnection {

    private static Connection connection = null;

    /***
     * Подключение к БД
     */
    public void connect(){
        try {
            Class.forName("org.postgresql.Driver");
            String ps = readPassword();
            // connection = DriverManager.getConnection("jdbc:postgresql://localhost:5430/studs",
            //        "s311736", ps);
            connection = DriverManager.getConnection("jdbc:postgresql://pg:5432/studs",
                         "s311736", ps);
        } catch (ClassNotFoundException e) {
            System.out.println("Postgresql Driver не может быть найден");
            System.exit(1);
        } catch (SQLException e) {
            System.out.println("Неверный пароль");
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    /***
     * Считываем введенный пароль, и возвращаем его
     * @return пароль от БД
     */
    private String readPassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите пароль: ");
        return scanner.nextLine();
    }

    /**
     * геттер
     * @return connection
     */
    public static Connection getConnection() {
        return connection;
    }
}
