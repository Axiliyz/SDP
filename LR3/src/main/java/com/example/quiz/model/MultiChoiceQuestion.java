package com.example.quiz.model;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class MultiChoiceQuestion implements Question {
    private final String question;
    private final List<String> options;
    private final Set<String> correctAnswers;

    public MultiChoiceQuestion(String question,
                               List<String> options,
                               Set<String> correctAnswers) {
        this.question = question;
        this.options = options;
        this.correctAnswers = correctAnswers.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    @Override
    public void ask(PrintWriter out) {
        out.println(question);
        for (int i = 0; i < options.size(); i++) {
            out.printf("%d. %s%n", i + 1, options.get(i));
        }
    }

    @Override
    public boolean checkAnswer(String answer) {
        Set<String> user = Arrays.stream(answer.split("[,\\s]+"))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        return user.equals(correctAnswers);
    }
}
