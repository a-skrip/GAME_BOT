package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GameSession {
    public static final int ANSWER_OPTIONS_AMOUNT = 4;
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
        Question question = new Question(secretMovie, createAnswerOptions(secretMovie));
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


    private List<String> createAnswerOptions(Movie secretMovie) {
        List<Movie> moviesForAnswer = new ArrayList<>(movies);
        moviesForAnswer.remove(secretMovie);
        Collections.shuffle(moviesForAnswer);

        List<Movie> subMoviesForAnswer = new ArrayList<>(moviesForAnswer.subList(0,
                Math.min(ANSWER_OPTIONS_AMOUNT-1, moviesForAnswer.size())));
        subMoviesForAnswer.add(secretMovie);
        Collections.shuffle(subMoviesForAnswer);
        return subMoviesForAnswer.stream()
                .map(Movie::title)
                .toList();
    }
}
