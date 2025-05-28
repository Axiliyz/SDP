package quiz;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class DefaultQuestionFactory implements QuestionFactory {
    
    @Override
    public Question createTextQuestion(String question, String answer) {
        return new TextQuestion.Builder(question, answer).build();
    }

    @Override
    public Question createMultiChoiceQuestion(String q,
                                            List<String> options,
                                            Set<String> correct) {
        return new MultiChoiceQuestion(q, options, correct);
    }
} 