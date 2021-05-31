package com.commands;

import com.CollectionManager;
import com.utility.Request;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class ExecuteScript {
    /** Выполнение скрипта из файла. Команды считываются так же как бы они считывались в интерактивном режиме
     * @exception FileNotFoundException - Файл не обнаружен
     * @exception StackOverflowError - Рекурсия
     * */
    public String execute(CollectionManager manager, String script, String login) throws IOException {
        String[] commands = script.split("\n");
        String response = null;
        StringBuilder sb = new StringBuilder();
            for (int i = 0; i < commands.length; i++) {
                try {
                    String[] userMessage = commands[i].split(" ", 2);
                    switch (userMessage[0].trim()){
                        case "":
                            response = "";
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
                        case "execute_script":
                            response = "Нельзя вызвать скрипт внутри скрипта";
                            break;
                        case "average_of_salary":
                            response =  (manager.average_of_salary());
                            break;
                        case "head":
                            response = (manager.head());
                            break;
                        case "remove_head":
                            response = (manager.remove_head(login));
                            break;
                        case "update":
                        case "add":
                            File tmp = createTmpFile();
                            PrintWriter writer = new PrintWriter(new File(tmp.getAbsolutePath()));
                            for (int j = 1; j <= 9; j++ ){
                                // запись всей строки
                                String text = commands[i+j];
                                writer.write(text+"\n");
                            }
                            writer.flush();
                            writer.close();
                            i+=9;
                            FileInputStream fis = new FileInputStream(tmp);
                            Scanner scanner = new Scanner(fis);
                            System.out.println(Arrays.toString(userMessage));
                            if (userMessage[0].trim().equals("add")){
                                response = manager.addES(scanner,login);
                            }
                            else{
                                response = manager.updateES(Integer.parseInt(userMessage[1]), scanner,login);
                            }
                            fis.close();
                            if (tmp.delete())
                                System.out.println(tmp.getAbsolutePath() + " успешно удален");
                            else
                                System.out.println("Возникли проблемы с удалением временного файла - "+ tmp.getAbsolutePath());
                            break;
                        case "history":
                            response = "";
                            break;
                        case "remove_by_id":
                            response = manager.removeById(Integer.parseInt(userMessage[1]),login);
                        case "filter_starts_with_name":
                            response = manager.filterStartsWithName(userMessage[1]);
                            break;
                        case "print_ascending":
                            response = manager.printAscending(userMessage[1]);
                            break;
                        default:
                            response = "Неопознанная команда, введите \"help\" для справки по командам";
                            break;
                    }
                }catch (ArrayIndexOutOfBoundsException | SQLException e){
                    response = "Отсутствует аргумент";
                }
                sb.append(response).append("\n");
            }
            sb.append("\n");
            return sb.toString();
        }

        static File createTmpFile(){
            File tmpDir1 = new File("tmpDir").getAbsoluteFile();
            String uniqueID = UUID.randomUUID().toString();
            //tmpDir.mkdir();
            File tmp1 = new File(tmpDir1 + "\\tmp_" + uniqueID + ".txt");
            try {
                if (tmp1.createNewFile()){
                    System.out.println("Временный файл "+ tmpDir1 + "\\tmp_" + uniqueID + ".txt - " +
                            "успешно создан!");
                }
                else{
                    System.out.println("Возникли ошибки при создании файла - " + tmpDir1 + "\\tmp_" + uniqueID + ".txt");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tmp1;
        }
}
