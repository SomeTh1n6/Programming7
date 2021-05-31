import com.utility.DataBaseConnection;
import sun.plugin2.gluegen.runtime.CPU;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class App {
    public static void main (String[] args) throws InterruptedException, IOException, ClassNotFoundException, SQLException {
        int port = 25534;
        String data = null;
        String log = "H:\\Pgogramming_labs\\2_semestr\\Lab_7\\server\\log.txt";
        //String s = new String("string");
        if (args.length == 2){
            try {
                port = Integer.parseInt(args[0]);
                log = args[1];
            }catch (Exception e){
                System.out.println("Ошибка при считывании данных.\n\nВведите\n" +
                        "   1) Порт\n" +
                        "   2) Путь к файлу , куда писать логи\n");
                System.exit(1);
            }
        }else{
            System.out.println("Ошибка при считывании данных.\n\nВведите\n" +
                    "   1) Порт\n" +
                    "   2) Путь к файлу , куда писать логи\n");
            System.exit(1);
        }

        new DataBaseConnection().connect();
        new Server("localhost",port,log).run();
    }
}