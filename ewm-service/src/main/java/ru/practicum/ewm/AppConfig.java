package ru.practicum.ewm;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.practicum.ewm.core.json.JacksonConfig;

@Configuration
@Import(JacksonConfig.class)
public class AppConfig {
}
