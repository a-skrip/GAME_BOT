package game;

import java.util.List;

public record Question(
        Movie secretMovie,
        List<String> answerOptions
) {

    public Question {
        if (!answerOptions.contains(secretMovie.title())) {
            throw new IllegalArgumentException(
                    "Варианты ответов %s не содержат правильный вариант %s".formatted(answerOptions, secretMovie)
            );
        }
    }

    public boolean isRightAnswer(String guess) {
        return guess.equalsIgnoreCase(secretMovie.title());
    }
}
