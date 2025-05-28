package quiz;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
@Scope("prototype")
public class QuestionIterator implements Iterator<Question> {

    private final List<Question> questions;
    private int index = 0;

    public QuestionIterator(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public boolean hasNext() {
        return index < questions.size();
    }

    @Override
    public Question next() {
        return questions.get(index++);
    }
}
