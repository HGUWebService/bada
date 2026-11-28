package com.hgu.bada.observation.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// 관측 기록 수동 등록(POST)·수정(PUT) 요청 본문
public record ObservationRequest(
        @NotNull(message = "관측 시각은 필수입니다.")
        @PastOrPresent(message = "관측 시각은 미래일 수 없습니다.")
        LocalDateTime observedAt,

        @DecimalMin(value = "-2.0", message = "수온은 -2 ~ 35℃입니다.")
        @DecimalMax(value = "35.0", message = "수온은 -2 ~ 35℃입니다.")
        BigDecimal waterTemp,

        Integer tideLevel,

        @DecimalMin(value = "0.0", message = "파고는 0 ~ 20m입니다.")
        @DecimalMax(value = "20.0", message = "파고는 0 ~ 20m입니다.")
        BigDecimal waveHeight,

        @DecimalMin(value = "0.0", message = "풍속은 0 ~ 60m/s입니다.")
        @DecimalMax(value = "60.0", message = "풍속은 0 ~ 60m/s입니다.")
        BigDecimal windSpeed
) {
}
