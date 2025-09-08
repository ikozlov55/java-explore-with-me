package ru.practicum.ewm.core.json;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public Module customModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new TrimDeserializer());
        return module;
    }
}
