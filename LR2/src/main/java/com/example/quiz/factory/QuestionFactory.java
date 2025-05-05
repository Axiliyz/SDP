package com.example.quiz.factory;

import com.example.quiz.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class QuestionFactory {

    public Question createTextQuestion(String q, String answer) {
        return new TextQuestion.Builder(q, answer).build();
    }

    public Question createMultiChoiceQuestion(String q,
                                              List<String> options,
                                              Set<String> correct) {
        return new MultiChoiceQuestion(q, options, correct);
    }
}
