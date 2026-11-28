package com.hgu.bada.observation;

import com.hgu.bada.common.PageResponse;
import com.hgu.bada.observation.dto.ObservationRequest;
import com.hgu.bada.observation.dto.ObservationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

// 관측 기록은 해변에 속하므로 생성·조회는 /api/beaches/{beachId}/observations,
// 수정·삭제는 관측 id만으로 대상이 정해지므로 /api/observations/{id}
@Tag(name = "Observation", description = "바다 관측 기록 CRUD")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ObservationController {

    private final ObservationService observationService;

    @Operation(summary = "해변의 관측 기록 목록 (최신순, 페이지)")
    @GetMapping("/beaches/{beachId}/observations")
    public PageResponse<ObservationResponse> list(
            @PathVariable Long beachId,
            @ParameterObject @PageableDefault(size = 20, sort = "observedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return observationService.findByBeach(beachId, pageable);
    }

    @Operation(summary = "관측 기록 수동 등록")
    @PostMapping("/beaches/{beachId}/observations")
    public ResponseEntity<ObservationResponse> create(@PathVariable Long beachId,
                                                      @Valid @RequestBody ObservationRequest request) {
        ObservationResponse created = observationService.create(beachId, request);
        return ResponseEntity.created(URI.create("/api/observations/" + created.id())).body(created);
    }

    @Operation(summary = "관측 기록 수정")
    @PutMapping("/observations/{id}")
    public ObservationResponse update(@PathVariable Long id, @Valid @RequestBody ObservationRequest request) {
        return observationService.update(id, request);
    }

    @Operation(summary = "관측 기록 삭제")
    @DeleteMapping("/observations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        observationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
