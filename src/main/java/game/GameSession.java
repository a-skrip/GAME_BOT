package game;

import java.util.List;
import java.util.Optional;

public class GameSession {

    private final List<Movie> movies;
    private final String playerId;

    private int score;
    private int currentMovieIndex;
    private Question lastQuestion;

    public GameSession(List<Movie> movies, String playerId) {
        this.movies = movies;
        this.playerId = playerId;
    }

    public Optional<Question> getNextQuestion() {
        if (currentMovieIndex >= movies.size()) {
            lastQuestion = null;
            return Optional.empty();
        }

        Movie secretMovie = movies.get(currentMovieIndex);
        //TODO сгенерировать варианты ответа
        Question question = new Question(secretMovie, List.of(secretMovie.title()));
        currentMovieIndex++;
        lastQuestion = question;
        return Optional.of(question);
    }

    public Optional<Question> getLastQuestion() {
        return Optional.ofNullable(lastQuestion);
    }

    public boolean isGameOver() {
        return currentMovieIndex >= movies.size();
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score++;
    }

    public String getPlayerId() {
        return playerId;
    }

}
