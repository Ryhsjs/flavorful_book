package ru.itis.flavorful_book.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.itis.flavorful_book.util.StringToUnitConverter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final StringToUnitConverter stringToUnitConverter;

    public WebMvcConfig(StringToUnitConverter stringToUnitConverter) {
        this.stringToUnitConverter = stringToUnitConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToUnitConverter);
    }
}
