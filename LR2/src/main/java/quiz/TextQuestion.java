package quiz;

public class TextQuestion implements Question {
    private final String question;
    private final String correctAnswer;

    private TextQuestion(Builder b) {
        this.question = b.question;
        this.correctAnswer = b.correctAnswer;
    }

    public static class Builder {
        private final String question;
        private final String correctAnswer;

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