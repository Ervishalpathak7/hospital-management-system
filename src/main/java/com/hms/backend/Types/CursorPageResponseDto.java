package com.hms.backend.Types;

import java.util.List;

public record CursorPageResponseDto<T>(
        List<T> content,
        String nextCursor,
        boolean hasNext) {
}