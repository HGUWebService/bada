package com.hgu.bada.beach.dto;

import com.hgu.bada.beach.Beach;
import com.hgu.bada.beach.Region;
import com.hgu.bada.observation.dto.ObservationResponse;

// 해변 목록 카드 하나: 해변 기본 정보 + 최신 관측값(없으면 null)
public record BeachSummaryResponse(Long id, String name, Region region, ObservationResponse latest) {

    public static BeachSummaryResponse of(Beach beach, ObservationResponse latest) {
        return new BeachSummaryResponse(beach.getId(), beach.getName(), beach.getRegion(), latest);
    }
}
