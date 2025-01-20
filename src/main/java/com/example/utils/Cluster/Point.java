package com.example.utils.Cluster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

// 表示二维坐标点的类
@Data
@NoArgsConstructor
@ToString
public class Point implements Serializable {
    double x;
    double y;//离上一个距离
    double y2;//下一个距离
    String Hugo_Symbol;
    String Cluster;
    int size;
    String strand;
    String WtAlle;
    String MutAlle;
    String mutDescription;
    String context;


    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Point(double x, double y, String gene) {
        this.x = x;
        this.y = y;
        this.Hugo_Symbol = gene;
    }
    public Point(double x, double y,double y2, String gene) {
        this.x = x;
        this.y = y;
        this.y2 = y2;
        this.Hugo_Symbol = gene;
    }
    public Point(double x, double y,double y2, String gene, String WtAlle, String MutAlle, String strand,String mutDescription) {
        this.x = x;
        this.y = y;
        this.y2 = y2;
        this.Hugo_Symbol = gene;
        this.WtAlle = WtAlle;
        this.MutAlle = MutAlle;
        this.strand = strand;
        this.mutDescription = mutDescription;
    }



    // 添加的构造函数，用于复制另一个点对象的属性值
    public Point(Point other) {
        this.x = other.x;
        this.y = other.y;
        this.y2 = other.y2;
        this.Cluster = other.Cluster;
        this.size = other.size;
        this.Hugo_Symbol = other.Hugo_Symbol;
        this.strand = other.strand;
        this.WtAlle = other.WtAlle;
        this.MutAlle = other.MutAlle;
        this.mutDescription = other.mutDescription;
    }

    public Point(int x, double y, double y2, String gene, String WtAlle, String MutAlle, String strand, String mutDescription, String context) {
        this.x = x;
        this.y = y;
        this.y2 = y2;
        this.Hugo_Symbol = gene;
        this.WtAlle = WtAlle;
        this.MutAlle = MutAlle;
        this.strand = strand;
        this.mutDescription = mutDescription;
        this.context = context;
    }

    @Override
    public String toString() {
        return "{"+Hugo_Symbol+"(" + x + ", " + y +  ", " + y2+ ")"+"}";
    }

}
