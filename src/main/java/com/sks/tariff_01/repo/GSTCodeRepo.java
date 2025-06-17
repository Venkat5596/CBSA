package com.sks.tariff_01.repo;

import com.sks.tariff_01.entity.GSTCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GSTCodeRepo extends JpaRepository<GSTCodes, String> {
}
