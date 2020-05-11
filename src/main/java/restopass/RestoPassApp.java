package restopass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestoPassApp {
    public static void main(String... args) {
        System.setProperty("server.servlet.context-path", "/restopass");
        SpringApplication.run(RestoPassApp.class, args);
    }
}
