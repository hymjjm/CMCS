<template>
  <div>
    <el-timeline style="margin-top: 50px">
      <el-timeline-item>
        <el-card>
          <h1>mRNA Expression</h1>
          <ul>
            <li>
              <p class="intro-font">
                Our analysis utilizes log<span class="subscript">2</span>(tpm + 1) data obtained from the TCGA (The Cancer Genome Atlas) and GTEx (Genotype-Tissue Expression) databases
              </p>
            </li>
            <li>
              <p class="intro-font">
                The asterisk (*) represents the degree of difference
              </p>
            </li>
            <li>
              <p class="intro-font">
                R Packages: limma
              </p>
            </li>
          </ul>
        </el-card>
      </el-timeline-item>
      <el-timeline-item>
        <el-card>
          <h1>Survival</h1>
          <!--差异表达分析数据来源为TCGA和GTEx的log2(tpm + 0.01)数据, 采用limma对其进行差异表达分析-->
          <ul>
            <li>
              <p class="intro-font">
                Our analysis is based on mRNA expression data obtained from TCGA (The Cancer Genome Atlas) for tumor patients
              </p>
            </li>
            <li>
              <p class="intro-font">
                Our samples are divided into high and low expression groups using median gene expression as the cutoff
              </p>
            </li>
            <li>
              <p class="intro-font">
                R Packages: survminer, survival
              </p>
            </li>
          </ul>
        </el-card>

      </el-timeline-item>
      <el-timeline-item>
        <el-card>
          <h1>ssGSEA</h1>
          <!--差异表达分析数据来源为TCGA和GTEx的log2(tpm + 0.01)数据, 采用limma对其进行差异表达分析-->
          <ul>
            <li>
              <p class="intro-font">
                Our analysis is based on mRNA expression data obtained from TCGA (The Cancer Genome Atlas) for tumor patients
              </p>
            </li>
            <li>
              <p class="intro-font">
                Our samples are divided into high and low expression groups using median gene expression as the cutoff
              </p>
            </li>
            <li>
              <p class="intro-font">
                R Packages: GSVA
              </p>
            </li>
          </ul>
        </el-card>
      </el-timeline-item>
      <el-timeline-item>
        <el-card>
          <h1>DNA Methylation</h1>
          <ul>
            <li>
              <p class="intro-font">
                The DNA methylation data used in the analysis is sourced from the TCGA (The Cancer Genome Atlas)
              </p>
            </li>
          </ul>
        </el-card>
      </el-timeline-item>
      <el-timeline-item>
        <el-card>
          <h1>Mutation</h1>
          <ul>
            <li>
              <p class="intro-font">
                The mutation data used in the analysis is sourced from the TCGA (The Cancer Genome Atlas)
              </p>
            </li>
          </ul>
        </el-card>
      </el-timeline-item>
      <el-timeline-item>
        <el-card>
          <h1>Copy Number Variance</h1>
          <ul>
            <li>
              <p class="intro-font">
                The CNV data used in the analysis is sourced from the TCGA (The Cancer Genome Atlas)
              </p>
            </li>
          </ul>
        </el-card>
      </el-timeline-item>
      <el-timeline-item>
        <el-card>
          <el-row>
            <el-col :span="12">
              <el-table
                :data="tcgas1"
                border
                style="width: 100%">
                <el-table-column
                  prop="some"
                  label="Abbreviation"
                  width="180">
                </el-table-column>
                <el-table-column
                  prop="full"
                  label="Cancer type"
                  width="300">
                </el-table-column>
              </el-table>
            </el-col>
            <el-col :span="12">
              <el-table
                :data="tcgas2"
                border
                style="width: 100%">
                <el-table-column
                  prop="some"
                  label="Abbreviation"
                  width="180">
                </el-table-column>
                <el-table-column
                  prop="full"
                  label="Cancer type"
                  width="300">
                </el-table-column>
              </el-table>
            </el-col>
          </el-row>
        </el-card>
      </el-timeline-item>
    </el-timeline>


  </div>
</template>

<script>
  export default {
    name: "Method",
    data() {
      return {
        tcgas1: [
          {some: 'ACC', full: 'Adrenocortical carcinoma'},
          {some: 'BLCA', full: 'Bladder urothelial carcinoma'},
          {some: 'BRCA', full: 'Breast invasive carcinoma'},
          {some: 'CESC', full: 'Cervical squamous cell carcinoma'},
          {some: 'CHOL', full: 'Cholangiocarcinoma'},
          {some: 'COAD', full: 'Colon adenocarcinoma'},
          {some: 'DLBC', full: 'Lymphoid neoplasm diffuse large B-cell'},
          {some: 'ESCA', full: 'Esophageal carcinoma'},
          {some: 'GBM', full: 'Glioblastoma multiforme'},
          {some: 'HNSC', full: 'Head and neck squamous cell carcinoma '},
          {some: 'KICH', full: 'Kidney chromophobe'},
          {some: 'KIRC', full: 'Kidney renal clear cell carcinoma'},
          {some: 'KIRP', full: 'Kidney renal papillary cell carcinoma'},
          {some: 'LAML', full: 'Acute myeloid leukemia'},
          {some: 'LGG', full: 'Brain Lower Grade Glioma'},
          {some: 'LIHC', full: 'Liver hepatocellular carcinoma'},
          {some: 'LUAD', full: 'Lung adenocarcinoma'},
        ],
        tcgas2: [
          {some: 'LUSC', full: 'Lung squamous cell carcinoma'},
          {some: 'MESO', full: 'Mesothelioma'},
          {some: 'OV', full: 'Ovarian serous cystadenocarcinoma'},
          {some: 'PAAD', full: 'Pancreatic adenocarcinoma'},
          {some: 'PCPG', full: 'Pheochromocytoma and paraganglioma'},
          {some: 'PRAD', full: 'Prostate adenocarcinoma'},
          {some: 'READ', full: 'Rectum adenocarcinoma'},
          {some: 'SARC', full: 'Sarcoma'},
          {some: 'SCBC', full: 'Small-cell bladder cancer'},
          {some: 'SKCM', full: 'Skin cutaneous melanoma'},
          {some: 'STAD', full: 'Stomach adenocarcinoma'},
          {some: 'TGCT', full: 'Testicular germ cell tumors'},
          {some: 'THCA', full: 'Thyroid carcinoma'},
          {some: 'THYM', full: 'Thymoma'},
          {some: 'UCEC', full: 'Uterine corpus endometrial carcinoma'},
          {some: 'UCS', full: 'Uterine carcinosarcoma'},
          {some: 'UVM', full: 'Uveal melanoma'},
        ]
      }
    }
  }
</script>

<style scoped>
  .intro-font {
    font-size: 18px;
  }

  .subscript {
    position: relative;
    top: 0.3em; /* 调整下标位置 */
    font-size: 0.7em; /* 调整下标大小 */
  }

</style>
