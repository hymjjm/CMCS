package com.example.utils.Cluster;

import java.util.*;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class SetPointClusterUtils {
    public static List<Set<Point>> assignClusterLabels(List<Set<Point>> clusters) {
        int kataegisCount = 0;
        int omikliCount = 0;
        int DBSCount = 0;
        int MBSCount = 0;

        for (int i = 0; i < clusters.size(); i++) {
            Set<Point> cluster = clusters.get(i);

            if (cluster.size() > 4) {
                if(i == clusters.size()-1){
                    for (Point point : cluster) {
                        point.setCluster("NoCluster" );
                    }
                }else{
                    ++kataegisCount;
                    for (Point point : cluster) {
                        point.setCluster("K " + kataegisCount);
                        point.setSize(cluster.size());
                    }
                }

            } else if (cluster.size() <= 4 && cluster.size() > 1) {
                if(i == clusters.size()-1){
                    for (Point point : cluster) {
                        point.setCluster("NoCluster" );
                    }
                }else {
                    ++omikliCount;
                    for (Point point : cluster) {
                        point.setCluster("O " + omikliCount);
                        point.setSize(cluster.size());
                    }
                }
            }
        }

        return assignMBSDBS(clusters,DBSCount,MBSCount);
    }

    public static List<Set<Point>> assignMBSDBS(List<Set<Point>> clusters,  int DBSCount, int MBSCount ) {
        List<Set<Point>> newClusters = new ArrayList<>();

        for (Set<Point> cluster : clusters) {
            boolean continuousZeroY = true;
            Set<Point> newCluster = new HashSet<>();

            // 检查是否存在连续的y为1的点
            for (Point point : cluster) {
                if (point.getY() != 1 && point.getY2() != 1) {
                    continuousZeroY = false;
                    break;
                }
            }

            if (continuousZeroY) {
                // 如果所有点都是连续的y为1的点
                for (Point point : cluster) {
                    if (cluster.size() == 2) {
                        point.setCluster("DBS " + DBSCount);
                    } else if (cluster.size() > 2) {
                        point.setCluster("MBS " + MBSCount);
                    }
                    point.setSize(cluster.size());
                }
                if (cluster.size() == 2) {
                    ++DBSCount;
                } else if (cluster.size() > 2) {
                    ++MBSCount;
                }
            } else {
                // 存在非连续的y为1的点，进行分析
                List<Point> sortedCluster = new ArrayList<>(cluster);
                Collections.sort(sortedCluster, Comparator.comparingDouble(Point::getX)); // 按照 x 值排序
                Point prevPoint = null;
                for (Point point : sortedCluster) {
                    System.out.println("prevPoint:"+prevPoint);
                    //只要当前点的前一个点y和前一个点的后一个点y2相等都是1，说明连续
                    if (prevPoint != null && point.getY() == 1 && prevPoint.getY2() == 1) {//判断当前点point和前一个点连续！！！！！！！！！！！！！！！！
                        newCluster.add(new Point(prevPoint)); // 创建新的点对象并添加到 newCluster 中
                        newCluster.add(new Point(point)); // 创建新的点对象并添加到 newCluster 中
                        System.out.println("newCluster:"+newCluster);
                    } else if (prevPoint != null && !newCluster.isEmpty()) { //连续中断时

                        if (newCluster.size() == 2) {
                            for (Point p : newCluster) {
                                p.setCluster("DBS " + DBSCount);
                                p.setSize(newCluster.size());
                            }
                            ++DBSCount;
                        } else if (newCluster.size() > 2) {
                            for (Point p : newCluster) {
                                p.setCluster("MBS " + MBSCount);
                                p.setSize(newCluster.size());
                            }
                            ++MBSCount;
                        }
                        newClusters.add(newCluster);
                        System.out.println("1"+newClusters);
                        newCluster = new HashSet<>();
                    }
                    prevPoint = point;//前一个点
                }

                // 处理最后一个连续的y为1的点集
                if (!newCluster.isEmpty()) {
                    if (newCluster.size() == 2) {
                        for (Point p : newCluster) {
                            p.setCluster("DBS " + DBSCount);
                            p.setSize(newCluster.size());
                        }
                        ++DBSCount;
                    } else if (newCluster.size() > 2) {
                        for (Point p : newCluster) {
                            p.setCluster("MBS " + MBSCount);
                            p.setSize(newCluster.size());
                        }
                        ++MBSCount;
                    }
                    newClusters.add(newCluster);
                    System.out.println("2"+newClusters);
                }
            }
        }
        System.out.println("3"+newClusters);
        clusters.addAll(0,newClusters);
        System.out.println("clusters："+clusters);
        return clusters;
    }
}
