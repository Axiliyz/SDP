package com.example.quiz.server;

import com.example.quiz.factory.QuestionFactory;
import com.example.quiz.service.TestManager;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class QuizServer {
    private final TestManager manager;
    private final QuestionFactory factory;
    private final Semaphore sem = new Semaphore(10);
    private final ReadWriteLock rw = new ReentrantReadWriteLock();

    public QuizServer(TestManager manager, QuestionFactory factory) {
        this.manager = manager;
        this.factory = factory;
    }

    public void start(int port) throws IOException {
        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Сервер на порту " + port);
            while (true) {
                Socket sock = ss.accept();
                new Thread(() -> handle(sock)).start();
            }
        }
    }

    private void handle(Socket sock) {
        try (BufferedReader in = new BufferedReader(
                 new InputStreamReader(sock.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(
                 new OutputStreamWriter(sock.getOutputStream(), StandardCharsets.UTF_8), true)) {

            if (!sem.tryAcquire()) {
                out.println("Busy");
                sock.close();
                return;
            }

            out.println("1. ADD");
            out.println("2. TEST");
            out.println("3. EXIT");

            String choice;
            while ((choice = in.readLine()) != null) {
                if (choice.equals("1")) {
                    out.println("Question:");
                    String text = in.readLine();
                    out.println("Right answer:");
                    String ans = in.readLine();
                    rw.writeLock().lock();
                    try {
                        manager.addQuestion(factory.createTextQuestion(text, ans));
                    } finally {
                        rw.writeLock().unlock();
                    }
                    out.println("Added!");
                } else if (choice.equals("2")) {
                    rw.readLock().lock();
                    try {
                        manager.startTest(in, out);
                    } finally {
                        rw.readLock().unlock();
                    }
                } else if (choice.equals("3")) {
                    break;
                }
                out.println("1. ADD");
                out.println("2. TEST");
                out.println("3. EXIT");
            }

            sem.release();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
