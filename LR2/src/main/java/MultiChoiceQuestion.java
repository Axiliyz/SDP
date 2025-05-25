package com.example.quiz;

import java.util.*;
import java.util.stream.Collectors;

public class MultiChoiceQuestion implements Question {
    private final String question;
    private final List<String> options;
    private final Set<String> correct;

    public MultiChoiceQuestion(String question, List<String> options, Set<String> correctAnswers) {
        this.question = question;
        this.options = options;
        this.correct = correctAnswers.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    @Override
    public void ask() {
        System.out.println(question);
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, options.get(i));
        }
    }

    @Override
    public boolean checkAnswer(String answer) {
        var user = Arrays.stream(answer.split("[,\s]+"))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        return user.equals(correct);
    }
}
