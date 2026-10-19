package com.example.setlibrary.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.setlibrary.entity.DanceSet;
import com.example.setlibrary.repository.DanceSetRepository;

@Controller
public class DanceSetController {

    private final DanceSetRepository danceSetRepository;

    public DanceSetController(DanceSetRepository danceSetRepository) {
        this.danceSetRepository = danceSetRepository;
    }

    @GetMapping("/sets")
    public String showSets(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            Model model) {

        List<DanceSet> danceSets;

        boolean hasCategory = category != null && !category.isBlank();
        boolean hasKeyword = keyword != null && !keyword.isBlank();

        if (hasCategory && hasKeyword) {

            danceSets = danceSetRepository
                    .findByCategoryAndNameContainingIgnoreCaseOrderByCreatedAtDesc(
                            category, keyword);

        } else if (hasCategory) {

            danceSets = danceSetRepository
                    .findByCategoryOrderByCreatedAtDesc(category);

        } else if (hasKeyword) {

            danceSets = danceSetRepository
                    .findByNameContainingIgnoreCaseOrderByCreatedAtDesc(keyword);

        } else {

            danceSets = danceSetRepository
                    .findAllByOrderByCreatedAtDesc();
        }

        model.addAttribute("danceSets", danceSets);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("resultCount", danceSets.size());

        boolean hasSearchCondition = hasCategory || hasKeyword;
        model.addAttribute("showResultCount", hasSearchCondition);

        return "sets";
    }

    @GetMapping("/sets/new")
    public String showNewSetForm() {

        return "set-form";
    }

    @PostMapping("/sets")
    public String createSet(
            DanceSet danceSet,
            @RequestParam("video") MultipartFile video) throws IOException {

        if (!video.isEmpty()) {

            Path uploadDir = Paths.get("uploads/videos");

            Files.createDirectories(uploadDir);

            String fileName = video.getOriginalFilename();

            Path filePath = uploadDir.resolve(fileName);

            video.transferTo(filePath);

            danceSet.setVideoUrl("/videos/" + fileName);
        }

        danceSetRepository.save(danceSet);

        return "redirect:/sets";
    }

    @GetMapping("/sets/{id}")
    public String showSetDetail(@PathVariable Long id, Model model) {

        Optional<DanceSet> danceSet = danceSetRepository.findById(id);

        if (danceSet.isEmpty()) {
            return "redirect:/sets";
        }

        model.addAttribute("danceSet", danceSet.get());

        return "set-detail";
    }
    
    @GetMapping("/sets/{id}/edit")
    public String showEditForm(
            @PathVariable Long id,
            Model model) {

        DanceSet danceSet = danceSetRepository.findById(id)
                .orElseThrow();

        model.addAttribute("danceSet", danceSet);

        return "set-edit";
    }
    
    @PostMapping("/sets/{id}/update")
    public String updateSet(
            @PathVariable Long id,
            DanceSet danceSet) {

        DanceSet existingSet = danceSetRepository.findById(id)
                .orElseThrow();

        existingSet.setName(danceSet.getName());
        existingSet.setCategory(danceSet.getCategory());
        existingSet.setDescription(danceSet.getDescription());
        existingSet.setVideoUrl(danceSet.getVideoUrl());

        danceSetRepository.save(existingSet);

        return "redirect:/sets/" + id;
    }
    
    @PostMapping("/sets/{id}/delete")
    public String deleteSet(@PathVariable Long id) {

        danceSetRepository.deleteById(id);

        return "redirect:/sets";
    }
}