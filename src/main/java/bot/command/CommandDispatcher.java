package bot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.HashMap;
import java.util.Map;

public class CommandDispatcher {

    private final Map<String, Command> commandMap = new HashMap<>();

    public CommandDispatcher(TelegramClient client) {
        commandMap.put("/start", new StartCommand(client));
        commandMap.put("/myid", new UserIdCommand(client));
    }

    public void dispatch(String commandText, Update update) {

        String[] split = commandText.split("\\s");
        String lowerCase = split[0].toLowerCase();
        Command command = commandMap.get(lowerCase);
        System.out.printf("Обработка команды %s%n", commandText);
        if (command != null) {
            command.execute(update);
        } else {
            System.out.printf("Команда %s не найдена%n", commandText);
        }


    }
}
