package com.example.mapper;

import com.example.entity.JjmClusterUpload;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jjm
 * @since 2024-04-11
 */
public interface JjmClusterUploadMapper extends BaseMapper<JjmClusterUpload> {

    void deleteTempTable();

    void createTempTable();

    void truncateOriginalTable();

    void copyDataFromTempTable();

    void dropTempTable();

//    boolean insertOrUpdateBatch(@Param("entities") Collection<JjmClusterUpload> JjmClusterUpload);


}
