<template>
  <div id="app">
    <el-container>
      <el-aside width="200px">
        <nav-bar></nav-bar>
      </el-aside>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'
import NavBar from './views/NavBar.vue'

export default {
  name: 'app',
  components: {
    HelloWorld,
    NavBar
  }
}

</script>

<style>
html,body {
    width: 100%;
    height: 100%;
}
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 0px;
  height: 100%;
}
 .el-aside {
    box-shadow: 0px 10px 5px 5px #0d55db;
    background-color: #D3DCE6;
    color: #333;
    text-align: center;
    line-height: 0px;
    margin-right: 1%;
  }
    .el-main {
    background-color: #E9EEF3;
    color: #333;
    text-align: center;
    line-height: 160px;
  }
  .el-container {
    height: 100%;
  }
</style>

