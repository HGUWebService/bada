package com.hgu.bada.beach;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// JpaRepository<Beach, Long>: Beach 테이블을 다루고, PK 타입은 Long
// save, findById, findAll, deleteById는 상속만으로 자동 제공된다
public interface BeachRepository extends JpaRepository<Beach, Long> {

    // SELECT * FROM beach WHERE region = ?
    List<Beach> findByRegion(Region region);

    // SELECT * FROM beach WHERE name LIKE '%keyword%'
    List<Beach> findByNameContaining(String keyword);

    // SELECT * FROM beach WHERE region = ? AND name LIKE '%keyword%'
    List<Beach> findByRegionAndNameContaining(Region region, String keyword);

    // SELECT COUNT(*) > 0 FROM beach WHERE name = ?   (등록 시 이름 중복 확인)
    boolean existsByName(String name);

    // SELECT COUNT(*) > 0 FROM beach WHERE name = ? AND id <> ?
    // 수정할 때는 "자기 자신을 뺀" 나머지 중에 같은 이름이 있는지 확인
    boolean existsByNameAndIdNot(String name, Long id);
}
