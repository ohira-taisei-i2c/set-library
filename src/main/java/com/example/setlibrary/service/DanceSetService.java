package com.example.setlibrary.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.setlibrary.entity.DanceSet;
import com.example.setlibrary.repository.DanceSetRepository;

@Service
/**
 * DanceSetに関する業務処理を提供するサービス。
 */
public class DanceSetService {

    private final DanceSetRepository danceSetRepository;

    /**
     * リポジトリを受け取り、サービスを初期化する。
     *
     * @param danceSetRepository DanceSetのデータアクセスを行うリポジトリ
     */
    public DanceSetService(DanceSetRepository danceSetRepository) {
        this.danceSetRepository = danceSetRepository;
    }

    /**
     * 登録されているSETをすべて取得する。
     *
     * @return 登録されているSETの一覧
     */
    public List<DanceSet> findAll() {
    	
    	System.out.println("★★★ DB情報 = " + danceSetRepository.getDatabaseInfo());

        Long count = danceSetRepository.countDanceSets();

        System.out.println("★★★ OracleのDANCE_SET件数 = " + count);

        List<DanceSet> danceSets = danceSetRepository.findAll();

        return danceSets;
    }
}