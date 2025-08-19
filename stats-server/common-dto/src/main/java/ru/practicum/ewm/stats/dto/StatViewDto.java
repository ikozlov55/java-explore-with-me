package ru.practicum.ewm.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatViewDto {
    String app;
    String uri;
    Long hits;
}
