package bsb.group5.auth.util;

import bsb.group5.auth.config.MessageConfig;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;


@Component
@AllArgsConstructor
public class ResponseBodyUtil {
    private final MessageConfig messageConfig;

    public Map<String, Object> getResponseBody(String message, HttpStatus status) {
        Map<String, Object> resultBody = new LinkedHashMap<>();
        resultBody.put(messageConfig.getMessageFieldTimestamp(), String.valueOf(LocalDateTime.now()));
        resultBody.put(messageConfig.getMessageFieldStatus(), status);
        resultBody.put(messageConfig.getMessageFieldMessage(), message);
        return resultBody;
    }
}
