package com;

import com.commands.Authorization;
import com.utility.DataBaseConnection;
import com.utility.Request;
import com.utility.UserData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public class CommandHandler {

    private CollectionManager manager;
    private Request userMessage;
    private UserData userData;

    public String run(CollectionManager manager, Request userMessage) throws FileNotFoundException {
        String response;
        try {
            switch (userMessage.getCommand().trim()){
                case "":
                    response = "";
                    break;
                case "authorization":
                    response = new Authorization(DataBaseConnection.getConnection())
                            .auth(userMessage.getAuth());
                    break;
                case "help":
                    response = manager.help();
                    break;
                case "show":
                    response = manager.show();
                    break;
                case "clear":
                    response = manager.clear();
                    break;
                case "info":
                    response = (manager.toString());
                    break;
                case "average_of_salary":
                    response =  (manager.average_of_salary());
                    break;
                case "head":
                    response = (manager.head());
                    break;
                case "remove_head":
                    response = (manager.remove_head(userMessage.getAuth().getLogin()));
                    break;
                case "update":
                    response = manager.update(userMessage.getWorker(), Integer.parseInt(userMessage.getArgument()),userMessage.getAuth().getLogin());
                    break;
                case "add":
                    response = (manager.add(userMessage.getWorker(),userMessage.getAuth().getLogin()));
                    break;
                case "history":
                    response = "История команд:\n";
                    break;
                case "remove_by_id":
                    response = manager.removeById(Integer.parseInt(userMessage.getArgument()),userMessage.getAuth().getLogin());
                    break;
                case "filter_starts_with_name":
                    response = manager.filterStartsWithName(userMessage.getArgument());
                    break;
                case "print_ascending":
                    response = manager.printAscending(userMessage.getArgument());
                    break;
                case "execute_script":
                    response = manager.executeScript(manager, userMessage.getScript(), userMessage.getAuth().getLogin());
                    System.out.println(response);
                    break;
                default:
                    response = "Неопознанная команда, введите \"help\" для справки по командам";
                    break;
            }
        }catch (ArrayIndexOutOfBoundsException | NullPointerException | IOException | SQLException e){
            response = "Отсутствует аргумент";
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
}
