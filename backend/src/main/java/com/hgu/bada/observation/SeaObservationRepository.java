package com.hgu.bada.observation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface SeaObservationRepository extends JpaRepository<SeaObservation, Long> {

    // 해변의 가장 최근 관측 1건 (해변 목록 카드, 상세 화면 최신값)
    // SELECT * FROM sea_observation WHERE beach_id = ? ORDER BY observed_at DESC LIMIT 1
    Optional<SeaObservation> findTopByBeachIdOrderByObservedAtDesc(Long beachId);

    // 해변의 관측 기록을 페이지 단위로 (정렬은 Pageable로 전달)
    // SELECT * FROM sea_observation WHERE beach_id = ? ORDER BY ... LIMIT ? OFFSET ?
    Page<SeaObservation> findByBeachId(Long beachId, Pageable pageable);

    // 해변을 삭제할 때 그 해변의 관측 기록을 먼저 삭제 (FK 때문에)
    void deleteByBeachId(Long beachId);

    // 수집기가 같은 데이터를 다시 저장하지 않도록 확인
    boolean existsByBeachIdAndObservedAtAndSource(Long beachId, LocalDateTime observedAt, Source source);
}
