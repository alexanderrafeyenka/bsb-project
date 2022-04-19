package bsb.group5.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class, scanBasePackages = "bsb.group5.employee")
public class EmployeeApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmployeeApplication.class, args);
    }
}
