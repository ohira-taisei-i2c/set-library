package com.example.setlibrary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.setlibrary.entity.DanceSet;


/**
 * DanceSetエンティティのデータアクセスを提供するリポジトリ。
 */
public interface DanceSetRepository extends JpaRepository<DanceSet, Long> {

    /**
     * DANCE_SETテーブルに登録されている件数を取得する。
     *
     * @return 登録件数
     */
    @Query(value = "SELECT COUNT(*) FROM DANCE_SET", nativeQuery = true)
    Long countDanceSets();

    /**
     * 接続先データベースの名前、サービス名、コンテナ名を取得する。
     *
     * @return データベース接続先の情報
     */
    @Query(value = """
        SELECT
            SYS_CONTEXT('USERENV', 'DB_NAME') || ' / ' ||
            SYS_CONTEXT('USERENV', 'SERVICE_NAME') || ' / ' ||
            SYS_CONTEXT('USERENV', 'CON_NAME')
        FROM DUAL
        """, nativeQuery = true)
    String getDatabaseInfo();

    /**
     * 登録されているSET名の一覧を取得する。
     *
     * @return SET名の一覧
     */
    @Query(value = "SELECT NAME FROM DANCE_SET", nativeQuery = true)
    List<String> getDanceSetNames();

    List<DanceSet> findByCategoryOrderByCreatedAtDesc(String category);

    List<DanceSet> findByNameContainingIgnoreCaseOrderByCreatedAtDesc(String name);

    List<DanceSet> findByCategoryAndNameContainingIgnoreCaseOrderByCreatedAtDesc(
            String category,
            String name);
    
    List<DanceSet> findAllByOrderByCreatedAtDesc();
}