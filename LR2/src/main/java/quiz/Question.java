package quiz;

public interface Question {
    void ask();
    boolean checkAnswer(String answer);
} 