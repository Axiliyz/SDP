package com.example.quiz.command;

import com.example.quiz.service.TestManager;

public class StartTestCommand implements TestCommand {
    private final TestManager manager;

    public StartTestCommand(TestManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        manager.startTest();
    }
}
