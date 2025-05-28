package quiz;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service("alternativeTestManager")
public class AlternativeTestManager implements TestManager {

    private final List<Question> questions = new ArrayList<>();
    private final Scanner scanner;

    public AlternativeTestManager(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void addQuestion(Question q) {
        questions.add(q);
    }

    @Override
    public void startTest() {
        if (questions.isEmpty()) {
            System.out.println("Тест пуст. Добавьте вопросы перед запуском.");
            return;
        }

        int score = 0;
        int totalQuestions = questions.size();
        
        System.out.println("=== Начало альтернативного теста ===");
        for (Question q : questions) {
            q.ask();
            System.out.print("Введите ваш ответ: ");
            String answer = scanner.nextLine().trim();
            
            if (q.checkAnswer(answer)) {
                System.out.println("Правильно! +1 балл");
                score++;
            } else {
                System.out.println("Неправильно!");
            }
            System.out.println("------------------------");
        }
        
        double percentage = (double) score / totalQuestions * 100;
        System.out.printf("=== Результаты теста ===%n");
        System.out.printf("Правильных ответов: %d из %d%n", score, totalQuestions);
        System.out.printf("Процент выполнения: %.1f%%%n", percentage);
    }

    @Override
    public List<Question> getQuestions() {
        return questions;
    }
} 