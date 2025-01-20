<!-- WordCloud.vue -->

<template>
  <div>


    <div style="display: flex;justify-content: center;align-items: center;padding: 10px;height: 600px;
    background-color: rgb(255,254,254);border-radius: 9px;padding-top: 8px;border: 1px solid #ccc;"
    >
      <div style="display: flex;justify-content: center;align-items: center" >
        <svg style="margin-left: -50px" :width="width" :height="height">
          <!-- 水印 -->
          <!--      <text x="30%" y="20" fill="#c0392b" font-size="18" font-weight="bold" >-->
          <!--        {{ EMTorOS }}-->
          <!--      </text>-->

          <a class="fontA" v-for="(tag, index) in tags" :key="`tag-${index}`">
            <text
              :id="tag.id"
              :x="tag.x"
              :y="tag.y"
              :font-size="12.5 * (600 / (600 - tag.z))"
              :fill-opacity="(400 + tag.z) / 600"
              @mousemove="listenerMove($event)"
              @mouseout="listenerOut($event)"
              @click="clickToShawCard(tag)"

            >
              {{ tag.text }}
            </text>
          </a>
        </svg>
      </div>
<!--      <div style="width: 2px; height: 500px;background-color: rgba(0,0,0,0.49);"/>-->
<!--      <div style="width: 50%;display: flex;justify-content: center;align-items: center" >-->
<!--        <el-image src="http://tmliang.cn/pic/jjm/mutCluater/3-chr.png"-->
<!--             style="color: #FFFFFF ;height: 500px;width: 500px;" lazy></el-image>-->
<!--      </div>-->


    </div>

    <el-dialog
      :visible.sync="showModal"
      title="Search Result"
      width="500px">
      <DialogResult :dialogData="dialogData"></DialogResult>
      <span slot="footer" class="dialog-footer">
    Click here to Analysis: <el-button type="text" style="font-size: 25px"  @click="clickToPage(dialogData[0].gene)">Go</el-button>
        </span>
    </el-dialog>

  </div>
</template>

<script>
import DialogResult from "./DialogResult";
export default {
  name: "WordCloud",
   components:{DialogResult},
  props: {
    WordClouddata: Array, // 接收 dropdowndata 作为 props
    // EMTorOS: String,
  },
  data() {
    return {
      width: 700,
      height: 590,
      tagsNum: 0,
      RADIUS: 270,
      speedX: Math.PI / 360 / 1.5,
      speedY: Math.PI / 360 / 1.5,
      tags: [],
      timer: null,
      //dialog
      showModal: false,
      dialogData:[],
    };
  },
  computed: {
    CX() {
      return this.width / 2;
    },
    CY() {
      return this.height / 2;
    },
  },
  created() {
    if (Array.isArray(this.WordClouddata) && this.WordClouddata.length > 0) {
      // console.log("WordClouddata:", this.WordClouddata);
      this.initData();
      this.runTags();
    } else {
      // console.error("WordClouddata is not a valid array or is empty");
    }
  },

  watch: {
    WordClouddata: {
      immediate: true, // 立即执行
      handler(newVal) {
        if (Array.isArray(newVal) && newVal.length > 0) {
          this.initData();
          this.runTags();
        }
      },
    },
  },
  methods: {
    initData() {
      let tags = [];
      this.tagsNum = this.WordClouddata.length;
      for (let i = 0; i < this.WordClouddata.length; i++) {
        let tag = {};
        let k = -1 + (2 * (i + 1) - 1) / this.tagsNum;
        let a = Math.acos(k);
        let b = a * Math.sqrt(this.tagsNum * Math.PI);
        tag.text = this.WordClouddata[i].gene;
        tag.all = this.WordClouddata[i];
        tag.x = this.CX + this.RADIUS * Math.sin(a) * Math.cos(b);
        // tag.x = this.CX + this.RADIUS * Math.sin(a) * Math.cos(b);
        tag.y = this.CY + this.RADIUS * Math.sin(a) * Math.sin(b);
        tag.z = this.RADIUS * Math.cos(a);
        tag.id = i;
        tags.push(tag);
      }
      this.tags = tags;
    },
    rotateX(angleX) {
      var cos = Math.cos(angleX);
      var sin = Math.sin(angleX);
      for (let tag of this.tags) {
        var y1 = (tag.y - this.CY) * cos - tag.z * sin + this.CY;
        var z1 = tag.z * cos + (tag.y - this.CY) * sin;
        tag.y = y1;
        tag.z = z1;
      }
    },
    rotateY(angleY) {
      var cos = Math.cos(angleY);
      var sin = Math.sin(angleY);
      for (let tag of this.tags) {
        var x1 = (tag.x - this.CX) * cos - tag.z * sin + this.CX;
        var z1 = tag.z * cos + (tag.x - this.CX) * sin;
        tag.x = x1;
        tag.z = z1;
      }
    },
    runTags() {
      if (typeof this.timer === "number") {
        clearInterval(this.timer);
        this.timer = null;
      }
      let timer = setInterval(() => {
        this.rotateX(this.speedX);
        this.rotateY(this.speedY);
      }, 17);
      this.timer = timer;
    },
    listenerMove(e) {
      if (e.target.id) {
        clearInterval(this.timer);
      }
    },
    listenerOut(e) {
      if (e.target.id) {
        this.runTags();
      }
    },
    clickToShawCard(tag) {
      this.dialogData = [tag.all];
       // console.log([tag.all])
      // 显示模态框
      this.showModal = true;
    },
    clickToPage(tag) {
      this.$router.push({ name: 'Omics', params: { key: tag} });
    },

  },
  mounted() {
    this.runTags();
  },
};
</script>

<style scoped>
.fontA {
  fill: rgb(5, 56, 68);
  font-weight: lighter;
  /*font-family: 'Bookman Old Style';*/

}

/*给每个字加白色边框*/
/*.fontA text{*/
/*  stroke-width:0.5;*/
/*  stroke:#FFFFFF;*/
/*}*/

.fontA:hover {
  fill: rgba(227, 66, 66, 0.92);
  cursor: pointer;
  font-weight: bold;
}
</style>
