import com.utility.Authorization;
import com.utility.UserData;

import java.io.*;

public class App {

    public static void main (String[] args) throws IOException, ClassNotFoundException {
        int port = 25534;
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception exception) {
                System.out.println("Не получается спарсить порт\nИспользуется стандартный");
            }
        }
        new Client("localhost",port).run();
    }
}

