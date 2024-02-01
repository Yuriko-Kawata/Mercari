package com.example.productmanagementex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagementex.repository.ImageRepository;

/**
 * imageのserviceクラス
 * 
 * @author hiraizumi
 */
@Transactional
@Service
public class ImageService {

    @Autowired
    ImageRepository repository;

    /**
     * 画像のpath保管
     * 
     * @param itemId item id
     * @param path   path
     */
    public void storage(int itemId, String path) {
        repository.insert(itemId, path);
    }

    /**
     * pathの取得
     * 
     * @param itemId item id
     * @return path
     */
    public String getPath(int itemId) {
        String path = repository.findPathByItemId(itemId);
        return path;
    }
}
