package com.example.utils.Cluster;

import org.springframework.stereotype.Component;

import java.util.*;

@Component // 声明为Spring组件，让Spring容器管理
public class DBSCANUtil {



    /**
     * 用来算k-distance里的y坐标值用的——>distance
     * */
    public static double[] calculateDistances(List<Point> points, int k) {
        double[] distances = new double[points.size()];
        for (int i = 0; i < points.size(); i++) {
            Point current = points.get(i);
            List<Double> pointDistances = new ArrayList<>();
            for (Point other : points) {
                if (!current.equals(other)) {
                    double distance = Math.sqrt(Math.pow(current.x - other.x, 2) + Math.pow(current.y - other.y, 2));
                    pointDistances.add(distance);
                }
            }
            Collections.sort(pointDistances);
            distances[i] = pointDistances.get(k - 1);
        }
        return distances;
    }


}


/**
 * 主要核心：聚类算法类
 * */
class DBSCAN {
    List<Point> points;
    double eps;
    int minPts;
    List<Set<Point>> clusters; // 存储簇
    Map<Point, Set<Point>> densityReachable;
    Set<Point> visited = new HashSet<>();

    public DBSCAN(List<Point> points, double eps, int minPts) {
        this.points = points;
        this.eps = eps;
        this.minPts = minPts;
        this.clusters = new ArrayList<>();
        this.densityReachable  = new HashMap<>(); // 核心点以及它的邻域
    }

    // 执行 聚类方法并返回聚类结果
    public List<Set<Point>> dbscan() {
//        Set<Point> allNeighbors = new HashSet<>();
        for (Point point : points) {
            Set<Point> neighbors = regionQuery(point); // 查询点的邻域
            if (neighbors.size() >= minPts) {
                densityReachable.put(point, neighbors);
//                allNeighbors.addAll(neighbors);
            }
        }

//       递归调用，直接完成聚类
        for(Point i:densityReachable.keySet() ){
            if(!visited.contains(i)){
                Set<Point> cluster = new HashSet<>();
//                System.out.println("Starting new cluster with core point:______________________________ " );
//                System.out.println("Starting new cluster with core point: " + i);
                 cluster = buildDensityReachableGraph(i,cluster);
                clusters.add(cluster);
            }
        }
        Set<Point> nocluster = new HashSet<>();//把没有聚类的也合为一类
//        for(Point i: points){
//            if(!allNeighbors.contains(i)){
//                nocluster.add(i);
//            }
//        }
        clusters.add(nocluster);

        return clusters;
    }
    // 直接使用递归调用完成聚类
    private Set<Point> buildDensityReachableGraph(Point corePoint,Set<Point> cluster) {
        visited.add(corePoint);
//        cluster.add(corePoint);

            Set<Point> neighborsPoints = densityReachable.get(corePoint);//取到的是当前核心点的邻居点集合（包含corePoint）

            for (Point neighborsPoint : neighborsPoints) {
                if (!visited.contains(neighborsPoint) &&neighborsPoint != corePoint && densityReachable.containsKey(neighborsPoint)) {
                    // 如果reachablePoint是另一个核心点，且与corePoint密度可达，则将corePoint添加到reachablePoint的密度可达列表中
                    buildDensityReachableGraph(neighborsPoint,cluster);
                    cluster.addAll(densityReachable.get(neighborsPoint)) ;
                }
            }
        return cluster;
    }
    // 查询给定点的邻域
    private Set<Point> regionQuery(Point point) {
        Set<Point> neighbors = new HashSet<>();
        for (Point p : points) {
            if (distance(point, p) <= eps) {
                neighbors.add(p);
            }
        }
        neighbors.add(point);
//        System.out.println("给定点的领域————————————————————————————————————："+neighbors);
        return neighbors;
    }

    // 计算两点之间的欧氏距离
    private double distance(Point p1, Point p2) {
        return Math.min(Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2)),Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y2 - p2.y2, 2))) ;
    }

}

