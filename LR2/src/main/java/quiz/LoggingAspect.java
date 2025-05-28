package quiz;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* TestManager.addQuestion(..))")
    public void beforeAdd() {
        System.out.println("Добавление вопроса (через AOP‑прокси)");
    }

    @Before("execution(* TestManager.startTest(..))")
    public void beforeStart() {
        System.out.println("Запуск теста (через AOP‑прокси)");
    }
}
