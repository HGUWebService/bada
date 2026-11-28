package com.hgu.bada.beach;

import com.hgu.bada.beach.dto.BeachDetailResponse;
import com.hgu.bada.beach.dto.BeachRequest;
import com.hgu.bada.beach.dto.BeachSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

// HTTP 요청을 받아 Service에 넘기고, 결과를 알맞은 상태 코드로 돌려준다. 비즈니스 규칙은 두지 않는다
@Tag(name = "Beach", description = "해변 CRUD")
@RestController
@RequestMapping("/api/beaches")
@RequiredArgsConstructor
public class BeachController {

    private final BeachService beachService;

    @Operation(summary = "해변 목록 (지역·이름 검색)")
    @GetMapping
    public List<BeachSummaryResponse> list(@RequestParam(required = false) Region region,
                                           @RequestParam(required = false) String keyword) {
        return beachService.findAll(region, keyword);                     // 200
    }

    @Operation(summary = "해변 상세")
    @GetMapping("/{id}")
    public BeachDetailResponse get(@PathVariable Long id) {
        return beachService.findById(id);                                 // 200, 없으면 404
    }

    @Operation(summary = "해변 등록")
    @PostMapping
    public ResponseEntity<BeachDetailResponse> create(@Valid @RequestBody BeachRequest request) {
        BeachDetailResponse created = beachService.create(request);
        return ResponseEntity.created(URI.create("/api/beaches/" + created.id())) // 201 + Location 헤더
                .body(created);
    }

    @Operation(summary = "해변 수정")
    @PutMapping("/{id}")
    public BeachDetailResponse update(@PathVariable Long id, @Valid @RequestBody BeachRequest request) {
        return beachService.update(id, request);                          // 200
    }

    @Operation(summary = "해변 삭제 (관측 기록도 함께 삭제)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        beachService.delete(id);
        return ResponseEntity.noContent().build();                        // 204
    }
}
