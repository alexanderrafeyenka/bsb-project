package bsb.group5.company.util;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;


@Component
@AllArgsConstructor
public class ResponseBodyUtil {
    public Map<String, Object> getResponseBody(String message, HttpStatus status) {
        Map<String, Object> resultBody = new LinkedHashMap<>();
        resultBody.put("timestamp", String.valueOf(LocalDateTime.now()));
        resultBody.put("status", status);
        resultBody.put("message", message);
        return resultBody;
    }
}
