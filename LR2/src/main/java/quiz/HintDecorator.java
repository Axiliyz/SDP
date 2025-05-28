package quiz;

public class HintDecorator implements Question {
    private final Question inner;
    private final String hint;

    public HintDecorator(Question inner, String hint) {
        this.inner = inner;
        this.hint = hint;
    }

    @Override
    public void ask() {
        inner.ask();
        System.out.println("Подсказка: " + hint);
    }

    @Override
    public boolean checkAnswer(String answer) {
        return inner.checkAnswer(answer);
    }
} 