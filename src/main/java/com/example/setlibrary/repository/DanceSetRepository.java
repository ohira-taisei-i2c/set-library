package com.example.setlibrary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.setlibrary.entity.DanceSet;


public interface DanceSetRepository extends JpaRepository<DanceSet, Long> {

    @Query(value = "SELECT COUNT(*) FROM DANCE_SET", nativeQuery = true)
    Long countDanceSets();

    @Query(value = """
        SELECT
            SYS_CONTEXT('USERENV', 'DB_NAME') || ' / ' ||
            SYS_CONTEXT('USERENV', 'SERVICE_NAME') || ' / ' ||
            SYS_CONTEXT('USERENV', 'CON_NAME')
        FROM DUAL
        """, nativeQuery = true)
    String getDatabaseInfo();

    @Query(value = "SELECT NAME FROM DANCE_SET", nativeQuery = true)
    List<String> getDanceSetNames();

    List<DanceSet> findByCategoryOrderByCreatedAtDesc(String category);

    List<DanceSet> findByNameContainingIgnoreCaseOrderByCreatedAtDesc(String name);

    List<DanceSet> findByCategoryAndNameContainingIgnoreCaseOrderByCreatedAtDesc(
            String category,
            String name);
    
    List<DanceSet> findAllByOrderByCreatedAtDesc();
}