package bsb.group5.company.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Getter
@PropertySource(value = "classpath:/messages.properties")
public class MessageConfig {
    @Value("${message.invalid.parameters}")
    private String messageInvalidSearchParameters;
    @Value("${message.company.not.exists}")
    private String messageCompanyNotExists;
    @Value("${message.company.added}")
    private String messageCompanyAdded;
    @Value("${message.company.exists}")
    private String messageCompanyExists;
    @Value("${message.company.not.found}")
    private String messageCompanyNotFound;
    @Value("${message.something.went.wrong}")
    private String messageSomethingWentWrong;
    @Value("${message.cannot.process}")
    private String messageCannotProcess;
    @Value("${message.authentication.exception}")
    private String messageAuthenticationException;
}
