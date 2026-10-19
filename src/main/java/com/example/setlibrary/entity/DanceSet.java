package com.example.setlibrary.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "DANCE_SET")
/**
 * ブレイキンのSET情報を保持するエンティティ。
 */
public class DanceSet {
	
	/**
	 * 新規登録時に作成日時と更新日時を設定する。
	 */
	@PrePersist
	protected void onCreate() {
	    LocalDateTime now = LocalDateTime.now();
	    createdAt = now;
	    updatedAt = now;
	}

	/**
	 * 更新時に更新日時を現在時刻に設定する。
	 */
	@PreUpdate
	protected void onUpdate() {
	    updatedAt = LocalDateTime.now();
	}

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    
    @Column(name = "CATEGORY", nullable = false, length = 20)
    private String category;
    
    @Column(name = "DESCRIPTION", length = 500)
    private String description;
    
    @Column(name = "VIDEO_URL", length = 500)
    private String videoUrl;
    
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

}