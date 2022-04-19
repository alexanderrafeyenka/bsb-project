package bsb.group5.converter.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Getter
public class PaginationConfig {
    @Value("${pagination.max.default}")
    private String paginationMaxDefault;
}
