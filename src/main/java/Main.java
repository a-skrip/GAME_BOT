import bot.GameBot;
import config.Config;
import config.ConfigReader;
import config.ConfigReaderEnvironment;
import game.GameManager;
import game.Movie;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import parser.MovieParser;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        ConfigReader configReader = new ConfigReaderEnvironment();
        Config config = configReader.read();
        TelegramClient client = new OkHttpTelegramClient(config.botApiToken());
        GameManager gameManager = new GameManager();

        List<Movie> movieList = new MovieParser().parseMovies("/movies.csv");
        System.out.println(movieList);
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(
                    config.botApiToken(),
                    new GameBot(client, gameManager, movieList));
            System.out.println("Бот запущен!");


            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
