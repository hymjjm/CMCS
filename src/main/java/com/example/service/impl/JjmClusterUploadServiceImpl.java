package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dto.Result;
import com.example.entity.JjmClusterUpload;
import com.example.mapper.JjmClusterUploadMapper;
import com.example.service.IJjmClusterUploadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.utils.UserHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jjm
 * @since 2024-04-11
 */
@Service
@Transactional
public class JjmClusterUploadServiceImpl extends ServiceImpl<JjmClusterUploadMapper, JjmClusterUpload> implements IJjmClusterUploadService {

    public void importDataFromExcel(List<JjmClusterUpload> dataList) {
        saveBatch(dataList); // MyBatis Plus会自动将数据批量插入到数据库中
//        saveOrUpdateBatch(dataList);
//        saveOrUpdateBatch(dataList);
    }

        @Resource
    private JjmClusterUploadMapper jjmClusterUploadMapper;

//    去重
    @Transactional(rollbackFor = Exception.class)
    public void deduplicateData() {
        jjmClusterUploadMapper.createTempTable();
        jjmClusterUploadMapper.truncateOriginalTable();
        jjmClusterUploadMapper.copyDataFromTempTable();
        jjmClusterUploadMapper.dropTempTable();
    }

    //删除
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteByConditions(String nickName, String tumorSample, String cancerType, String referenceGenomes) {

        if (nickName.equals(UserHolder.getUser().getNickName())) {
            QueryWrapper<JjmClusterUpload> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tumor_sample", tumorSample)
                    .eq("cancer_type", cancerType)
                    .eq("reference_genomes", referenceGenomes);

            // 执行删除操作
            this.remove(queryWrapper);
            return Result.ok();
        }else{
            return Result.fail("Unable to delete data uploaded by others");
        }

    }
//
////    批量插入或更新
//    @Override
//    public boolean saveOrUpdateBatch(Collection<JjmClusterUpload> entityList) {
//        return jjmClusterUploadMapper.insertOrUpdateBatch(entityList);
//}
}
