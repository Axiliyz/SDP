package com.example.quiz.model;

import java.io.PrintWriter;

public interface Question {
    void ask(PrintWriter out);
    boolean checkAnswer(String answer);
}
