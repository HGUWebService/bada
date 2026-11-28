package com.hgu.bada.observation;

import com.hgu.bada.beach.Beach;
import jakarta.persistence.*;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "sea_observation",
    // 같은 해변, 같은 시각, 같은 출처의 기록은 한 번만 저장 (수집기가 같은 데이터를 다시 받아도 중복 방지)
    uniqueConstraints = @UniqueConstraint(columnNames = {"beach_id", "observed_at", "source"})
)
@Getter
public class SeaObservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 여러 관측 기록(N)이 하나의 해변(1)에 속한다 → sea_observation.beach_id FK
    // LAZY: 관측 기록을 읽을 때 해변 정보는 실제로 쓸 때만 조회
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "beach_id")
    private Beach beach;

    @Column(nullable = false)
    private LocalDateTime observedAt;   // 관측 시각

    @Column(precision = 4, scale = 1)
    private BigDecimal waterTemp;       // 수온 ℃  (예: 14.2)

    private Integer tideLevel;          // 조위 cm (예: 35)

    @Column(precision = 4, scale = 2)
    private BigDecimal waveHeight;      // 파고 m  (예: 0.60)

    @Column(precision = 4, scale = 1)
    private BigDecimal windSpeed;       // 풍속 m/s (예: 5.3)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Source source;              // MANUAL / KHOA_TIDE / KMA_BUOY

    protected SeaObservation() {
    }

    public SeaObservation(Beach beach, LocalDateTime observedAt, BigDecimal waterTemp, Integer tideLevel,
                          BigDecimal waveHeight, BigDecimal windSpeed, Source source) {
        this.beach = beach;
        this.observedAt = observedAt;
        this.waterTemp = waterTemp;
        this.tideLevel = tideLevel;
        this.waveHeight = waveHeight;
        this.windSpeed = windSpeed;
        this.source = source;
    }

    // 관측값 수정. 어느 해변의 기록인지(beach)와 출처(source)는 바꾸지 않는다
    public void update(LocalDateTime observedAt, BigDecimal waterTemp, Integer tideLevel,
                       BigDecimal waveHeight, BigDecimal windSpeed) {
        this.observedAt = observedAt;
        this.waterTemp = waterTemp;
        this.tideLevel = tideLevel;
        this.waveHeight = waveHeight;
        this.windSpeed = windSpeed;
    }
}
