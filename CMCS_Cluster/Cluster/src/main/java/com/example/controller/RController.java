package com.example.controller;

import com.example.entity.JjmClusterUpload;
import com.example.utils.R.Rservice;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/R")
public class RController {

    @Resource
    private Rservice rservice;

//    簇突变雨林图
    @PostMapping("/RunYL")
    public String importYLExcel(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return "Please select a file to upload.";
            }
            List<JjmClusterUpload> dataList ;
            // 检查文件类型
            String originalFilename = file.getOriginalFilename();
            if(originalFilename.endsWith(".xlsx") || originalFilename.endsWith(".xlsx")){
                // 读取Excel文件并解析成实体列表
               return rservice.getYLxlsx(file);
            }else if(originalFilename.endsWith(".txt") || originalFilename.endsWith(".TXT")){
                // 读取Excel文件并解析成实体列表
                return rservice.getYLxlsx(file);
            }else {
                return "Only CSV files (.CSV or .csv) and TXT files (.txt or .TXT) are allowed.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to import data.";
        }
    }
}
