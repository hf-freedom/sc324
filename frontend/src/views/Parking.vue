<template>
  <div class="parking">
    <h2>停车点管理</h2>
    <el-card>
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="区域">
          <el-select v-model="searchForm.area" placeholder="全部" clearable @change="loadParkingSpots">
            <el-option v-for="area in areas" :key="area" :label="area" :value="area"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <el-table :data="parkingSpots" border stripe>
        <el-table-column prop="name" label="停车点名称" width="180"></el-table-column>
        <el-table-column prop="area" label="区域" width="80"></el-table-column>
        <el-table-column prop="capacity" label="容量" width="80"></el-table-column>
        <el-table-column label="当前车辆" width="100">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.currentBikeCount >= scope.row.capacity ? 'red' : '' }">{{ scope.row.currentBikeCount }}</span>
            <span>/{{ scope.row.capacity }}</span>
          </template>
        </el-table-column>
        <el-table-column label="使用率" width="150">
          <template slot-scope="scope">
            <el-progress :percentage="Math.round(scope.row.currentBikeCount / scope.row.capacity * 100)" :status="scope.row.currentBikeCount >= scope.row.capacity ? 'exception' : 'success'"></el-progress>
          </template>
        </el-table-column>
        <el-table-column prop="longitude" label="经度" width="120"></el-table-column>
        <el-table-column prop="latitude" label="纬度" width="120"></el-table-column>
        <el-table-column label="状态" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.currentBikeCount >= scope.row.capacity" type="danger">已满</el-tag>
            <el-tag v-else-if="scope.row.currentBikeCount / scope.row.capacity > 0.8" type="warning">接近饱和</el-tag>
            <el-tag v-else type="success">正常</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import request from '../api/request'

export default {
  name: 'Parking',
  data() {
    return {
      parkingSpots: [],
      areas: [],
      searchForm: {
        area: ''
      }
    }
  },
  mounted() {
    this.loadAreas()
    this.loadParkingSpots()
    this.timer = setInterval(this.loadParkingSpots, 5000)
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    loadAreas() {
      request.get('/areas').then(res => {
        this.areas = res.data
      })
    },
    loadParkingSpots() {
      request.get('/parking-spots', { params: this.searchForm }).then(res => {
        this.parkingSpots = res.data
      })
    }
  }
}
</script>
