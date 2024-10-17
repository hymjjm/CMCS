package com.example.utils.Cluster;

import org.springframework.stereotype.Component;

import java.util.*;

@Component // 声明为Spring组件，让Spring容器管理
public class DBSCANUtil {

    //************************************/***测试用，因为要用到图形窗口，测试完注释掉*/********************************

    //************************************/***测试用，因为要用到图形窗口，测试完注释掉*/********************************
///**
//*     RunDBSCAN：主要调用运行的方法
//*     formData: String formData = "3.275154, 2.957587\n" +
//                "-3.344465, 2.603513\n" +
//                "0.355083, -3.376585\n" +   这种类型的数据
// 输入：
//      k :  设置 k-distance 的 k 值这个在测试方法里面写
//      eps: 节点半径
//      minPts: 领域最小节点数  //这里根据k-distance 看拐点得到eps
//* */
//    public static List<Set<Point>> RunDBSCAN(String formData,double eps,int minPts){
//        // 将输入数据分割并添加到点列表中
//        List<Point> points = parseData(formData);
//
//        /**
//         * 使用 k-distance 计算 eps 的函数，并绘图*******************************
//         *            测试使用哪个eps最好的时候打开，接口封装好了就关掉
//         * */
//////        int k = 5; //  设置 k-distance 的 k 值
////        double[] distances = calculateDistances(points, k);
////        KDistancePlotter.plot(distances);
//
//        // 创建 DBSCAN 对象并执行聚类
//        DBSCAN dbscan = new DBSCAN(points, eps, minPts);
//        List<Set<Point>> clusters = dbscan.dbscan();
//
//        /**
//         *     打印聚类数量和每个聚类的点
//         * */
////        System.out.println("Number of clusters: " + clusters.size());
////        for (int i = 0; i < clusters.size(); i++) {
////            System.out.println("Cluster " + (i + 1) + ": " + clusters.get(i));
////            Set<Point> clusteri = clusters.get(i);
////            int numberOfPoints = clusteri.size();
////            System.out.println("Number of points in Cluster " + (i + 1) + ": " + numberOfPoints);
////        }
//
//        /**
//         *       计算轮廓系数
//         * */
////       double Silhouette_score = SilhouetteCoefficient.calculate(clusters);
////        System.out.println("Silhouette_score: " + Silhouette_score);
//
//        return clusters;
//
//    }
    /**
     *     RunMutCluster：主要调用运行的方法_________识别簇突变格式
     *     formData: String formData = chr1	55159655
     *                                 chr1 74571494
     *                                 chr2	215397809  这种类型的数据
     输入：
     k :  设置 k-distance 的 k 值这个在测试方法里面写
     eps: 节点半径
     minPts: 领域最小节点数  //这里根据k-distance 看拐点得到eps
     * */
    public static List<Set<Point>> RunMutCluster(List<JjmBrcaPd4103a> JjmBrcaMaf, double eps, int minPts){
        // 将输入数据分割,按第一列值为chr的进行筛选，然后对筛选好的数据按第二列进行排序，然后将第二列的数据按
        List<Point> points = parseDataMut(JjmBrcaMaf, eps);

        /**
         * 使用 k-distance 计算 eps 的函数，并绘图*******************************
         *            测试使用哪个eps最好的时候打开，接口封装好了就关掉
         * */
//        int k = 2; //  设置 k-distance 的 k 值
//        double[] distances = calculateDistances(points, k);
//        KDistancePlotter.plot(distances);
        /** 测试最佳间隔距离*/

        // 创建 DBSCAN 对象并执行聚类
        DBSCAN dbscan = new DBSCAN(points, eps, minPts);
        List<Set<Point>> clusters = dbscan.dbscan();

        /**
         *     打印聚类数量和每个聚类的点的数量
         * */
//        System.out.println("Number of clusters: " + clusters.size());
//        for (int i = 0; i < clusters.size(); i++) {
//            System.out.println("Cluster " + (i + 1) + ": " + clusters.get(i));
//            Set<Point> clusteri = clusters.get(i);
//            int numberOfPoints = clusteri.size();
//            System.out.println("Number of points in Cluster " + (i + 1) + ": " + numberOfPoints);
//        }

        /**
         *       计算轮廓系数
         * */
//       double Silhouette_score = SilhouetteCoefficient.calculate(clusters);
//        System.out.println("Silhouette_score: " + Silhouette_score);

        return clusters;

    }


    /**
     * 如果是簇突变的话，因为有染色体在所以处理数据的方式会有些不同
     * 可以换成表格输入
     *
     * 功能描述：按Chromosome列值为chr的进行筛选，然后对筛选好的数据按Start_Position列进行从小到大排序。
     * 然后将Start_Position列的数据的当前值当作x给Point，x减前一个值作为y给Point，当前值对应的Hugo_Symbol的值作为gene给Point(如果是第一行数据就是当前行减第二行)。
     * */
    public static List<Point> parseDataMut(List<JjmBrcaPd4103a> JjmBrcaMaf, double eps) {
        List<Point> points = new ArrayList<>();
        // 查询染色体为chr的数据，并按Start_Position列排序
        List<JjmBrcaPd4103a> dataList = JjmBrcaMaf;

        // 遍历查询到的数据
        for (int i = 0; i < dataList.size(); i++) {
            JjmBrcaPd4103a currentData = dataList.get(i);
            int s = currentData.getGenomeStart(); // 当前行的起始位置作为x值

            // 计算y值
            int y;
            if (i == 0) {
                // 如果是第一行数据，y值为第二行减第一行
                JjmBrcaPd4103a prevData = dataList.get(i + 1);
                int prevX = prevData.getGenomeStart();
                y = prevX - s; // 当前行与前一行的起始位置差值作为y值
            } else {
                JjmBrcaPd4103a prevData = dataList.get(i - 1);
                int prevX = prevData.getGenomeStart();
                y = s - prevX; // 当前行与前一行的起始位置差值作为y值

            }

            String gene = currentData.getGeneSymbol(); // 当前行的Hugo_Symbol作为gene值

            // 创建Point对象并添加到List中
            if(y<eps){
                points.add(new Point(s, y, gene));
            }
//               points.add(new Point(i, y, gene));

        }

        return points;
    }
//    // 将输入数据分割并添加到点列表中
//    /**
//     * 用来处理String formData = "3.275154, 2.957587\n" +
//     *                 "-3.344465, 2.603513\n" +
//     *                 这种类型的输入数据
//     * */
//    private static List<Point> parseData(String formData) {
//        List<Point> points = new ArrayList<>();
//        String[] lines = formData.split("\n");
//        for (String line : lines) {
//            String[] coordinates = line.split(", ");
//            double x = Double.parseDouble(coordinates[0]);
//            double y = Double.parseDouble(coordinates[1]);
//            points.add(new Point(x, y));
//        }
//        return points;
//    }

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

