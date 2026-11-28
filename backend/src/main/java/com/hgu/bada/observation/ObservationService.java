package com.hgu.bada.observation;

import com.hgu.bada.beach.Beach;
import com.hgu.bada.beach.BeachRepository;
import com.hgu.bada.common.ApiException;
import com.hgu.bada.common.PageResponse;
import com.hgu.bada.observation.dto.ObservationRequest;
import com.hgu.bada.observation.dto.ObservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ObservationService {

    private final SeaObservationRepository observationRepository;
    private final BeachRepository beachRepository;

    public PageResponse<ObservationResponse> findByBeach(Long beachId, Pageable pageable) {
        getBeach(beachId); // 없는 해변이면 404
        return PageResponse.from(observationRepository.findByBeachId(beachId, pageable)
                .map(ObservationResponse::from));
    }

    @Transactional
    public ObservationResponse create(Long beachId, ObservationRequest req) {
        Beach beach = getBeach(beachId);
        // 같은 해변·같은 시각의 수동 기록은 한 번만 (DB UNIQUE 제약 전에 409로 친절하게 응답)
        if (observationRepository.existsByBeachIdAndObservedAtAndSource(beachId, req.observedAt(), Source.MANUAL)) {
            throw ApiException.conflict("DUPLICATE_OBSERVATION", "같은 시각의 수동 관측 기록이 이미 있습니다.");
        }
        SeaObservation saved = observationRepository.save(new SeaObservation(beach, req.observedAt(),
                req.waterTemp(), req.tideLevel(), req.waveHeight(), req.windSpeed(), Source.MANUAL));
        return ObservationResponse.from(saved);
    }

    @Transactional
    public ObservationResponse update(Long id, ObservationRequest req) {
        SeaObservation observation = getObservation(id);
        observation.update(req.observedAt(), req.waterTemp(), req.tideLevel(), req.waveHeight(), req.windSpeed());
        return ObservationResponse.from(observation);
    }

    @Transactional
    public void delete(Long id) {
        observationRepository.delete(getObservation(id));
    }

    private Beach getBeach(Long id) {
        return beachRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("BEACH_NOT_FOUND", "해변을 찾을 수 없습니다. id=" + id));
    }

    private SeaObservation getObservation(Long id) {
        return observationRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("OBSERVATION_NOT_FOUND", "관측 기록을 찾을 수 없습니다. id=" + id));
    }
}
