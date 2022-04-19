package bsb.group5.employee.config;

import feign.RequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Configuration
@EnableFeignClients(basePackages = "bsb.group5.employee.repository")
@AllArgsConstructor
public class FeignConfig {

    private static final String JWT_PREFIX = "Bearer ";

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header(HttpHeaders.AUTHORIZATION, JWT_PREFIX +
                    "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbmEiLCJpYXQiOjE2NDc3MjIzMjIsImV4cCI6MTk2MzM0MTUyMn0.5tJuYy14oc-XHQS3Biauriwb0Fqf3FwRVVyT1Vn3G1AiOzwSlOPqHs0pDttlSste2zS2o9QLxeNnnXbg6r2Jlg");
            requestTemplate.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        };
    }
}
