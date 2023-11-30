package example.webflux;

import example.webflux.hello.GreetingClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Slf4j
@EnableR2dbcRepositories
@SpringBootApplication
public class ExampleWebfluxApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ExampleWebfluxApplication.class, args);
        String message = context.getBean(GreetingClient.class)
                .getMessage().block();
        log.atInfo().log("message: {}", message);
    }


}
