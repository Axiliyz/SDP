package com.example.quiz.factory;

import com.example.quiz.model.Question;
import com.example.quiz.model.TextQuestion;
import com.example.quiz.model.MultiChoiceQuestion;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class QuestionFactory {
    public Question createTextQuestion(String question, String answer) {
        return new TextQuestion.Builder(question, answer).build();
    }
    public Question createMultiChoiceQuestion(
            String question,
            List<String> options,
            Set<String> correct
    ) {
        return new MultiChoiceQuestion(question, options, correct);
    }
}
