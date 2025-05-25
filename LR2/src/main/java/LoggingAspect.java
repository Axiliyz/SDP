package com.example.quiz;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.quiz.TestManager.addQuestion(..))")
    public void beforeAdd() {
        System.out.println("Добавление вопроса (через AOP‑прокси)");
    }

    @Before("execution(* com.example.quiz.TestManager.startTest(..))")
    public void beforeStart() {
        System.out.println("Запуск теста (через AOP‑прокси)");
    }
}
