<template>
  <div id="app">
    <el-container style="height: 100vh;">
      <el-header style="background-color: #409EFF; color: white; font-size: 20px; line-height: 60px; padding: 0 20px;">
        <i class="el-icon-bicycle" style="margin-right: 10px;"></i>
        共享单车运维调度系统
      </el-header>
      <el-container>
        <el-aside width="200px" style="background-color: #f5f5f5;">
          <el-menu
            :default-active="$route.path"
            router
            background-color="#f5f5f5"
            text-color="#333"
            active-text-color="#409EFF">
            <el-menu-item index="/dashboard">
              <i class="el-icon-data-line"></i>
              <span>数据概览</span>
            </el-menu-item>
            <el-menu-item index="/bikes">
              <i class="el-icon-mobile-phone"></i>
              <span>车辆管理</span>
            </el-menu-item>
            <el-menu-item index="/parking">
              <i class="el-icon-location"></i>
              <span>停车点管理</span>
            </el-menu-item>
            <el-menu-item index="/tasks">
              <i class="el-icon-document"></i>
              <span>运维任务</span>
            </el-menu-item>
            <el-menu-item index="/workers">
              <i class="el-icon-user"></i>
              <span>运维人员</span>
            </el-menu-item>
            <el-menu-item index="/abnormal">
              <i class="el-icon-warning"></i>
              <span>异常记录</span>
            </el-menu-item>
            <el-menu-item index="/statistics">
              <i class="el-icon-pie-chart"></i>
              <span>区域统计</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
.el-header {
  display: flex;
  align-items: center;
}
</style>
