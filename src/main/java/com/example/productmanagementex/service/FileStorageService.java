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

@Service
public class FileStorageService {

    private static final Logger logger = LogManager.getLogger(FileStorageService.class);
    private final String uploadDir = "uploaded-img";

    public String storeFile(MultipartFile file) {
        logger.debug("Started storeFile");

        try {
            // 空のファイル名をチェック
            if (file.isEmpty()) {
                throw new IllegalStateException("ファイルが空です。");
            }

            // ファイル名の取得
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            // ファイルパスの構築
            Path targetLocation = Paths.get(uploadDir).resolve(fileName);

            // ファイルの保存
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 保存したファイルのパスを返す
            logger.debug("Finished storeFile");
            return targetLocation.toString();

        } catch (IOException ex) {
            throw new IllegalStateException("ファイルの保存に失敗しました。", ex);
        }
    }
}
