package bot;

import bot.command.CommandDispatcher;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import users.Users;

public class EchoBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient client;
    private final CommandDispatcher commandDispatcher;
    private final Users users;

    public EchoBot(TelegramClient client) {
        this.users = new Users();
        this.client = client;
        this.commandDispatcher = new CommandDispatcher(client);
    }


    @Override
    public void consume(Update update) {

        if (!update.hasMessage() || !update.getMessage().hasText()) {
            System.out.println("В сообщении пользователя нет текста!");
            return;
        }

        String messageText = update.getMessage().getText().stripLeading();

        if (messageText.startsWith("/")) {
            commandDispatcher.dispatch(messageText, update);
            Long chatId = update.getMessage().getChatId();
            if(users.getUsersMap().containsKey(chatId)) {
                System.out.println(messageText + " from User = " + users.getUsersMap().get(chatId));
            } else {
                System.out.println(messageText + " new user " + chatId);
            }

        }

    }
}
