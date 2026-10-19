package com.example.setlibrary.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.setlibrary.entity.DanceSet;
import com.example.setlibrary.repository.DanceSetRepository;

@Service
public class DanceSetService {

    private final DanceSetRepository danceSetRepository;

    public DanceSetService(DanceSetRepository danceSetRepository) {
        this.danceSetRepository = danceSetRepository;
    }

    public List<DanceSet> findAll() {
    	
    	System.out.println("★★★ DB情報 = " + danceSetRepository.getDatabaseInfo());

        Long count = danceSetRepository.countDanceSets();

        System.out.println("★★★ OracleのDANCE_SET件数 = " + count);

        List<DanceSet> danceSets = danceSetRepository.findAll();

        return danceSets;
    }
}