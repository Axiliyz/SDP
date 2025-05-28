package com.example.quiz;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Component
public class QuizServer {
    private final TestManager manager;
    private final QuestionFactory factory;

    public QuizServer(TestManager manager, QuestionFactory factory) {
        this.manager = manager;
        this.factory = factory;
    }

    public void start(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port);
            while (true) {
                Socket client = serverSocket.accept();
                new Thread(() -> handle(client)).start();
            }
        }
    }

    private void handle(Socket sock) {
        try (
                InputStream input = sock.getInputStream();
                OutputStream output = sock.getOutputStream()
        ) {
            writeMenu(output);

            String choice;
            while ((choice = readLine(input)) != null) {
                switch (choice.trim()) {
                    case "1":
                        try {
                            manager.writeLock();
                            try {
                                writeLine(output, "Enter question:");
                                String questionText = readLine(input);
                                writeLine(output, "Enter answer:");
                                String answer = readLine(input);
                                if (questionText != null && answer != null) {
                                    manager.addQuestion(factory.createTextQuestion(questionText, answer));
                                    writeLine(output, "Added");
                                } else {
                                    writeLine(output, "Invalid input for question or answer.");
                                }
                            } finally {
                                manager.writeUnlock();
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            writeLine(output, "Operation was interrupted.");
                        }
                        break;

                    case "2":
                        manager.startTest(
                                wrapReader(input),
                                wrapWriter(output)
                        );
                        break;

                    case "3":
                        writeLine(output, "Goodbye!");
                        return;

                    default:
                        writeLine(output, "Invalid choice");
                }
                writeMenu(output);
            }
        } catch (IOException e) {
            System.err.println("IOException in client handler: " + e.getMessage());
        } finally {
            try {
                if (sock != null && !sock.isClosed()) {
                    sock.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    private void writeMenu(OutputStream output) throws IOException {
        writeLine(output, "\nMenu:");
        writeLine(output, "1. Add Question");
        writeLine(output, "2. Start Test");
        writeLine(output, "3. Exit");
    }

    private String readLine(InputStream input) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int c;
        while ((c = input.read()) != -1) {
            if (c == '\n') break;
            if (c != '\r') buffer.write(c);
        }
        return buffer.size() == 0 && c == -1 ? null : buffer.toString(StandardCharsets.UTF_8);
    }

    private void writeLine(OutputStream output, String line) throws IOException {
        output.write((line + "\n").getBytes(StandardCharsets.UTF_8));
        output.flush();
    }

    private BufferedReader wrapReader(InputStream input) {
        return new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
    }

    private PrintWriter wrapWriter(OutputStream output) {
        return new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true);
    }
}
