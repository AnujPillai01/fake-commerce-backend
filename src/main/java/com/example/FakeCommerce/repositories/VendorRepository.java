package com.example.FakeCommerce.repositories;

import com.example.FakeCommerce.schema.Vendor;
import com.example.FakeCommerce.schema.VendorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor,Long> {
    Optional<Vendor> findByUserId(Long userId);
    List<Vendor> findByStatus(VendorStatus status);

    @Query("SELECT v FROM Vendor v JOIN FETCH v.user WHERE v.id = :id")
    Optional<Vendor> findByIdWithUser(@Param("id") Long id);

    @Query("SELECT v FROM Vendor v JOIN FETCH v.user WHERE v.status = :status")
    List<Vendor> findByStatusWithUser(@Param("status") VendorStatus status);
}
