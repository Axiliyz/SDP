package com.example.quiz;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class TestManager {

    private final List<Question> questions = new ArrayList<>();
    private final Scanner scanner;

    public TestManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public void addQuestion(Question q) {
        questions.add(q);
    }

    public void startTest() {
        if (questions.isEmpty()) {
            System.out.println("Тест пуст. Добавьте вопросы перед запуском.");
            return;
        }

        int score = 0;
        for (Question q : questions) {
            q.ask();
            System.out.print("Ваш ответ: ");
            if (q.checkAnswer(scanner.nextLine())) {
                System.out.println("Верно!");
                score++;
            } else {
                System.out.println("Неверно!");
            }
        }
        System.out.printf("Результат: %d/%d%n", score, questions.size());
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
