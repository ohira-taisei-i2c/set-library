package com.example.setlibrary.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
/**
 * SETの一覧表示、登録、詳細表示、編集、削除を扱うコントローラー。
 */
public class DanceSetController {

    private final DanceSetRepository danceSetRepository;

    /**
     * データアクセス用リポジトリを受け取り、コントローラーを初期化する。
     *
     * @param danceSetRepository SET情報を操作するリポジトリ
     */
    public DanceSetController(DanceSetRepository danceSetRepository) {
        this.danceSetRepository = danceSetRepository;
    }

    /**
     * 検索条件に応じたSET一覧を表示する。
     *
     * @param category 絞り込むカテゴリー（省略可）
     * @param keyword SET名の検索キーワード（省略可）
     * @param model ビューに渡す表示データ
     * @return 一覧画面のテンプレート名
     */
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

    /**
     * SETの新規登録フォームを表示する。
     *
     * @return 登録フォームのテンプレート名
     */
    @GetMapping("/sets/new")
    public String showNewSetForm() {

        return "set-form";
    }


/**
 * 入力されたSET情報と動画ファイルを保存する。
 *
 * @param danceSet 登録するSET情報
 * @param video アップロードされた動画ファイル
 * @return 登録後の一覧画面へのリダイレクト先
 * @throws IOException 動画ファイルの保存に失敗した場合
 */
@PostMapping("/sets")
public String createSet(
        DanceSet danceSet,
        @RequestParam("video") MultipartFile video) throws IOException {

    if (!video.isEmpty()) {
        String originalFilename = video.getOriginalFilename();

        String extension = "";
        if (originalFilename != null) {
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex >= 0) {
                extension = originalFilename.substring(dotIndex)
                        .toLowerCase(Locale.ROOT);
            }
        }

        Set<String> allowedExtensions =
                Set.of(".mp4", ".mov", ".m4v", ".webm");

        if (!allowedExtensions.contains(extension)) {
            return "redirect:/sets/new?error=invalidVideo";
        }

        Path uploadDir = Paths.get("uploads/videos")
                .toAbsolutePath()
                .normalize();

        Files.createDirectories(uploadDir);

        String fileName = UUID.randomUUID() + extension;
        Path filePath = uploadDir.resolve(fileName).normalize();

        if (!filePath.startsWith(uploadDir)) {
            throw new IOException("Invalid video file path");
        }

        video.transferTo(filePath);
        danceSet.setVideoUrl("/videos/" + fileName);
    }

    danceSetRepository.save(danceSet);
    return "redirect:/sets";
}

    /**
     * 指定されたIDのSET詳細を表示する。
     *
     * @param id 表示するSETのID
     * @param model ビューに渡す表示データ
     * @return 詳細画面、または一覧画面へのリダイレクト先
     */
    @GetMapping("/sets/{id}")
    public String showSetDetail(@PathVariable Long id, Model model) {

        Optional<DanceSet> danceSet = danceSetRepository.findById(id);

        if (danceSet.isEmpty()) {
            return "redirect:/sets";
        }

        model.addAttribute("danceSet", danceSet.get());

        return "set-detail";
    }
    
    /**
     * 指定されたSETの編集フォームを表示する。
     *
     * @param id 編集するSETのID
     * @param model ビューに渡す表示データ
     * @return 編集画面のテンプレート名
     */
    @GetMapping("/sets/{id}/edit")
    public String showEditForm(
            @PathVariable Long id,
            Model model) {

        DanceSet danceSet = danceSetRepository.findById(id)
                .orElseThrow();

        model.addAttribute("danceSet", danceSet);

        return "set-edit";
    }
    
    /**
     * 指定されたSETの名前、カテゴリー、説明を更新する。
     *
     * @param id 更新するSETのID
     * @param danceSet フォームから受け取った更新内容
     * @return 更新後の詳細画面へのリダイレクト先
     */
    @PostMapping("/sets/{id}/update")
    public String updateSet(
            @PathVariable Long id,
            DanceSet danceSet) {

        DanceSet existingSet = danceSetRepository.findById(id)
                .orElseThrow();

        existingSet.setName(danceSet.getName());
        existingSet.setCategory(danceSet.getCategory());
        existingSet.setDescription(danceSet.getDescription());

        danceSetRepository.save(existingSet);

        return "redirect:/sets/" + id;
    }
    
    /**
     * 指定されたSETを削除する。
     *
     * @param id 削除するSETのID
     * @return 削除後の一覧画面へのリダイレクト先
     */
    @PostMapping("/sets/{id}/delete")
    public String deleteSet(@PathVariable Long id) {

        danceSetRepository.deleteById(id);

        return "redirect:/sets";
    }
}