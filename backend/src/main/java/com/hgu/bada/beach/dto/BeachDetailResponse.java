package com.hgu.bada.beach.dto;

import com.hgu.bada.beach.Beach;
import com.hgu.bada.beach.Region;
import com.hgu.bada.observation.dto.ObservationResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// 해변 상세 화면: 모든 정보 + 최신 관측값. Entity를 그대로 내보내지 않고 필요한 값만 복사한다
public record BeachDetailResponse(
        Long id, String name, Region region,
        BigDecimal latitude, BigDecimal longitude,
        String tideStationCode, String buoyStationId, String description,
        LocalDateTime createdAt, LocalDateTime updatedAt,
        ObservationResponse latest
) {
    public static BeachDetailResponse of(Beach b, ObservationResponse latest) {
        return new BeachDetailResponse(b.getId(), b.getName(), b.getRegion(),
                b.getLatitude(), b.getLongitude(),
                b.getTideStationCode(), b.getBuoyStationId(), b.getDescription(),
                b.getCreatedAt(), b.getUpdatedAt(), latest);
    }
}
