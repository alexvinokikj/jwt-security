package tl.alex;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.Environment;

/** Application entry point */
@SpringBootApplication
@Slf4j
public class ServiceApplication {

    @Autowired Environment environment;

    public static void main(String[] args) {
        log.info("Running security micro-service");
        SpringApplication application = new SpringApplicationBuilder(ServiceApplication.class).build();
        application.run(args);
    }
}
