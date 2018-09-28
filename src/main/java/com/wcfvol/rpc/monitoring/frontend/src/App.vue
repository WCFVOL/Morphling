<template>
  <div id="app">
    <el-container>
      <el-header>
        <nav-bar></nav-bar>
      </el-header>
      <el-main>Main</el-main>
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
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
