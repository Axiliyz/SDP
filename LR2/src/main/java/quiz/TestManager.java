package quiz;

import java.util.List;

public interface TestManager {
    void addQuestion(Question q);
    void startTest();
    List<Question> getQuestions();
}