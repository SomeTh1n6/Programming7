package com.utility;

import java.util.Scanner;

public class Authorization {

    public UserData authorization(){

        UserData userData = null;
        String s = asker("У Вас есть аккаунт? (y|n)");
        if (s.trim().toLowerCase().equals("y")){
            userData = log_in(AuthMode.LOG_IN);
        }
        else{
            s = asker("Хотите создать аккаунт? (y|n)");
            if (s.trim().toLowerCase().equals("y")){
                userData = create(AuthMode.CREATE);
            }else{
                System.exit(0);
            }
        }
        //System.out.println(userData.toString());
        return userData;
    }

    private String asker(String message){
        System.out.println(message);
        Scanner scanner = new Scanner(System.in);
        String answer = null;
        boolean flag = false;
        while (!flag) {
            if (scanner.hasNext()) {
                answer = scanner.nextLine();
                if (!answer.toLowerCase().trim().equals("y") && !answer.toLowerCase().trim().equals("n")){
                    System.out.println("Введите корректный ответ");
                }else flag = true;
            }
        }
        return answer;
    }

    private UserData create(AuthMode authMode){
        String login = "";
        String password = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите логин");
        while (login.equals("") || !login.matches("[0-9A-Za-z]{5,50}")) {
            if (scanner.hasNext()) {
                login = scanner.nextLine();
                if (!login.matches("[0-9A-Za-z]{5,50}"))
                    System.out.println("Введите логин (от 5 до 50 символов - Латиница) ");
            }
        }
        while (password.equals("") || password.toLowerCase().equals(password) || !password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{5,50}")) {
            System.out.println("Введите пароль от 5 до 50 символов, а также \n " +
                    "содержащий хотя бы 1 цифру и заглавную букву (Латиница)");
            if (scanner.hasNext()) {
                password = scanner.nextLine();
            }
        }
        password = new Hash().getMd5(password);

        return new UserData(login,password,authMode);
    }

    private UserData log_in(AuthMode authMode){
        String login = "";
        String password = "";
        Scanner scanner = new Scanner(System.in);
        while (login.equals("") || !login.matches("[0-9A-Za-z]{5,50}" )) {
            System.out.println("Введите логин");
            if (scanner.hasNext()) {
                login = scanner.nextLine();
            }
        }
        System.out.println("Введите пароль");
        int cnt = 0;
        while (password.equals("") || password.toLowerCase().equals(password) || !password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,30}")) {
            if (cnt==1) System.out.println("Введите корректный пароль");
            if (scanner.hasNext()) {
                password = scanner.nextLine();
            }cnt=1;
        }
        password = new Hash().getMd5(password);

        return new UserData(login,password,authMode);
    }

}

