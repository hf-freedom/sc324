<template>
  <div class="bikes">
    <h2>车辆管理</h2>
    <el-card>
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="区域">
          <el-select v-model="searchForm.area" placeholder="全部" clearable @change="loadBikes">
            <el-option v-for="area in areas" :key="area" :label="area" :value="area"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable @change="loadBikes">
            <el-option label="可用" value="AVAILABLE"></el-option>
            <el-option label="使用中" value="IN_USE"></el-option>
            <el-option label="低电量" value="LOW_BATTERY"></el-option>
            <el-option label="维修中" value="MAINTENANCE"></el-option>
            <el-option label="停用" value="OUT_OF_SERVICE"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <el-table :data="bikes" border stripe>
        <el-table-column prop="bikeNo" label="车辆编号" width="100"></el-table-column>
        <el-table-column prop="area" label="区域" width="80"></el-table-column>
        <el-table-column prop="batteryLevel" label="电量(%)" width="100">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.batteryLevel" :status="scope.row.batteryLevel <= 20 ? 'exception' : 'success'"></el-progress>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="parkingSpotId" label="停车点" width="100"></el-table-column>
        <el-table-column prop="longitude" label="经度" width="120"></el-table-column>
        <el-table-column prop="latitude" label="纬度" width="120"></el-table-column>
        <el-table-column label="操作" width="240">
          <template slot-scope="scope">
            <el-button size="mini" type="success" @click="startRide(scope.row)" v-if="scope.row.status === 'AVAILABLE'">开始骑行</el-button>
            <el-button size="mini" type="primary" @click="openEndRide(scope.row)" v-if="scope.row.status === 'IN_USE'">结束骑行</el-button>
            <el-button size="mini" type="warning" @click="openUpdateBattery(scope.row)" v-if="scope.row.status !== 'MAINTENANCE'">更新电量</el-button>
            <el-button size="mini" type="danger" @click="openReportFault(scope.row)" v-if="scope.row.status === 'AVAILABLE'">故障上报</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="结束骑行" :visible.sync="endRideVisible" width="500px">
      <el-form :model="rideForm" label-width="100px">
        <el-form-item label="车辆编号">
          <el-input v-model="rideForm.bikeNo" disabled></el-input>
        </el-form-item>
        <el-form-item label="经度">
          <el-input v-model.number="rideForm.longitude"></el-input>
        </el-form-item>
        <el-form-item label="纬度">
          <el-input v-model.number="rideForm.latitude"></el-input>
        </el-form-item>
        <el-form-item label="剩余电量(%)">
          <el-input v-model.number="rideForm.batteryLevel"></el-input>
        </el-form-item>
        <el-form-item label="停车点">
          <el-select v-model="rideForm.parkingSpotId" placeholder="请选择停车点">
            <el-option v-for="spot in parkingSpots" :key="spot.id" :label="spot.name" :value="spot.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="endRideVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEndRide">确认</el-button>
      </div>
    </el-dialog>

    <el-dialog title="更新电量" :visible.sync="batteryVisible" width="400px">
      <el-form :model="batteryForm" label-width="100px">
        <el-form-item label="车辆编号">
          <el-input v-model="batteryForm.bikeNo" disabled></el-input>
        </el-form-item>
        <el-form-item label="当前电量">
          <el-slider v-model="batteryForm.batteryLevel" :min="0" :max="100" show-input></el-slider>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="batteryVisible = false">取消</el-button>
        <el-button type="warning" @click="submitUpdateBattery">更新</el-button>
      </div>
    </el-dialog>

    <el-dialog title="故障上报" :visible.sync="faultVisible" width="400px">
      <el-form :model="faultForm" label-width="80px">
        <el-form-item label="车辆编号">
          <el-input v-model="faultForm.bikeNo" disabled></el-input>
        </el-form-item>
        <el-form-item label="故障描述">
          <el-input type="textarea" v-model="faultForm.description" rows="3"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="faultVisible = false">取消</el-button>
        <el-button type="danger" @click="submitFault">上报</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '../api/request'

export default {
  name: 'Bikes',
  data() {
    return {
      bikes: [],
      areas: [],
      parkingSpots: [],
      searchForm: {
        area: '',
        status: ''
      },
      endRideVisible: false,
      rideForm: {
        bikeId: '',
        bikeNo: '',
        longitude: 0,
        latitude: 0,
        batteryLevel: 0,
        parkingSpotId: ''
      },
      batteryVisible: false,
      batteryForm: {
        bikeId: '',
        bikeNo: '',
        batteryLevel: 0
      },
      faultVisible: false,
      faultForm: {
        bikeId: '',
        bikeNo: '',
        description: ''
      }
    }
  },
  mounted() {
    this.loadAreas()
    this.loadBikes()
    this.loadParkingSpots()
    this.timer = setInterval(this.loadBikes, 5000)
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
    loadBikes() {
      request.get('/bikes', { params: this.searchForm }).then(res => {
        this.bikes = res.data
      })
    },
    loadParkingSpots() {
      request.get('/parking-spots').then(res => {
        this.parkingSpots = res.data
      })
    },
    getStatusType(status) {
      const map = {
        'AVAILABLE': 'success',
        'IN_USE': 'primary',
        'LOW_BATTERY': 'warning',
        'MAINTENANCE': 'danger',
        'OUT_OF_SERVICE': 'info'
      }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = {
        'AVAILABLE': '可用',
        'IN_USE': '使用中',
        'LOW_BATTERY': '低电量',
        'MAINTENANCE': '维修中',
        'OUT_OF_SERVICE': '停用'
      }
      return map[status] || status
    },
    startRide(bike) {
      request.post('/bikes/start-ride', { bikeId: bike.id }).then(res => {
        this.$message.success('开始骑行成功')
        this.loadBikes()
      })
    },
    openEndRide(bike) {
      this.rideForm = {
        bikeId: bike.id,
        bikeNo: bike.bikeNo,
        longitude: bike.longitude,
        latitude: bike.latitude,
        batteryLevel: bike.batteryLevel,
        parkingSpotId: bike.parkingSpotId
      }
      this.endRideVisible = true
    },
    openUpdateBattery(bike) {
      this.batteryForm = {
        bikeId: bike.id,
        bikeNo: bike.bikeNo,
        batteryLevel: bike.batteryLevel
      }
      this.batteryVisible = true
    },
    submitUpdateBattery() {
      request.put(`/bikes/${this.batteryForm.bikeId}/battery`, { batteryLevel: this.batteryForm.batteryLevel }).then(res => {
        this.$message.success('电量更新成功')
        this.batteryVisible = false
        this.loadBikes()
      })
    },
    submitEndRide() {
      request.post('/bikes/end-ride', {
        bikeId: this.rideForm.bikeId,
        longitude: this.rideForm.longitude,
        latitude: this.rideForm.latitude,
        batteryLevel: this.rideForm.batteryLevel,
        parkingSpotId: this.rideForm.parkingSpotId
      }).then(res => {
        this.$message.success('骑行结束，车辆信息已更新')
        this.endRideVisible = false
        this.loadBikes()
      })
    },
    openReportFault(bike) {
      this.faultForm = {
        bikeId: bike.id,
        bikeNo: bike.bikeNo,
        description: ''
      }
      this.faultVisible = true
    },
    submitFault() {
      request.post('/bikes/report-fault', this.faultForm).then(res => {
        this.$message.success('故障已上报，车辆进入维修状态')
        this.faultVisible = false
        this.loadBikes()
      })
    }
  }
}
</script>
