package com.hms.backend.doctors.dto;

import java.util.List;

public record CursorPageResponseDto<T>(
        List<T> content,
        String nextCursor,
        boolean hasNext) {
}