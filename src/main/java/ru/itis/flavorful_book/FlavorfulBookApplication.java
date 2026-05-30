package ru.itis.flavorful_book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class FlavorfulBookApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FlavorfulBookApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(FlavorfulBookApplication.class, args);
    }
}
