package com.example.quiz;

import java.io.PrintWriter;

public interface Question {
    void ask(PrintWriter out);
    boolean checkAnswer(String answer);
}
