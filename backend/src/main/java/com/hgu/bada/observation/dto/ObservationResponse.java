package com.hgu.bada.observation.dto;

import com.hgu.bada.observation.SeaObservation;
import com.hgu.bada.observation.Source;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ObservationResponse(
        Long id, Long beachId, LocalDateTime observedAt,
        BigDecimal waterTemp, Integer tideLevel, BigDecimal waveHeight, BigDecimal windSpeed,
        Source source
) {
    public static ObservationResponse from(SeaObservation o) {
        // o.getBeach().getId()는 LAZY 프록시에서도 DB 조회 없이 FK 값만 꺼낸다
        return new ObservationResponse(o.getId(), o.getBeach().getId(), o.getObservedAt(),
                o.getWaterTemp(), o.getTideLevel(), o.getWaveHeight(), o.getWindSpeed(), o.getSource());
    }
}
