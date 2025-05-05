package com.example.quiz.model;

public interface Question {
    void ask();
    boolean checkAnswer(String answer);
}
