package bsb.group5.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class, scanBasePackages = "bsb.group5.company")
public class CompanyServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompanyServiceApplication.class, args);
    }
}
