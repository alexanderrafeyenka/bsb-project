package bsb.group5.auth.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Getter
@PropertySource(value = "classpath:/messages.properties")
public class MessageConfig {
    @Value("${message.username.password.not.match}")
    private String messageUsernamePasswordNotMatch;
    @Value("${message.session.disabled}")
    private String messageSessionDisabled;
    @Value("${message.account.blocked}")
    private String messageAccountBlocked;
    @Value("${message.invalid.login.password}")
    private String messageInvalidLoginPassword;
    @Value("${message.user.already.exists}")
    private String messageUserAlreadyExists;
    @Value("${message.something.went.wrong}")
    private String messageSomethingWentWrong;
    @Value("${message.cannot.process}")
    private String messageCannotProcess;
    @Value("${message.not.unique}")
    private String messageNotUnique;
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
}


