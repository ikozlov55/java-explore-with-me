package ru.practicum.ewm.stats.service.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.practicum.ewm.stats.dto.Constants;

import java.time.format.DateTimeFormatter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT));
        registrar.registerFormatters(registry);
    }
}
