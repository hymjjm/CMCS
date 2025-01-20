package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author jjm
 * @since 2024-06-12
 */
@Getter
@Setter
  @TableName("jjm_cluster_gene")
public class JjmClusterGene implements Serializable {

    private static final long serialVersionUID = 1L;

    private String gene;

    private String chr;

    private String mutNumber;

    private String mutRange;

    private String clusterType;

    private String cancer;

    private String sample;

    private String rg;


}
