package com.tobi.MusicLearn_Studio_Backend.common.service;

import com.tobi.MusicLearn_Studio_Backend.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseService<REQUEST, RESPONSE> {

    /**
     * Tạo mới một entity
     */
    RESPONSE create(REQUEST request);

    /**
     * Tạo nhiều entities cùng lúc
     */
    List<RESPONSE> createMany(List<REQUEST> requests);

    /**
     * Lấy entity theo ID
     */
    RESPONSE findById(String id);

    /**
     * Lấy tất cả entities
     */
    List<RESPONSE> findAll();

    /**
     * Lấy entities với phân trang
     */
    PageResponse<RESPONSE> findAll(Pageable pageable);

    /**
     * Cập nhật entity theo ID
     */
    RESPONSE update(String id, REQUEST request);

    /**
     * Cập nhật nhiều entities theo danh sách IDs
     */
    List<RESPONSE> updateMany(List<String> ids, REQUEST request);

    /**
     * Xóa entity theo ID (soft delete)
     */
    void delete(String id);

    /**
     * Xóa nhiều entities theo danh sách IDs (soft delete)
     */
    void deleteMany(List<String> ids);

    /**
     * Xóa vĩnh viễn entity theo ID (hard delete)
     */
    void hardDelete(String id);

    /**
     * Kiểm tra entity có tồn tại không
     */
    boolean exists(String id);
}
