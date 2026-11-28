package com.hgu.bada.common;

import org.springframework.data.domain.Page;
import java.util.List;

// Spring의 Page 객체를 그대로 JSON으로 내보내지 않고, 필요한 값만 담아 응답
public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }
}
