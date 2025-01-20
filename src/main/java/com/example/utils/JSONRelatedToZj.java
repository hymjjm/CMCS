package com.example.utils;

import java.io.*;
import java.util.*;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class JSONRelatedToZj {
    public static void main(String[] args) {
        try {
            // 读取JSON文件
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File("E:\\jjm\\work\\佐剂\\experimental-sl.json"));
            JsonNode nodesNode = rootNode.path("nodes");
            JsonNode linksNode = rootNode.path("links");

            // 读取Excel文件中Related Genesymbol列的值
//            Set<String> relatedGenes = readExcelRelatedGeneSymbols("E:\\jjm\\work\\佐剂\\genelist0327.xlsx");
            // 读取txt文件中的数据
            Set<String> relatedGenes = readTxtRelatedGeneSymbols("E:\\jjm\\work\\rdk\\TF_diff.txt");

            // 创建新的JSON对象以保存符合条件的节点和链接
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode filteredNodes = mapper.createArrayNode();
            ArrayNode filteredLinks = mapper.createArrayNode();

            // 将与Excel中基因列表匹配的节点的颜色设置为绿色，并记录下节点ID
            Set<String> greenNodeIds = new HashSet<>();
            for (JsonNode node : nodesNode) {
                String nodeName = node.path("name").asText();
                String nodeId = node.path("name").asText();
                if (relatedGenes.contains(nodeName)) {
                    ((ObjectNode) node).put("color", "green");
                    greenNodeIds.add(nodeId);
                    filteredNodes.add(node);
                }

            }
//            // 输出greenNodeIds中存储的所有节点的ID
//            System.out.println("Green Node colors:");
//            for (JsonNode node : filteredNodes) {
//                String color = node.path("color").asText();
//                System.out.println(color);
//            }
            // 输出并保存为txt格式
            BufferedWriter writer = new BufferedWriter(new FileWriter("E:\\jjm\\work\\rdk\\TF_diff_.txt"));
            for (String nodeId : greenNodeIds) {
                writer.write(nodeId);
                writer.newLine();
            }
            writer.close();



            // 保留与Excel中基因列表匹配的链接，并记录下链接的起始节点和目标节点
            Set<String> linkedNodeIds = new HashSet<>();
            for (JsonNode link : linksNode) {
                String sourceId = link.path("source").asText();
                String targetId = link.path("target").asText();
                if (greenNodeIds.contains(sourceId) || greenNodeIds.contains(targetId)) {
                    filteredLinks.add(link);
                    linkedNodeIds.add(sourceId);
                    linkedNodeIds.add(targetId);
                }
            }
//            // 输出linkedNodeIds中存储的所有节点的ID
//            System.out.println("Linked Node IDs:");
//            for (String nodeId : linkedNodeIds) {
//                System.out.println(nodeId);
//            }

            // 根据保留的链接更新节点颜色，保留匹配的节点
            for (JsonNode node : nodesNode) {
                String nodeId = node.path("name").asText();
//                String nodeName = node.path("name").asText();
                if (linkedNodeIds.contains(nodeId)) {
                    // 如果节点的ID在linkedNodeIds中出现
                    if (!greenNodeIds.contains(nodeId)) {
                        // 如果节点不在filteredNodes中，则将其添加到filteredNodes中
                        // 将节点的颜色设置为蓝色
                        ((ObjectNode) node).put("color", "darkblue");
                        filteredNodes.add(node);
                    }
                }
            }
            System.out.println("All Node colors:");
            for (JsonNode node : filteredNodes) {
                String color = node.path("color").asText();
                System.out.println(color);
            }

            // 将新的JSON对象写入文件
            ObjectNode newRoot = mapper.createObjectNode();
            newRoot.set("nodes", filteredNodes);
            newRoot.set("links", filteredLinks);
            // 使用writerWithDefaultPrettyPrinter()方法添加换行符和缩进
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("E:\\jjm\\work\\rdk\\experimental-sl-TF.json"), newRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // 读取JSON文件
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File("E:\\jjm\\work\\rdk\\experimental-sl-TF.json"));
            JsonNode nodesNode = rootNode.path("nodes");
            JsonNode linksNode = rootNode.path("links");

            // 计算节点的连接数量
            Map<String, Integer> nodeConnectionCount = calculateNodeConnectionCount(linksNode);

            // 创建新的JSON对象以保存更新后的节点信息
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode updatedNodes = mapper.createArrayNode();

            // 更新节点的value属性
            for (JsonNode node : nodesNode) {
                String nodeId = node.path("name").asText();
                int connectionCount = nodeConnectionCount.getOrDefault(nodeId, 0);
                ((ObjectNode) node).put("value", connectionCount);
                updatedNodes.add(node);
            }

            // 将更新后的节点信息写入文件
            ObjectNode newRoot = mapper.createObjectNode();
            newRoot.set("nodes", updatedNodes);
            newRoot.set("links", linksNode); // 链接信息不变
            // 使用writerWithDefaultPrettyPrinter()方法添加换行符和缩进
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("E:\\jjm\\work\\rdk\\experimental-sl-TF.json"), newRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//读excel
    private static Set<String> readExcelRelatedGeneSymbols(String filePath) throws IOException, InvalidFormatException {
        Set<String> relatedGenes = new HashSet<>();
        Workbook workbook = WorkbookFactory.create(new File(filePath));
        Sheet sheet = workbook.getSheetAt(0); // 假设相关基因在第一个工作表中
        DataFormatter dataFormatter = new DataFormatter();
        for (Row row : sheet) {
            Cell cell = row.getCell(0); // 假设相关基因在第一列
            if (cell != null) {
                String cellValue = dataFormatter.formatCellValue(cell);
//                System.out.println("读取到基因: " + cellValue);
                relatedGenes.add(cellValue);
            }
        }
        workbook.close();
        return relatedGenes;
    }
//读txt
private static Set<String> readTxtRelatedGeneSymbols(String filePath) throws IOException {
    Set<String> relatedGenes = new HashSet<>();
    BufferedReader reader = new BufferedReader(new FileReader(filePath));
    String line;
    while ((line = reader.readLine()) != null) {
        relatedGenes.add(line.trim());
    }
    reader.close();
    return relatedGenes;
}

    private static Map<String, Integer> calculateNodeConnectionCount(JsonNode linksNode) {
        Map<String, Integer> nodeConnectionCount = new HashMap<>();
        // 遍历链接信息，统计每个节点的连接数量
        for (JsonNode link : linksNode) {
            String sourceId = link.path("source").asText();
            String targetId = link.path("target").asText();
            // 更新源节点的连接数量
            nodeConnectionCount.put(sourceId, nodeConnectionCount.getOrDefault(sourceId, 0) + 1);
            // 更新目标节点的连接数量
            nodeConnectionCount.put(targetId, nodeConnectionCount.getOrDefault(targetId, 0) + 1);
        }
        return nodeConnectionCount;
    }


}

