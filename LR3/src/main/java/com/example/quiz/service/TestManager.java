package com.example.quiz.service;

import com.example.quiz.model.Question;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class TestManager {
    private final List<Question> questions = new ArrayList<>();
    private final ReadWriteLock rw = new ReentrantReadWriteLock();

    public void addQuestion(Question q) {
        rw.writeLock().lock();
        try { questions.add(q); }
        finally { rw.writeLock().unlock(); }
    }

    public List<Question> getQuestions() {
        rw.readLock().lock();
        try { return new ArrayList<>(questions); }
        finally { rw.readLock().unlock(); }
    }

    public void startTest(BufferedReader in, PrintWriter out) throws IOException {
        if (questions.isEmpty()) {
            out.println("Test is empty");
            return;
        }
        int score = 0;
        for (Question q : questions) {
            q.ask(out);
            String ans = in.readLine();
            if (ans == null) return;
            if (q.checkAnswer(ans)) {
                out.println("Right!");
                score++;
            } else {
                out.println("Wrong!");
            }
        }
        out.printf("Result: %d/%d%n", score, questions.size());
    }
}
