package com.hgu.bada.beach.dto;

import com.hgu.bada.beach.Region;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

// 해변 등록(POST)·수정(PUT) 요청 본문. @Valid가 붙으면 아래 규칙을 자동 검사하고, 어기면 400
public record BeachRequest(
        @NotBlank(message = "해변 이름은 필수입니다.")
        @Size(max = 50, message = "해변 이름은 50자 이하입니다.")
        String name,

        @NotNull(message = "지역(NAM, BUK)은 필수입니다.")
        Region region,

        @DecimalMin(value = "-90", message = "위도는 -90 ~ 90입니다.")
        @DecimalMax(value = "90", message = "위도는 -90 ~ 90입니다.")
        BigDecimal latitude,

        @DecimalMin(value = "-180", message = "경도는 -180 ~ 180입니다.")
        @DecimalMax(value = "180", message = "경도는 -180 ~ 180입니다.")
        BigDecimal longitude,

        @Size(max = 20) String tideStationCode,
        @Size(max = 20) String buoyStationId,
        @Size(max = 500) String description
) {
}
