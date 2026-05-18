<template>
  <div class="dashboard">
    <h2>数据概览</h2>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <i class="el-icon-mobile-phone stat-icon blue"></i>
            <div class="stat-info">
              <div class="stat-value">{{ dashboard.totalBikes }}</div>
              <div class="stat-label">总车辆数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <i class="el-icon-circle-check stat-icon green"></i>
            <div class="stat-info">
              <div class="stat-value">{{ dashboard.availableBikes }}</div>
              <div class="stat-label">可用车辆</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <i class="el-icon-warning stat-icon orange"></i>
            <div class="stat-info">
              <div class="stat-value">{{ dashboard.lowBatteryBikes }}</div>
              <div class="stat-label">低电量车辆</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <i class="el-icon-setting stat-icon red"></i>
            <div class="stat-info">
              <div class="stat-value">{{ dashboard.maintenanceBikes }}</div>
              <div class="stat-label">维修中车辆</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <i class="el-icon-location stat-icon blue"></i>
            <div class="stat-info">
              <div class="stat-value">{{ dashboard.totalParkingSpots }}</div>
              <div class="stat-label">停车点数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <i class="el-icon-location-outline stat-icon red"></i>
            <div class="stat-info">
              <div class="stat-value">{{ dashboard.fullParkingSpots }}</div>
              <div class="stat-label">已满停车点</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <i class="el-icon-document stat-icon blue"></i>
            <div class="stat-info">
              <div class="stat-value">{{ dashboard.totalTasks }}</div>
              <div class="stat-label">总任务数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <i class="el-icon-time stat-icon orange"></i>
            <div class="stat-info">
              <div class="stat-value">{{ dashboard.pendingTasks }}</div>
              <div class="stat-label">待处理任务</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <div slot="header">车辆状态分布</div>
          <div style="display: flex; justify-content: space-around; align-items: center; padding: 20px;">
            <div class="pie-item">
              <div class="pie-value green">{{ dashboard.availableBikes }}</div>
              <div class="pie-label">可用</div>
            </div>
            <div class="pie-item">
              <div class="pie-value orange">{{ dashboard.lowBatteryBikes }}</div>
              <div class="pie-label">低电量</div>
            </div>
            <div class="pie-item">
              <div class="pie-value red">{{ dashboard.maintenanceBikes }}</div>
              <div class="pie-label">维修中</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header">运维人员状态</div>
          <div style="display: flex; justify-content: space-around; align-items: center; padding: 20px;">
            <div class="pie-item">
              <div class="pie-value blue">{{ dashboard.totalWorkers }}</div>
              <div class="pie-label">总人数</div>
            </div>
            <div class="pie-item">
              <div class="pie-value green">{{ dashboard.onlineWorkers }}</div>
              <div class="pie-label">在线</div>
            </div>
            <div class="pie-item">
              <div class="pie-value orange">{{ dashboard.abnormalRecords }}</div>
              <div class="pie-label">异常记录</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import request from '../api/request'

export default {
  name: 'Dashboard',
  data() {
    return {
      dashboard: {
        totalBikes: 0,
        availableBikes: 0,
        lowBatteryBikes: 0,
        maintenanceBikes: 0,
        totalParkingSpots: 0,
        fullParkingSpots: 0,
        totalWorkers: 0,
        onlineWorkers: 0,
        totalTasks: 0,
        pendingTasks: 0,
        completedTasks: 0,
        abnormalRecords: 0
      }
    }
  },
  mounted() {
    this.loadData()
    this.timer = setInterval(this.loadData, 5000)
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    loadData() {
      request.get('/statistics/dashboard').then(res => {
        this.dashboard = res.data
      })
    }
  }
}
</script>

<style scoped>
.stat-card {
  margin-bottom: 20px;
}
.stat-content {
  display: flex;
  align-items: center;
}
.stat-icon {
  font-size: 40px;
  margin-right: 20px;
}
.stat-info .stat-value {
  font-size: 28px;
  font-weight: bold;
}
.stat-info .stat-label {
  font-size: 14px;
  color: #909399;
}
.blue { color: #409EFF; }
.green { color: #67C23A; }
.orange { color: #E6A23C; }
.red { color: #F56C6C; }
.pie-item {
  text-align: center;
}
.pie-value {
  font-size: 32px;
  font-weight: bold;
}
.pie-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}
</style>
