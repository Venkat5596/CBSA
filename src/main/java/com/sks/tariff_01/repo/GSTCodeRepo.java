package com.sks.tariff_01.repo;

import com.sks.tariff_01.entity.GSTCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GSTCodeRepo extends JpaRepository<GSTCodes, String> {

    List<GSTCodes> findAllByGstRateType(String gstRateType);
    @Query("SELECT c FROM GSTCodes c WHERE c.description LIKE %:keyword%")
    List<GSTCodes> searchByDescription(@Param("keyword") String keyword);
}
