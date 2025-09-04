package ru.practicum.ewm.testutils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.ewm.event.service.ViewService;

@Configuration
public class TestConfig {
    @Bean
    public ViewService viewService() {
        return new MockViewService();
    }
}
