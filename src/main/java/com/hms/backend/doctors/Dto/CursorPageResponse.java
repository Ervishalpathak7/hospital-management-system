package com.hms.backend.doctors.Dto;

import java.util.List;

public record CursorPageResponse<T>(
                List<T> content,
                String nextCursor,
                boolean hasNext) {
}