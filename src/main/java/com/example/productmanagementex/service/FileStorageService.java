package com.example.productmanagementex.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

/**
 * imagesのServiceクラス
 * 
 * @author hiraizumi
 */
@Service
public class FileStorageService {

    private static final Logger logger = LogManager.getLogger(FileStorageService.class);
    private final String uploadDir = "uploaded-img";

    /**
     * 画像の保管
     * 
     * @param file file
     * @return path
     */
    @SuppressWarnings("null") // 警告の抑制
    public String storeFile(MultipartFile file) {
        logger.debug("Started storeFile");

        try {
            // 空のファイル名をチェック
            if (file.isEmpty()) {
                throw new IllegalStateException("ファイルが空です。");
            }

            // ファイル名の取得とクリーニング
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + "_" + originalFileName; // 一意のファイル名を生成

            // ファイルパスの構築
            Path targetLocation = Paths.get(uploadDir).resolve(fileName);

            // 重複チェックとファイルの保存
            if (!Files.exists(targetLocation)) {
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            } else {
                throw new IllegalStateException("ファイル名が重複しています。");
            }

            // 保存したファイルのパスを返す
            logger.debug("Finished storeFile");
            return targetLocation.toString();

        } catch (IOException ex) {
            throw new IllegalStateException("ファイルの保存に失敗しました。", ex);
        }
    }

    /**
     * ファイルの削除
     * 
     * @param filePath
     */
    public void deleteFile(String filePath) {
        // 受け取ったpathにあるファイルを削除
        try {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
            logger.debug("Deleted file: " + path);
        } catch (IOException e) {
            logger.error("Could not delete file: " + filePath, e);
            throw new IllegalStateException("ファイルの削除に失敗しました。", e);
        }
    }
}