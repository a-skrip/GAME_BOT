import bot.EchoBot;
import config.Config;
import config.ConfigReader;
import config.ConfigReaderEnvironment;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class Main {
    public static void main(String[] args) {

        ConfigReader configReader = new ConfigReaderEnvironment();
        Config config = configReader.read();
        TelegramClient client = new OkHttpTelegramClient(config.botApiToken());

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(config.botApiToken(), new EchoBot(client));
            System.out.println("Бот запущен!");


            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
