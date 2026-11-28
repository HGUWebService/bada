package com.hgu.bada.beach;

import com.hgu.bada.beach.dto.BeachDetailResponse;
import com.hgu.bada.beach.dto.BeachRequest;
import com.hgu.bada.beach.dto.BeachSummaryResponse;
import com.hgu.bada.common.ApiException;
import com.hgu.bada.observation.SeaObservationRepository;
import com.hgu.bada.observation.dto.ObservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

// 해변 기능의 실제 규칙(중복 검사, 없는 해변 처리, 삭제 순서)이 모이는 곳
@Service
@RequiredArgsConstructor                // final 필드를 받는 생성자 자동 생성 → 생성자 주입
@Transactional(readOnly = true)         // 기본은 읽기 전용 트랜잭션
public class BeachService {

    private final BeachRepository beachRepository;
    private final SeaObservationRepository observationRepository;

    // 목록: 조건에 따라 다른 Query Method를 사용
    public List<BeachSummaryResponse> findAll(Region region, String keyword) {
        boolean hasKeyword = keyword != null && !keyword.isBlank();
        List<Beach> beaches;
        if (region != null && hasKeyword) {
            beaches = beachRepository.findByRegionAndNameContaining(region, keyword);
        } else if (region != null) {
            beaches = beachRepository.findByRegion(region);
        } else if (hasKeyword) {
            beaches = beachRepository.findByNameContaining(keyword);
        } else {
            beaches = beachRepository.findAll();
        }
        return beaches.stream()
                .map(beach -> BeachSummaryResponse.of(beach, latestOf(beach.getId())))
                .toList();
    }

    public BeachDetailResponse findById(Long id) {
        Beach beach = getBeach(id);
        return BeachDetailResponse.of(beach, latestOf(id));
    }

    @Transactional                      // 쓰기 작업은 읽기 전용이 아닌 트랜잭션
    public BeachDetailResponse create(BeachRequest req) {
        if (beachRepository.existsByName(req.name())) {
            throw ApiException.conflict("DUPLICATE_BEACH_NAME", "이미 등록된 해변 이름입니다: " + req.name());
        }
        Beach beach = new Beach(req.name(), req.region(), req.latitude(), req.longitude(),
                req.tideStationCode(), req.buoyStationId(), req.description());
        Beach saved = beachRepository.save(beach);   // INSERT
        return BeachDetailResponse.of(saved, null);  // 새 해변이라 관측값 없음
    }

    @Transactional
    public BeachDetailResponse update(Long id, BeachRequest req) {
        Beach beach = getBeach(id);
        if (beachRepository.existsByNameAndIdNot(req.name(), id)) {
            throw ApiException.conflict("DUPLICATE_BEACH_NAME", "이미 등록된 해변 이름입니다: " + req.name());
        }
        // save()를 부르지 않아도 트랜잭션이 끝날 때 바뀐 값을 감지해 UPDATE (dirty checking)
        beach.update(req.name(), req.region(), req.latitude(), req.longitude(),
                req.tideStationCode(), req.buoyStationId(), req.description());
        return BeachDetailResponse.of(beach, latestOf(id));
    }

    @Transactional
    public void delete(Long id) {
        Beach beach = getBeach(id);
        observationRepository.deleteByBeachId(id); // FK 때문에 관측 기록을 먼저 삭제
        beachRepository.delete(beach);
    }

    // 없는 id면 404
    private Beach getBeach(Long id) {
        return beachRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("BEACH_NOT_FOUND", "해변을 찾을 수 없습니다. id=" + id));
    }

    private ObservationResponse latestOf(Long beachId) {
        return observationRepository.findTopByBeachIdOrderByObservedAtDesc(beachId)
                .map(ObservationResponse::from)
                .orElse(null);
    }
}
