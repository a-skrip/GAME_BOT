package config;

public class ConfigReaderEnvironment implements ConfigReader {
    @Override
    public Config read() {
        String token = System.getenv("BOT_TOKEN");
        return new Config(token);
    }
}
