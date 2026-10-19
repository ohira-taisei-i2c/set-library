package com.example.setlibrary.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
/**
 * 動画ファイルなどの静的リソース配信設定を行う。
 */
public class WebConfig implements WebMvcConfigurer {

    /**
     * アップロード済み動画をURLから参照できるようにリソースハンドラーを登録する。
     *
     * @param registry 静的リソースのハンドラーを登録するレジストリ
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/videos/**")
                .addResourceLocations("file:uploads/videos/");
    }
}