package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.JjmClusterUpload;
import com.example.service.impl.JjmClusterUploadServiceImpl;
import com.example.utils.Cluster.DBSCANUtilPlus;
import com.example.utils.Cluster.Point;
import com.example.utils.UserHolder;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.utils.Cluster.SetPointClusterUtils.assignClusterLabels;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jjm
 * @since 2024-04-11
 */
@CrossOrigin
@RestController
@RequestMapping("/jjm-cluster-upload")
public class JjmClusterUploadController {

    @Resource
    private JjmClusterUploadServiceImpl jjmClusterUploadService; //做@autowired service  的模板

    @GetMapping("/cancerType")
    public List<String> selectByCancerType() {
        QueryWrapper<JjmClusterUpload> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT cancer_type");
        return jjmClusterUploadService.list(queryWrapper).stream()
                .map(JjmClusterUpload::getCancerType)
                .collect(Collectors.toList());
    }
    @GetMapping("/Sample")
    public List<String> selectBySample() {
        QueryWrapper<JjmClusterUpload> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT tumor_sample");
        return jjmClusterUploadService.list(queryWrapper).stream()
                .map(JjmClusterUpload::getTumorSample)
                .collect(Collectors.toList());
    }
    @GetMapping("/ReferenceGenomes")
    public List<String> selectByReferenceGenomes() {
        QueryWrapper<JjmClusterUpload> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT reference_genomes");
        return jjmClusterUploadService.list(queryWrapper).stream()
                .map(JjmClusterUpload::getReferenceGenomes)
                .collect(Collectors.toList());
    }

    //  分页查询 表格用
    @GetMapping("/selectPage")
    public IPage<JjmClusterUpload> FindAllData(@RequestParam Integer pageNum,
                                                 @RequestParam Integer pageSize){
        //参数一写当前页，默认为1；参数二显示记录数 默认值10
        IPage<JjmClusterUpload> page = new Page<>(pageNum,pageSize);
        return jjmClusterUploadService.page(page);

    }

//    用来去重所有数据
    @PostMapping("/deduplicateData")
    public void deduplicateData(){
        jjmClusterUploadService.deduplicateData();
    }

    //    用来删除所选条件数据
    @PostMapping("/delete")
    public void delete(@RequestParam String nickName,
                       @RequestParam String sample,
                       @RequestParam String cancerType,
                       @RequestParam String GR){
        jjmClusterUploadService.deleteByConditions(nickName,sample, cancerType, GR);
    }

    @PostMapping("/import")
    public String importExcel(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return "Please select a file to upload.";
            }
            List<JjmClusterUpload> dataList ;
            // 检查文件类型
            String originalFilename = file.getOriginalFilename();
            if(originalFilename.endsWith(".TSV") || originalFilename.endsWith(".tsv")){
                // 读取Excel文件并解析成实体列表
                 dataList = parseTSV(file);
            }else if(originalFilename.endsWith(".txt") || originalFilename.endsWith(".TXT")){
                // 读取Excel文件并解析成实体列表
                 dataList = parseTxt(file);
            }else {
                return "Only TSV files (.TSV or .tsv) and TXT files (.txt or .TXT) are allowed.";
            }

            // 调用Service进行数据导入
            jjmClusterUploadService.importDataFromExcel(dataList);
            return "Data imported successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to import data.";
        }
    }

    private List<JjmClusterUpload> parseExcel(MultipartFile file) throws IOException, InvalidFormatException {
        List<JjmClusterUpload> dataList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                // 获取 GeneSymbol 并检查是否为空
                String geneSymbol = getStringValue(row.getCell(0));
                if (geneSymbol == null || geneSymbol.trim().isEmpty()) continue;

                JjmClusterUpload entity = new JjmClusterUpload();
                entity.setGeneSymbol(getStringValue(row.getCell(0)));
                entity.setChromosome(getStringValue(row.getCell(1)));
                entity.setGenomeStart((int)row.getCell(2).getNumericCellValue());
                entity.setWtAllele(getStringValue(row.getCell(3)));
                entity.setMutAllele(getStringValue(row.getCell(4)));
                entity.setStrand(getStringValue(row.getCell(5)));
                entity.setMutDescription(getStringValue(row.getCell(6)));
                entity.setTumorSample(getStringValue(row.getCell(7)));
                entity.setCancerType(getStringValue(row.getCell(8)));
                entity.setReferenceGenomes(getStringValue(row.getCell(9)));
                entity.setContext(getStringValue(row.getCell(10)));

                entity.setNickName(UserHolder.getUser().getNickName());
                // 设置ID
                String id = entity.getGeneSymbol() + entity.getChromosome() + entity.getGenomeStart() + entity.getTumorSample() + entity.getCancerType() + entity.getReferenceGenomes();
                entity.setId(id);


                dataList.add(entity);
            }
        }

        return dataList;
    }
    private String getStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }

    //    读取tsv格式
    private List<JjmClusterUpload> parseTSV(MultipartFile file) throws IOException {
        List<JjmClusterUpload> dataList = new ArrayList<>();

        // 使用 BufferedReader 逐行读取 TSV 文件内容
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) continue; // 跳过表头

                String[] fields = line.split("\t"); // 按制表符分割
                if (fields.length < 11) {
                    // 如果字段数不足，跳过当前行
                    continue;
                }

                JjmClusterUpload entity = new JjmClusterUpload();
                entity.setGeneSymbol(fields[0].trim());
                entity.setChromosome(fields[1].trim());
                entity.setGenomeStart(Integer.parseInt(fields[2].trim()));
                entity.setWtAllele(fields[3].trim());
                entity.setMutAllele(fields[4].trim());
                entity.setStrand(fields[5].trim());
                entity.setMutDescription(fields[6].trim());
                entity.setTumorSample(fields[7].trim());
                entity.setCancerType(fields[8].trim());
                entity.setReferenceGenomes(fields[9].trim());
                entity.setContext(fields[10].trim());

                // 设置 ID
                String id = new StringBuilder()
                        .append(fields[0].trim())
                        .append(fields[1].trim())
                        .append(fields[2].trim())
                        .append(fields[7].trim())
                        .append(fields[8].trim())
                        .append(fields[9].trim())
                        .toString();
                entity.setId(id);

                dataList.add(entity);
            }
        }

        return dataList;
    }

    //    读取txt格式
private List<JjmClusterUpload> parseTxt(MultipartFile file) throws IOException {
    List<JjmClusterUpload> dataList = new ArrayList<>();

    try (InputStream inputStream = file.getInputStream();
         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

        String line;
        boolean isFirstLine = true;

        while ((line = reader.readLine()) != null) {
            if (isFirstLine) {
                isFirstLine = false;
                continue; // 跳过首行（假设首行为标题行）
            }

            String[] columns = line.split("\t"); // 假设是以制表符分隔的文件，若是逗号分隔则用","替代"\t"
            if (columns.length < 6) continue; // 确保每行有足够的列
            String geneSymbol = columns[0];
            if (geneSymbol == null || geneSymbol.trim().isEmpty()) continue;

            JjmClusterUpload entity = new JjmClusterUpload();
            entity.setGeneSymbol(columns[0]);
            entity.setChromosome(columns[1]);
            entity.setGenomeStart(Integer.parseInt(columns[2]));
            entity.setWtAllele(columns[3]);
            entity.setMutAllele(columns[4]);
            entity.setStrand(columns[5]);
            entity.setMutDescription(columns[6]);
            entity.setTumorSample(columns[7]);
            entity.setCancerType(columns[8]);
            entity.setReferenceGenomes(columns[9]);
            entity.setContext(columns[10]);
            entity.setNickName(UserHolder.getUser().getNickName());
            // 设置ID
            String id = entity.getGeneSymbol() + entity.getChromosome() + entity.getGenomeStart() + entity.getTumorSample() + entity.getCancerType() + entity.getReferenceGenomes();
            entity.setId(id);

            dataList.add(entity);
        }
    }

    return dataList;
}

    @GetMapping("/MutCluster")
    public List<Set<Point>> MutCluster(@RequestParam int eps,
                                       @RequestParam String Chromosome,
                                       @RequestParam String CancerType,
                                       @RequestParam String Sample,
                                       @RequestParam String ReferenceGenomes,
                                       @RequestParam(required = false) String nickname){
        List<JjmClusterUpload> JjmBrcaMaf = selectByChromosomeAndSortByStartPosition(Chromosome, CancerType, Sample, ReferenceGenomes,nickname);
        List<Set<Point>> clusters = DBSCANUtilPlus.RunMutCluster(JjmBrcaMaf,eps*Math.sqrt(2), 2);
        return assignClusterLabels(clusters);
    }
    public List<JjmClusterUpload> selectByChromosomeAndSortByStartPosition(String chr,String CancerType, String Sample,String ReferenceGenomes,String nickname) {
        QueryWrapper<JjmClusterUpload> queryWrapper = new QueryWrapper<>();
        // 如果提供了样本信息，则添加样本条件
        queryWrapper.eq("chromosome", chr)
                .eq("cancer_type",CancerType)
                .eq("tumor_sample",Sample)
                .eq("reference_genomes",ReferenceGenomes);
        // 如果 nickname 不为空，则添加 nickname 条件
        if (nickname != null && !nickname.isEmpty()) {
            queryWrapper.eq("nick_name", nickname);
        }
        // 按基因组起始位置排序
        queryWrapper.orderByAsc("genome_start");

        List<JjmClusterUpload> resultList = jjmClusterUploadService.list(queryWrapper);
        // 去重操作，使用 Set 来存储不重复的结果
//        Set<JjmClusterUpload> resultSet = new LinkedHashSet<>(resultList);

        // 将 Set 转换为 List 并返回
//        return new ArrayList<>(resultSet);
        return resultList;

    }

}

