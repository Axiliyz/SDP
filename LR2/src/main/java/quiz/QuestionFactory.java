package quiz;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

public interface QuestionFactory {
    Question createTextQuestion(String question, String answer);

    Question createMultiChoiceQuestion(String q,
                                      List<String> options,
                                      Set<String> correct);
}
