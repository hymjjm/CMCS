package com.example.utils.Cluster;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class KDistanceSlope {

    /**
     * 计算 k-distance 图中最大变化斜率的点的 y 值
     *
     * @param distances k-distance 值数组
     * @return 最大变化斜率的点的 y 值
     */
    public static double findMaxYValue(double[] distances) {
        double[] slopeChanges = calculateSlopeChanges(distances);

        double maxSlopeChange = 0;
        int maxSlopeChangeIndex = 0;

        for (int i = 0; i < slopeChanges.length; i++) {
            if (slopeChanges[i] > maxSlopeChange) {
                maxSlopeChange = slopeChanges[i];
                maxSlopeChangeIndex = i;
            }
        }

        return distances[maxSlopeChangeIndex];
    }

    /**
     * 计算 k-distance 图中每个点的斜率变化
     *
     * @param distances k-distance 值数组
     * @return 每个点的斜率变化数组
     */
    private static double[] calculateSlopeChanges(double[] distances) {
        double[] slopeChanges = new double[distances.length - 1];

        for (int i = 1; i < distances.length; i++) {
            slopeChanges[i - 1] = Math.abs(distances[i] - distances[i - 1]);
        }

        return slopeChanges;
    }

}
