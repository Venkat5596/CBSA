
// 3. Enhanced Repository with Additional Query Methods
package com.sks.tariff_01.repo;

import com.sks.tariff_01.entity.GSTCodes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnhancedGSTCodeRepo extends JpaRepository<GSTCodes, String> {

    List<GSTCodes> findAllByGstRateType(String gstRateType);

    @Query("SELECT c FROM GSTCodes c WHERE c.description LIKE %:keyword%")
    List<GSTCodes> searchByDescription(@Param("keyword") String keyword);

    @Query("SELECT c FROM GSTCodes c WHERE c.gstCode LIKE %:code%")
    List<GSTCodes> searchByGstCode(@Param("code") String code);

    @Query("SELECT DISTINCT c.gstRateType FROM GSTCodes c WHERE c.gstRateType IS NOT NULL")
    List<String> findDistinctGstRateTypes();

    @Query("SELECT c FROM GSTCodes c WHERE c.inactiveIndicator = :active")
    Page<GSTCodes> findByActiveStatus(@Param("active") boolean active, Pageable pageable);

    @Query("SELECT c FROM GSTCodes c WHERE c.language = :language")
    List<GSTCodes> findByLanguage(@Param("language") String language);

    @Query("SELECT COUNT(c) FROM GSTCodes c WHERE c.gstRateType = :rateType")
    long countByGstRateType(@Param("rateType") String rateType);

    Optional<GSTCodes> findByGstCodeIgnoreCase(String gstCode);
}