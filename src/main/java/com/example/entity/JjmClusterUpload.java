package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author jjm
 * @since 2024-04-11
 */
@Getter
@Setter
@TableName("jjm_cluster_upload")
public class JjmClusterUpload implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String geneSymbol;

    private String chromosome;

    private Integer genomeStart;

    private String tumorSample;

    private String cancerType;
@TableField(value = "Reference_Genomes")
    private String referenceGenomes;

    private String strand;

    private String wtAllele;

    private String mutAllele;

    private String nickName;

    private String mutDescription;

    private String Context;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JjmClusterUpload that = (JjmClusterUpload) o;
        return genomeStart == that.genomeStart;
    }

    @Override
    public int hashCode() {
        return Objects.hash(genomeStart);
    }
}
