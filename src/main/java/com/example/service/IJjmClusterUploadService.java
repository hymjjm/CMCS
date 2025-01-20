package com.example.service;

import com.example.dto.Result;
import com.example.entity.JjmClusterUpload;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jjm
 * @since 2024-04-11
 */
public interface IJjmClusterUploadService extends IService<JjmClusterUpload> {

    //删除
    @Transactional(rollbackFor = Exception.class)
    Result deleteByConditions(String nickName, String tumorSample, String cancerType, String referenceGenomes);
}
