package com.example.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartTestCommand implements TestCommand {

    private final TestManager manager;

    @Autowired
    public StartTestCommand(TestManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        manager.startTest();
    }
}
