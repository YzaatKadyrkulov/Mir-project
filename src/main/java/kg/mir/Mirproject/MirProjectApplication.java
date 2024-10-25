package kg.mir.Mirproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "kg.mir.Mirproject")
public class MirProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(MirProjectApplication.class, args);
    }
}
