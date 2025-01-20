package com.example.utils.Cluster;

import java.util.List;
import java.util.Set;

public class SilhouetteCoefficient {

    public static double calculate(List<Set<Point>> clusters) {
        double totalSilhouette = 0.0;
        int totalSamples = 0;

        // 计算每个样本的轮廓系数
        int numClusters = clusters.size();
        for (int i = 0; i < numClusters - 1; i++) { // 不包括最后一个簇
            Set<Point> cluster = clusters.get(i);
            if (cluster.size() <= 1) { // 排除只有一个样本的簇
                continue;
            }
            for (Point point : cluster) {
                double silhouette = calculateSilhouette(point, cluster, clusters);
                totalSilhouette += silhouette;
                totalSamples++;
            }
        }

        // 计算平均轮廓系数
        double averageSilhouette = totalSilhouette / totalSamples;
        return averageSilhouette;
    }

    private static double calculateSilhouette(Point point, Set<Point> cluster, List<Set<Point>> clusters) {
        double a = calculateAverageDistance(point, cluster); // 计算样本到同一簇内其他样本的平均距离（紧密度）

        // 计算样本到其他簇的平均距离（分离度）
        double b = Double.MAX_VALUE;
        for (Set<Point> otherCluster : clusters) {
            if (otherCluster != cluster) {
                double averageDistanceToOtherCluster = calculateAverageDistance(point, otherCluster);
                b = Math.min(b, averageDistanceToOtherCluster);
            }
        }

        // 计算轮廓系数
        double silhouette;
        if (a < b) {
            silhouette = 1 - (a / b);
        } else if (a > b) {
            silhouette = (b / a) - 1;
        } else {
            silhouette = 0; // 当 a 等于 b 时，轮廓系数为 0
        }
        return silhouette;
    }

    private static double calculateAverageDistance(Point point, Set<Point> cluster) {
        double totalDistance = 0.0;
        for (Point otherPoint : cluster) {
            totalDistance += distance(point, otherPoint);
        }
        return totalDistance / (cluster.size() - 1); // 减去自身到自身的距离
    }

    private static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
}
