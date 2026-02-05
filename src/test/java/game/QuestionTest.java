package game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class QuestionTest {

    @Test
    void whenOptionsContainsSecretMovie_thenQuestionIsCreated() {
        var question = new Question(
                new Movie("a", "b"),
                List.of("a", "c"));

        assertNotEquals(null, question);
    }
}