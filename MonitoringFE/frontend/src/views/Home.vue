<template>
  <el-container>
    <el-header>
      <h1  style="float:left;margin: 0%;">
        监控概览
      </h1>
    </el-header>
    <el-main>
      <div v-for="mod in modules" :key="mod" >
        <el-card class="box-card" >
          <div slot="header" class="clearfix">
            <span>{{mod.name}}</span>
            <el-button style="float: right; padding: 3px 0" type="text">查看详情</el-button>
          </div>
          <div class="text item">
            <h1>提供商：{{mod.provider}}</h1>
            <h1>描述</h1>
            {{mod.message}}
          </div>
        </el-card>
      </div>
    </el-main>
  </el-container>
</template>

<script>
// @ is an alias to /src

export default {
  name: 'home',
  data() {
    return {
      modules:[
        {name:"获取时间",message:"获取时间",provider:"morphling"},
        {name:"获取天气",message:"获取天气",provider:"morphling"}
      ],
      node:10
    }
  }
}
</script>

<style lang="scss">
  .text {
    text-align: left;
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .clearfix {
    text-align: left;
  }

  .box-card {
    width: 320px;
    float: left;
    margin-right: 50px;
    margin-bottom: 50px;
  }

</style>
