package com.memorykeeper.memory_keeper.repository;

import com.memorykeeper.memory_keeper.model.DementiaCenter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DementiaCenterRepository extends JpaRepository<DementiaCenter, Long> {
    boolean existsByCnterNmAndRdnmadrAndLnmadr(String cnterNm, String rdnmadr, String lnmadr);
}
