package bot;

import game.GameManager;
import game.GameSession;
import game.Movie;
import game.Question;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameBot implements LongPollingSingleThreadUpdateConsumer {

    public static final int COLUMNS_COUNT = 2;
    private final TelegramClient client;
    private final GameManager gameManager;
    private final List<Movie> movies;
//    private final CommandDispatcher commandDispatcher;
//    private final Users users;

    public GameBot(TelegramClient client, GameManager gameManager, List<Movie> movies) {
//        this.users = new Users();
        this.client = client;
//        this.commandDispatcher = new CommandDispatcher(client);
        this.gameManager = gameManager;
        this.movies = movies;
    }


    @Override
    public void consume(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            System.out.println("В сообщении пользователя нет текста!");
            return;
        }

        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        String userMessageText = message.getText();

        if (userMessageText.equalsIgnoreCase("/start")) {
            startNewGame(chatId);
        } else {
            handleUserGuess(chatId, userMessageText);
        }
    }

    private void handleUserGuess(String chatId, String userMessageText) {
        GameSession gameSession = gameManager.findGameSession(chatId);

        if (gameSession == null) {
            sendMessage(chatId, "Игра не начата! Для старта выполните команду /start");
            return;
        }
        Optional<Question> optionalLastQuestion = gameSession.getLastQuestion();
        if(optionalLastQuestion.isEmpty()) {
            sendMessage(chatId, "Вопросы закончились! Ваш счёт: %d".formatted(gameSession.getScore()));
            gameManager.endGame(chatId);
            return;
        }
        Question lastQuestion = optionalLastQuestion.get();

        if(lastQuestion.isRightAnswer(userMessageText)) {
            gameSession.incrementScore();
            sendMessage(chatId, "Правильно! Счёт: %d".formatted(gameSession.getScore()));
        } else {
            if(gameSession.isGameOver()) {
                var keyboardRowList = new ArrayList<KeyboardRow>();
                var row1 = new KeyboardRow();
                row1.add("/start");
                keyboardRowList.add(row1);
                var replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRowList);
                replyKeyboardMarkup.setResizeKeyboard(true);
                replyKeyboardMarkup.setInputFieldPlaceholder("Выберите команду:");

                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text("Начать Заново - нажмите Start")
                        .replyMarkup(replyKeyboardMarkup)
                        .build();
                try {
                    client.execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
                sendMessage(chatId, "Ответ не верный! Правильный ответ %s".formatted(lastQuestion.secretMovie().title()));
        }

        if(gameSession.isGameOver()) {
            sendMessage(chatId, "Игра окончена! Итоговый счёт: %d".formatted(gameSession.getScore()));
            var keyboardRowList = new ArrayList<KeyboardRow>();
            var row1 = new KeyboardRow();
            row1.add("/start");
            keyboardRowList.add(row1);
            var replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRowList);
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setInputFieldPlaceholder("Выберите команду:");

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text("Начать Заново - нажмите Start")
                    .replyMarkup(replyKeyboardMarkup)
                    .build();
            try {
                client.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            sendNextMovie(chatId, gameSession);
        }
    }


    private void startNewGame(String chatId) {
        GameSession gameSession = gameManager.startNewGame(movies, chatId);
        sendNextMovie(chatId, gameSession);
    }

    private void sendNextMovie(String chatId, GameSession gameSession) {
        gameSession.getNextQuestion()
                .ifPresent(
                        question -> sendPhoto(
                                chatId,
                                question.secretMovie().pathToImage(),
                                "Угадай фильм",
                                new KeyBoardBuilder().build(question.answerOptions(), COLUMNS_COUNT)));
    }

    private void sendMessage(String chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        try {
            client.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void sendPhoto(String chatId, String imagePath, String caption, ReplyKeyboard keyboard) {
        try (InputStream inputStream = getClass().getResourceAsStream(imagePath)) {
            if (inputStream == null) {
                System.out.println("Изображение %s не найдено".formatted(imagePath));
                return;
            }
            InputFile inputFile = new InputFile(inputStream, imagePath);

            SendPhoto.SendPhotoBuilder photo = SendPhoto.builder()
                    .chatId(chatId)
                    .photo(inputFile)
                    .caption(caption);

            if (keyboard != null) {
                photo.replyMarkup(keyboard);
            }

            client.execute(photo.build());

        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        }

    }


}

