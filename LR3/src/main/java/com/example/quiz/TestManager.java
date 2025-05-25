package com.example.quiz;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TestManager {
    private final List<Question> questions = new ArrayList<>();
    private final CustomReadWriteLock rw = new CustomReadWriteLock();

    public void addQuestion(Question q) {
        rw.writeLock();
        try { questions.add(q); }
        finally { rw.writeUnlock(); }
    }

    public List<Question> getQuestions() {
        rw.readLock();
        try { return new ArrayList<>(questions); }
        finally { rw.readUnlock(); }
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
