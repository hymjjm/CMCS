package com.example.utils.Cluster;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.Arrays;

public class KDistancePlotter {

    /**
     * 绘制 k-distance 图
     *
     * @param distances k-distance 值数组
     */
    public static void plot(double[] distances) {
        // 对距离数组进行排序
        Arrays.sort(distances);
        // 创建 XYSeries 对象，用于存储数据点
        XYSeries series = new XYSeries("k-distance");

        // 添加数据点到 series 中
        for (int i = 0; i < distances.length; i++) {
            series.add(i + 1, distances[i]);
        }

        // 创建 XYSeriesCollection 对象，用于存储 XYSeries 数据
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        // 创建 JFreeChart 对象，用于绘制图表***********************************************************
        JFreeChart chart = ChartFactory.createXYLineChart(
                "k-Distance Plot", // 图表标题
                "Data Point Index", // x 轴标签
                "Distance", // y 轴标签
                dataset // 数据集
        );
        // 设置 Y 轴的范围为 0 到 3000000
        chart.getXYPlot().getRangeAxis().setRange(0, 100000);
        // 创建 ChartPanel 对象，用于显示图表
        ChartPanel chartPanel = new ChartPanel(chart);

        // 创建 JFrame 对象，用于显示图形界面
        JFrame frame = new JFrame("k-Distance Plot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(chartPanel);
        frame.pack();
        frame.setVisible(true);
        // 等待用户手动关闭窗口
        while (true) {
            try {
                Thread.sleep(1000); // 每秒检查一次窗口是否关闭
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
