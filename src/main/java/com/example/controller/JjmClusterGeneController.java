package com.example.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import com.example.service.IJjmClusterGeneService;
import com.example.entity.JjmClusterGene;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jjm
 * @since 2024-06-12
 */
@RestController
@CrossOrigin
@RequestMapping("/jjm-cluster-gene")
public class JjmClusterGeneController {

    @Resource
    private IJjmClusterGeneService jjmClusterGeneService; //做@autowired service  的模板


    @GetMapping("/findAll")
    public List<JjmClusterGene> findAll() {
        //查询所有
//        return jjmClusterGeneService.list();
        return jjmClusterGeneService.Redislist();
        }




}

