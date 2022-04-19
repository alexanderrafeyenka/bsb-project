package bsb.group5.employee.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Getter
@PropertySource(value = "classpath:/messages.properties")
public class MessageConfig {
    @Value("${message.invalid.parameters}")
    private String messageInvalidParameters;
    @Value("${message.employee.not.exists}")
    private String messageEmployeeNotExists;
    @Value("${message.employee.added}")
    private String messageEmployeeAdded;
    @Value("${message.employee.exists}")
    private String messageEmployeeExists;
    @Value("${message.employee.not.found}")
    private String messageEmployeeNotFound;
    @Value("${message.something.went.wrong}")
    private String messageSomethingWentWrong;
    @Value("${message.cannot.process}")
    private String messageCannotProcess;
    @Value("${message.field.timestamp}")
    private String messageFieldTimestamp;
    @Value("${message.field.errors}")
    private String messageFieldErrors;
    @Value("${message.field.status}")
    private String messageFieldStatus;
    @Value("${message.field.body}")
    private String messageFieldBody;
    @Value("${message.field.message}")
    private String messageFieldMessage;
    @Value("${message.authentication.exception}")
    private String messageAuthenticationException;
    @Value("${pagination.max.default}")
    private String paginationMaxDefault;
}
