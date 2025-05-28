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
    private final CustomReadWriteLock rwLock = new CustomReadWriteLock();

    public void writeLock() throws InterruptedException {
        rwLock.writeLock();
    }

    public void writeUnlock() {
        rwLock.writeUnlock();
    }

    public void readLock() throws InterruptedException {
        rwLock.readLock();
    }

    public void readUnlock() throws InterruptedException {
        rwLock.readUnlock();
    }

    public void addQuestion(Question q) {
        questions.add(q);
    }

    public List<Question> getQuestions() {
        try {
            readLock();
            try {
                return new ArrayList<>(questions);
            } finally {
                readUnlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Get questions operation was interrupted.");
            return new ArrayList<>();
        }
    }

    public void startTest(BufferedReader in, PrintWriter out) throws IOException {
        try {
            readLock();
            try {
                if (questions.isEmpty()) {
                    out.println("Test is empty");
                    return;
                }
                int score = 0;
                List<Question> currentQuestions = new ArrayList<>(questions);

                for (Question q : currentQuestions) {
                    q.ask(out);
                    String ans = in.readLine();
                    if (ans == null) {
                        out.println("Input stream ended unexpectedly.");
                        return;
                    }
                    if (q.checkAnswer(ans)) {
                        out.println("Right!");
                        score++;
                    } else {
                        out.println("Wrong!");
                    }
                }
                out.printf("Result: %d/%d%n", score, currentQuestions.size());
            } finally {
                readUnlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Start test operation was interrupted.");
            if (out != null) {
                out.println("The test was interrupted due to an internal error. Please try again.");
            }
        }
    }
}