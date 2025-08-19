package ru.practicum.ewm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.StatViewDto;
import ru.practicum.statsclient.StatsClient;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Import(StatsClient.class)
public class DebugController {
    private final StatsClient statsClient;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> saveEndpointHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        return statsClient.saveEndpointHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatViewDto>> getStats(@RequestParam LocalDateTime start,
                                                      @RequestParam LocalDateTime end,
                                                      @RequestParam(required = false) List<String> uris,
                                                      @RequestParam(defaultValue = "false") boolean unique) {
        return statsClient.getStats(start, end, uris, unique);
    }
}
