<template>
  <div class="statistics">
    <h2>区域统计</h2>
    <el-card>
      <el-table :data="statistics" border stripe>
        <el-table-column prop="area" label="区域" width="100"></el-table-column>
        <el-table-column prop="totalBikes" label="总车辆" width="100"></el-table-column>
        <el-table-column prop="availableBikes" label="可用车辆" width="100">
          <template slot-scope="scope">
            <span class="green">{{ scope.row.availableBikes }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="lowBatteryBikes" label="低电量" width="100">
          <template slot-scope="scope">
            <span class="orange">{{ scope.row.lowBatteryBikes }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="inUseBikes" label="使用中" width="100"></el-table-column>
        <el-table-column prop="maintenanceBikes" label="维修中" width="100">
          <template slot-scope="scope">
            <span class="red">{{ scope.row.maintenanceBikes }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="parkingSpots" label="停车点数" width="100"></el-table-column>
        <el-table-column prop="overCapacitySpots" label="超容停车点" width="120">
          <template slot-scope="scope">
            <span :class="scope.row.overCapacitySpots > 0 ? 'red' : ''">{{ scope.row.overCapacitySpots }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="pendingTasks" label="待处理任务" width="120">
          <template slot-scope="scope">
            <span :class="scope.row.pendingTasks > 0 ? 'orange' : ''">{{ scope.row.pendingTasks }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="statisticsTime" label="统计时间" width="160"></el-table-column>
      </el-table>
    </el-card>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <div slot="header">区域车辆分布</div>
          <div style="padding: 20px;">
            <div v-for="stat in statistics" :key="stat.area" class="bar-item">
              <div class="bar-label">{{ stat.area }}</div>
              <div class="bar-wrapper">
                <div class="bar-fill" :style="{ width: getBarWidth(stat.totalBikes) + '%' }">
                  {{ stat.totalBikes }}辆
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header">区域任务分布</div>
          <div style="padding: 20px;">
            <div v-for="stat in statistics" :key="stat.area" class="bar-item">
              <div class="bar-label">{{ stat.area }}</div>
              <div class="bar-wrapper">
                <div class="bar-fill orange" :style="{ width: getTaskBarWidth(stat.pendingTasks) + '%' }">
                  {{ stat.pendingTasks }}个
                </div>
              </div>
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
  name: 'Statistics',
  data() {
    return {
      statistics: [],
      maxBikes: 0,
      maxTasks: 0
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
      request.get('/statistics/areas').then(res => {
        this.statistics = res.data
        this.maxBikes = Math.max(...this.statistics.map(s => s.totalBikes), 1)
        this.maxTasks = Math.max(...this.statistics.map(s => s.pendingTasks), 1)
      })
    },
    getBarWidth(value) {
      return Math.round((value / this.maxBikes) * 100)
    },
    getTaskBarWidth(value) {
      return Math.round((value / this.maxTasks) * 100)
    }
  }
}
</script>

<style scoped>
.green { color: #67C23A; }
.orange { color: #E6A23C; }
.red { color: #F56C6C; }
.bar-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}
.bar-label {
  width: 60px;
  font-size: 14px;
}
.bar-wrapper {
  flex: 1;
  height: 24px;
  background-color: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
}
.bar-fill {
  height: 100%;
  background-color: #409EFF;
  color: white;
  font-size: 12px;
  line-height: 24px;
  text-align: center;
  transition: width 0.3s;
  border-radius: 4px;
}
.bar-fill.orange {
  background-color: #E6A23C;
}
</style>
