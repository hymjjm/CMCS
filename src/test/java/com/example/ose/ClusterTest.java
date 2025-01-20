//package com.example.ose;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.example.utils.Cluster.DBSCANUtil;
//import com.example.utils.Cluster.Point;
//import com.example.utils.Cluster.SilhouetteCoefficient;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.Set;
//
//import static com.example.utils.Cluster.SetPointClusterUtils.assignClusterLabels;
//
//@SpringBootTest
//public class ClusterTest {
//
//    @Autowired
//    private IJjmBrcaPd4103aService jjmBrcaMafService;
//
//    @Test
//    public void test() {
//        List<JjmBrcaPd4103a> JjmBrcaMaf = selectByChromosomeAndSortByStartPosition("chr12");
////        List<Point> points =  DBSCANUtil.parseDataMut(JjmBrcaMaf);
////        /**
////         * 使用 k-distance 计算 eps 的函数，并绘图*******************************
////         *            测试使用哪个eps最好的时候打开，接口封装好了就关掉
////         * */
////        int k = 5; //  设置 k-distance 的 k 值
////        double[] distances = calculateDistances(points, k);
////        KDistancePlotter.plot(distances);
//        List<Set<Point>> clusters1 =  DBSCANUtil.RunMutCluster(JjmBrcaMaf, 1414, 2);
//        List<Set<Point>> clusters = assignClusterLabels(clusters1);
//        /**
//         * 打印聚类数量和每个聚类的点
//         */
//        System.out.println("Number of clusters: " + clusters.size());
//        int totalPoints = 0; // 总节点数
//        for (int i = 0; i < clusters.size(); i++) {
//            System.out.println("Cluster " + (i + 1) + ": " + clusters.get(i));
//            Set<Point> clusteri = clusters.get(i);
//            int numberOfPoints = clusteri.size();
//            if(i != clusters.size()-1) totalPoints += numberOfPoints; // 累加节点数
//            System.out.println("Number of points in Cluster " + (i + 1) + ": " + numberOfPoints);
//        }
//// 计算平均值
//        double averagePoints = (double) totalPoints /(clusters.size()-1) ;
//        System.out.println("Average number of points per cluster: " + averagePoints);
//
//
//        /**
//         *       计算轮廓系数
//         * */
//        double Silhouette_score = SilhouetteCoefficient.calculate(clusters);
//        System.out.println("Silhouette_score: " + Silhouette_score);
//    }
//
//    public List<JjmBrcaPd4103a> selectByChromosomeAndSortByStartPosition(String chr) {
//        QueryWrapper<JjmBrcaPd4103a> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("chromosome", chr).orderByAsc("genome_start");
//        return jjmBrcaMafService.list(queryWrapper);
//    }
//}