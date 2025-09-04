package ru.practicum.ewm.request.dto;

import java.util.List;

public record RequestStatusUpdateResult(List<RequestDto> confirmedRequests,
                                        List<RequestDto> rejectedRequests) {
}
