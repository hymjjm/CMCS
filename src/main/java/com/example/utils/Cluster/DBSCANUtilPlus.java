package com.example.utils.Cluster;

import com.example.entity.JjmClusterUpload;
import org.springframework.stereotype.Component;

import java.util.*;

@Component // 声明为Spring组件，让Spring容器管理
public class DBSCANUtilPlus {

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
    public static List<Set<Point>> RunMutCluster(List<JjmClusterUpload> JjmBrcaMaf, double eps, int minPts){
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
    public static List<Point> parseDataMut(List<JjmClusterUpload> JjmBrcaMaf, double eps) {
        List<Point> points = new ArrayList<>();
        // 查询染色体为chr的数据，并按Start_Position列排序
        List<JjmClusterUpload> dataList = JjmBrcaMaf;

        // 使用 LinkedHashSet 来去重
        Set<Integer> visited = new LinkedHashSet<>();
        // 遍历查询到的数据
        for (int i = 0; i < dataList.size(); i++) {
            JjmClusterUpload currentData = dataList.get(i);
            int s = currentData.getGenomeStart(); // 当前行的起始位置作为x值

            // 如果当前 s 已经处理过了，就跳过
            if (visited.contains(s)) {
                continue;
            }
            visited.add(s);

            // 计算y值
            double y1=9999, y2=9999;
            if (i == 0) {
                // 如果是第一行数据，y值为第二行减第一行
                JjmClusterUpload nextData1 = dataList.get(i + 1);
                int nextX = nextData1.getGenomeStart();
                y1 = nextX - s; // 后一行与当前行的差值作为y值
                y2 = y1;
            } else if (i == dataList.size() - 1) {
                // 如果是最后一行数据，不需要计算y2值
                JjmClusterUpload prevData2 = dataList.get(i - 1);
                int prevX = prevData2.getGenomeStart();
                y1 = s - prevX; // 当前行与前一行的起始位置差值作为y值
                y2 = -1;
            } else {
                JjmClusterUpload prevData = dataList.get(i - 1);
                int prevX = prevData.getGenomeStart();
                y1 = s - prevX; // 当前行与前一行的起始位置差值作为y值
                JjmClusterUpload nextData = dataList.get(i + 1);
                int nextX = nextData.getGenomeStart();

                y2 = nextX - s; // 当前行与后一行的起始位置差值作为y2值
                System.out.println( "  y2: " + y2  + " nextX:"+nextX +" s:"+s +" prevX:"+prevX);
            }

            String gene = currentData.getGeneSymbol(); // 当前行的Hugo_Symbol作为gene值

//            System.out.println("Current point: " + s + " with y1: " + y1 + " and y2: " + y2 + " gene: " + gene);

            if ((y1 < eps && y1 != 0) || (y2 < eps && y2 != 0)) {
                points.add(new Point(s, y1, y2, gene,currentData.getWtAllele(), currentData.getMutAllele(), currentData.getStrand(), currentData.getMutDescription(),currentData.getContext()));
//                System.out.println("Adding point: " + s + " with y1: " + y1 + " and y2: " + y2 + " gene: " + gene);
            }
//        if(y1>1414 && y2<1414 && y2 != 0){
//            points.add(new Point(s, y2, gene));
//        }
//        points.add(new Point(i, y, gene));

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


