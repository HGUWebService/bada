package com.hgu.bada.beach;

import jakarta.persistence.*;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity                  // 이 클래스 하나 = DB의 beach 테이블 하나
@Table(name = "beach")
@Getter                  // getId(), getName() 같은 getter를 자동 생성 (setter는 만들지 않음)
public class Beach {

    @Id                                                   // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // MariaDB AUTO_INCREMENT로 번호 발급
    private Long id;

    @Column(nullable = false, unique = true, length = 50) // VARCHAR(50) NOT NULL UNIQUE
    private String name;

    @Enumerated(EnumType.STRING)                          // 0, 1이 아니라 "NAM", "BUK" 글자로 저장
    @Column(nullable = false, length = 10)
    private Region region;

    @Column(precision = 9, scale = 6)                     // DECIMAL(9,6): 소수점 오차 없는 좌표
    private BigDecimal latitude;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(length = 20)                                  // DB 컬럼명은 tide_station_code로 자동 변환
    private String tideStationCode;

    @Column(length = 20)
    private String buoyStationId;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, updatable = false)          // 처음 저장 후에는 바뀌지 않음
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // JPA가 DB에서 읽은 값을 담을 때 쓰는 빈 생성자. 외부에서는 못 쓰게 protected
    protected Beach() {
    }

    // 새 해변을 등록할 때 사용
    public Beach(String name, Region region, BigDecimal latitude, BigDecimal longitude,
                 String tideStationCode, String buoyStationId, String description) {
        this.name = name;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tideStationCode = tideStationCode;
        this.buoyStationId = buoyStationId;
        this.description = description;
    }

    // 해변 정보를 수정할 때 사용. setter 대신 이 메서드로만 값을 바꾼다
    public void update(String name, Region region, BigDecimal latitude, BigDecimal longitude,
                       String tideStationCode, String buoyStationId, String description) {
        this.name = name;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tideStationCode = tideStationCode;
        this.buoyStationId = buoyStationId;
        this.description = description;
    }

    @PrePersist   // INSERT 직전에 JPA가 자동으로 호출
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate    // UPDATE 직전에 JPA가 자동으로 호출
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
