package com.server_command;

import com.CollectionManager;

import java.util.Scanner;
import java.util.logging.Logger;

public class UserInputHandler {
    /**
     * Второй поток для работы с клиентами и одновременно с консолью сервера (данный поток на консоль)
     * @param manager коллекция
     * @return как я понял, это закрывается поток
     */
    public static Thread getUserInputHandler(CollectionManager manager, Logger logger)
    {
        return new Thread(() -> {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                if (scanner.hasNextLine()) {
                    String serverCommand = scanner.nextLine();
                    switch (serverCommand) {
                        case "exit":
                            logger.info("Работа сервера успешно завершена командой - " + serverCommand);
                            System.exit(1);
                        default:
                            logger.info("Введена неверная команда на сервере - " + serverCommand);
                            break;
                    }
                }
                else {
                    logger.warning("Непредвиденная ошибка при вводе команды на сервере, вероятнее всего нажато сочетание : " +
                            "Ctrl+D \n\n Для корректной работы требуется перезапуск сервера");
                    return;
                }

            }
        });
    }
}
