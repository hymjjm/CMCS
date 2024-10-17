package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.utils.Cluster.DBSCANUtil;
import com.example.utils.Cluster.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Set;

import static com.example.utils.Cluster.SetPointClusterUtils.assignClusterLabels;

/**
 * <p>
 *  聚类分析供前端接口调用
 * </p>
 *
 * @author jjm
 * @since 2024-04-3
 */
@CrossOrigin
@RestController
@RequestMapping("/Cluster")
public class ClusterController {

    @Autowired
    private IJjmBrcaPd4103aService jjmBrcaPd4103aService;

    @GetMapping("/MutCluster")
    public List<Set<Point>> MutCluster(@RequestParam String Chromosome){
        List<JjmBrcaPd4103a> JjmBrcaMaf = selectByChromosomeAndSortByStartPosition(Chromosome);
       List<Set<Point>> clusters = DBSCANUtil.RunMutCluster(JjmBrcaMaf,1414, 2);

        return assignClusterLabels(clusters);

    }


    public List<JjmBrcaPd4103a> selectByChromosomeAndSortByStartPosition(String chr) {
        QueryWrapper<JjmBrcaPd4103a> queryWrapper = new QueryWrapper<>();
        // 如果提供了样本信息，则添加样本条件

            queryWrapper.eq("chromosome", chr).orderByAsc("genome_start");;

        return jjmBrcaPd4103aService.list(queryWrapper);
    }


}
