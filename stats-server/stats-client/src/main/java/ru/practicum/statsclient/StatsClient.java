package ru.practicum.statsclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.StatViewDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatsClient {
    public static final String HIT_PATH = "/hit";
    public static final String STATS_PATH = "/stats";

    private final ParameterizedTypeReference<List<StatViewDto>> statsListTypeRef = new ParameterizedTypeReference<>() {
    };
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String baseUrl) {
        restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    }

    public ResponseEntity<Object> saveEndpointHit(EndpointHitDto endpointHitDto) {
        HttpEntity<EndpointHitDto> request = new HttpEntity<>(endpointHitDto, headers);
        return restTemplate.postForEntity(HIT_PATH, request, Object.class);
    }

    public ResponseEntity<List<StatViewDto>> getStats(LocalDateTime start, LocalDateTime end, List<String> uris,
                                                      boolean unique) {
        HttpEntity<EndpointHitDto> request = new HttpEntity<>(null, headers);
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromPath(STATS_PATH)
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("unique", unique);
        if (uris != null && !uris.isEmpty()) {
            builder.queryParam("uris", uris);
        }
        return restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET,
                request,
                statsListTypeRef);
    }
}
