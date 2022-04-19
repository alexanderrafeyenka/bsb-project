package bsb.group5.converter.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Getter
@PropertySource(value = "classpath:/messages.properties")
public class MessageConfig {
    @Value("${message.something.went.wrong}")
    private String messageSomethingWentWrong;
    @Value("${message.authentication.exception}")
    private String messageAuthenticationException;
    @Value("${message.pagination.exception}")
    private String messagePaginationExceptions;
    @Value("${message.cannot.process}")
    private String messageCannotProcess;
}
