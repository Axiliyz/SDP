import java.util.*;
import java.util.stream.Collectors;

// 1. Strategy (Стратегия): Интерфейс вопроса
interface Question {
    void ask();
    boolean checkAnswer(String answer);
}

// 2. Builder (Строитель): Текстовый вопрос с использованием Builder
class TextQuestion implements Question {
    private String question;
    private String correctAnswer;

    private TextQuestion(Builder builder) {
        this.question = builder.question;
        this.correctAnswer = builder.correctAnswer;
    }

    public static class Builder {
        private String question;
        private String correctAnswer;

        public Builder(String question, String correctAnswer) {
            this.question = question;
            this.correctAnswer = correctAnswer;
        }

        public TextQuestion build() {
            return new TextQuestion(this);
        }
    }

    @Override
    public void ask() {
        System.out.println(question);
    }

    @Override
    public boolean checkAnswer(String answer) {
        return correctAnswer.equalsIgnoreCase(answer);
    }
}

// 1. Strategy (Стратегия): Вопрос с выбором
class MultiChoiceQuestion implements Question {
    private String question;
    private List<String> options;
    private Set<String> correctAnswers;

    public MultiChoiceQuestion(String question, List<String> options, Set<String> correctAnswers) {
        this.question = question;
        this.options = options;
        this.correctAnswers = correctAnswers.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    @Override
    public void ask() {
        System.out.println(question);
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
    }

    @Override
    public boolean checkAnswer(String answer) {
        Set<String> userAnswers = Arrays.stream(answer.split("[,\\s]+"))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        return userAnswers.equals(correctAnswers);
    }
}

// 4. Decorator (Декоратор): Добавляет подсказку к вопросу
class HintDecorator implements Question {
    private Question decoratedQuestion;
    private String hint;

    public HintDecorator(Question decoratedQuestion, String hint) {
        this.decoratedQuestion = decoratedQuestion;
        this.hint = hint;
    }

    @Override
    public void ask() {
        decoratedQuestion.ask();
        System.out.println("Подсказка: " + hint);
    }

    @Override
    public boolean checkAnswer(String answer) {
        return decoratedQuestion.checkAnswer(answer);
    }
}

// 5. Factory Method (Фабричный метод): Фабрика вопросов
class QuestionFactory {
    public Question createTextQuestion(String question, String correctAnswer) {
        return new TextQuestion.Builder(question, correctAnswer).build();
    }

    public Question createMultiChoiceQuestion(String question, List<String> options, Set<String> correctAnswers) {
        return new MultiChoiceQuestion(question, options, correctAnswers);
    }
}

// 6. Composite (Компоновщик): Тест как составной вопрос
class Test implements Question {
    private List<Question> questions = new ArrayList<>();

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    // Метод ask() реализует логику проведения теста с использованием итератора
    @Override
    public void ask() {
        int score = 0;
        Scanner scanner = new Scanner(System.in);
        Iterator<Question> iterator = new QuestionIterator(questions); // 8. Iterator
        while (iterator.hasNext()) {
            Question q = iterator.next();
            q.ask();
            System.out.print("Ваш ответ: ");
            String userAnswer = scanner.nextLine();
            if (q.checkAnswer(userAnswer)) {
                System.out.println("Верно!");
                score++;
            } else {
                System.out.println("Неверно!");
            }
        }
        System.out.printf("Результат: %d/%d%n", score, questions.size());
    }

    // Для композита проверка единственного ответа не реализована
    @Override
    public boolean checkAnswer(String answer) {
        throw new UnsupportedOperationException("Composite test не поддерживает проверку одного ответа.");
    }
}

// 8. Iterator (Итератор): Перебор вопросов
class QuestionIterator implements Iterator<Question> {
    private List<Question> questions;
    private int position = 0;

    public QuestionIterator(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public boolean hasNext() {
        return position < questions.size();
    }

    @Override
    public Question next() {
        if (!hasNext()) throw new NoSuchElementException();
        return questions.get(position++);
    }
}

// Интерфейс для менеджера тестов (для использования Proxy и Command)
interface ITestManager {
    void addQuestion(Question question);
    void startTest();
}

// 7. Singleton (Одиночка): Менеджер тестов
class TestManager implements ITestManager {
    private static TestManager instance;
    private Test test = new Test();

    private TestManager() {}

    public static TestManager getInstance() {
        if (instance == null) {
            instance = new TestManager();
        }
        return instance;
    }

    @Override
    public void addQuestion(Question question) {
        test.addQuestion(question);
    }

    @Override
    public void startTest() {
        if (test.getQuestions().isEmpty()) {
            System.out.println("Тест пуст. Добавьте вопросы перед запуском.");
            return;
        }
        test.ask();
    }
}

// 7. Proxy (Прокси): Менеджер тестов с дополнительным логированием
class TestManagerProxy implements ITestManager {
    private ITestManager realManager = TestManager.getInstance();

    @Override
    public void addQuestion(Question question) {
        System.out.println("Добавление вопроса через прокси");
        realManager.addQuestion(question);
    }

    @Override
    public void startTest() {
        System.out.println("Запуск теста через прокси");
        realManager.startTest();
    }
}

// 9. Command (Команда): Интерфейс команды и команда запуска теста
interface TestCommand {
    void execute();
}

class StartTestCommand implements TestCommand {
    private ITestManager manager;

    public StartTestCommand(ITestManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        manager.startTest();
    }
}

public class Main {
    public static void main(String[] args) {
        QuestionFactory factory = new QuestionFactory();
        Scanner scanner = new Scanner(System.in);
        ITestManager manager = new TestManagerProxy(); // Используем прокси для менеджера тестов

        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Добавить вопрос");
            System.out.println("2. Начать тест");
            System.out.println("3. Выйти");
            System.out.print("Ваш выбор: ");

            int choice = getIntInput(scanner, 1, 3);

            switch (choice) {
                case 1 -> addQuestion(scanner, factory, manager);
                case 2 -> {
                    // Используем Command для запуска теста
                    TestCommand startTest = new StartTestCommand(manager);
                    startTest.execute();
                }
                case 3 -> {
                    scanner.close();
                    return;
                }
            }
        }
    }

    private static void addQuestion(Scanner scanner, QuestionFactory factory, ITestManager manager) {
        System.out.println("\nТип вопроса:");
        System.out.println("1. Текстовый");
        System.out.println("2. Множественный выбор");
        int type = getIntInput(scanner, 1, 2);

        System.out.print("Текст вопроса: ");
        String text = scanner.nextLine();

        Question question;
        if (type == 1) {
            System.out.print("Правильный ответ: ");
            String correctAnswer = scanner.nextLine();
            question = factory.createTextQuestion(text, correctAnswer);
        } else {
            List<String> options = new ArrayList<>();
            System.out.println("Введите варианты (end для завершения):");
            while (true) {
                String option = scanner.nextLine();
                if (option.equalsIgnoreCase("end")) break;
                options.add(option);
            }
            System.out.print("Правильные ответы (через запятую): ");
            Set<String> correct = Arrays.stream(scanner.nextLine().split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet());
            question = factory.createMultiChoiceQuestion(text, options, correct);
        }

        System.out.print("Добавить подсказку? (1-Да/2-Нет): ");
        if (getIntInput(scanner, 1, 2) == 1) {
            System.out.print("Текст подсказки: ");
            String hint = scanner.nextLine();
            question = new HintDecorator(question, hint);
        }

        manager.addQuestion(question);
        System.out.println("Вопрос добавлен!");
    }

    private static int getIntInput(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) return input;
                System.out.printf("Введите число от %d до %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.print("Ошибка! Введите число: ");
            }
        }
    }
}
