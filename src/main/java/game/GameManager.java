package game;

import java.util.*;

public class GameManager {

    private final Map<String, GameSession> gameSessions = new HashMap<>();

    public GameSession startNewGame(List<Movie> movies, String playerId) {
        List<Movie> sessionMovieList = new ArrayList<>(movies);
        Collections.shuffle(sessionMovieList);
        GameSession gameSession = new GameSession(sessionMovieList, playerId);
        gameSessions.put(playerId, gameSession);
        return gameSession;
    }

    public GameSession findGameSession(String playerId) {
        return gameSessions.get(playerId);
    }

    public void endGame(String playerId) {
        gameSessions.remove(playerId);
    }

}
