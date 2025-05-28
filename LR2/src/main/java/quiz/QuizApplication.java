package quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@SpringBootApplication
public class QuizApplication implements CommandLineRunner {

    private final TestManager manager;
    private final QuestionFactory factory;
    private final Scanner scanner;

    @Autowired
    public QuizApplication(@Qualifier("defaultTestManager") TestManager manager, // defaultTestManager/alternativeTestManager
                          QuestionFactory factory, 
                          Scanner scanner) {
                            
        this.manager = manager;
        this.factory = factory;
        this.scanner = scanner;
    }

    public static void main(String[] args) {
        SpringApplication.run(QuizApplication.class, args);
    }

    @Override
    public void run(String... args) {
        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Добавить вопрос");
            System.out.println("2. Начать тест");
            System.out.println("3. Выйти");
            System.out.print("Ваш выбор: ");

            int choice = getIntInput(1, 3);

            switch (choice) {
                case 1 -> addQuestion();
                case 2 -> manager.startTest();
                case 3 -> {
                    return;
                }
            }
        }
    }

    private void addQuestion() {
        System.out.println("\nТип вопроса:");
        System.out.println("1. Текстовый");
        System.out.println("2. Множественный выбор");
        int type = getIntInput(1, 2);

        System.out.print("Текст вопроса: ");
        String text = scanner.nextLine();

        var question = switch (type) {
            case 1 -> {
                System.out.print("Правильный ответ: ");
                yield factory.createTextQuestion(text, scanner.nextLine());
            }
            default -> {
                var options = new java.util.ArrayList<String>();
                System.out.println("Введите варианты (end для завершения):");
                while (true) {
                    String option = scanner.nextLine();
                    if (option.equalsIgnoreCase("end")) break;
                    options.add(option);
                }
                System.out.print("Правильные ответы (через запятую): ");
                var correct = java.util.Arrays.stream(scanner.nextLine().split(","))
                        .map(String::trim)
                        .collect(java.util.stream.Collectors.toSet());
                yield factory.createMultiChoiceQuestion(text, options, correct);
            }
        };

        System.out.print("Добавить подсказку? (1-Да/2-Нет): ");
        if (getIntInput(1, 2) == 1) {
            System.out.print("Текст подсказки: ");
            question = new HintDecorator(question, scanner.nextLine());
        }

        manager.addQuestion(question);
        System.out.println("Вопрос добавлен!");
    }

    private int getIntInput(int min, int max) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) return input;
            } catch (NumberFormatException ignored) {}
            System.out.printf("Введите число от %d до %d: ", min, max);
        }
    }
}
