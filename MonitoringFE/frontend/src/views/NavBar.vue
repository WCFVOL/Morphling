<template>
    <div class="main">
        <img src="../assets/morphling.jpeg" style="width:100%">
        <el-row class="tac">
          <el-col :span="12" style="width:100%;margin-top:0px;">
            <el-menu
              default-active="2"
              class="el-menu-vertical-demo"
              :router=true
              background-color="RGB(38,100,130)"
              active-text-color="#ffd04b"
              text-color="RGB(217,155,125)">
              <el-menu-item index="1" route="/"> 
                  <i class="el-icon-menu"></i>
                  <span slot="title">概览</span>
              </el-menu-item>
              <el-menu-item index="2" >
                  <i class="el-icon-menu"></i>
                  <span slot="title">服务状态</span>
              </el-menu-item>
              <el-menu-item index="3">
                  <i class="el-icon-menu"></i>
                  <span slot="title">节点状态</span>
              </el-menu-item>
              <el-menu-item index="4">
                  <i class="el-icon-menu"></i>
                  <span slot="title">日志</span>
              </el-menu-item>
              <el-menu-item index="5" route="about">
                  <i class="el-icon-menu"></i>
                  <span slot="title">关于</span>
              </el-menu-item>
            </el-menu>
          </el-col>
        </el-row>
    </div>
</template>

<script>
export default {
    
}
</script>

<style lang="scss">
  .el-menu-item{
    text-align:left;
  }
  .main{
    background-color: RGB(110,159,186);
    margin-bottom: 0%;
    height: 100%;
    background-size: 100%;
    background-position: top left;
    background-position: 0px 0px;
    background-position: 100% 100%;
  }
  .el-row {
    margin-top: 0%;
    margin-bottom: 20px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .el-col {
    border-radius: 4px;
  }
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .bg-purple-light {
    background: #e5e9f2;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
  .tac {
    float: top;
  }
</style>

