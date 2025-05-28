package quiz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Scanner;

@Configuration
public class AppConfig {

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean
    @Primary
    public TestManager testManager(Scanner scanner) {
        // return new DefaultTestManager(scanner);
        return new AlternativeTestManager(scanner);
    }

    @Bean
    @Primary
    public QuestionFactory questionFactory() {
        return new DefaultQuestionFactory();
    }
}
