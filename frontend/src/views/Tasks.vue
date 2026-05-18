<template>
  <div class="tasks">
    <h2>运维任务</h2>
    <el-card>
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="区域">
          <el-select v-model="searchForm.area" placeholder="全部" clearable @change="loadTasks">
            <el-option v-for="area in areas" :key="area" :label="area" :value="area"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="运维人员">
          <el-select v-model="searchForm.workerId" placeholder="全部" clearable @change="loadTasks">
            <el-option v-for="worker in workers" :key="worker.id" :label="worker.name" :value="worker.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <el-table :data="tasks" border stripe>
        <el-table-column prop="id" label="任务ID" width="180"></el-table-column>
        <el-table-column prop="taskType" label="类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getTaskTypeColor(scope.row.taskType)">{{ getTaskTypeText(scope.row.taskType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="bikeId" label="车辆ID" width="80"></el-table-column>
        <el-table-column prop="area" label="区域" width="80"></el-table-column>
        <el-table-column prop="priority" label="优先级" width="80">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.priority === 1" type="danger">紧急</el-tag>
            <el-tag v-else type="warning">普通</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="150"></el-table-column>
        <el-table-column prop="assignedWorkerId" label="运维人员" width="100"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
        <el-table-column label="操作" width="100">
          <template slot-scope="scope">
            <el-button size="mini" type="success" @click="completeTask(scope.row)" v-if="scope.row.status === 'ASSIGNED'">完成</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import request from '../api/request'

export default {
  name: 'Tasks',
  data() {
    return {
      tasks: [],
      areas: [],
      workers: [],
      searchForm: {
        area: '',
        workerId: ''
      }
    }
  },
  mounted() {
    this.loadAreas()
    this.loadWorkers()
    this.loadTasks()
    this.timer = setInterval(this.loadTasks, 5000)
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
    loadWorkers() {
      request.get('/workers').then(res => {
        this.workers = res.data
      })
    },
    loadTasks() {
      request.get('/tasks', { params: this.searchForm }).then(res => {
        this.tasks = res.data
      })
    },
    getTaskTypeText(type) {
      const map = {
        'BATTERY_SWAP': '换电任务',
        'RELOCATION': '车辆调拨',
        'MAINTENANCE': '维修任务',
        'ABNORMAL_PARKING': '异常停车'
      }
      return map[type] || type
    },
    getTaskTypeColor(type) {
      const map = {
        'BATTERY_SWAP': 'warning',
        'RELOCATION': 'primary',
        'MAINTENANCE': 'danger',
        'ABNORMAL_PARKING': 'info'
      }
      return map[type] || 'info'
    },
    getStatusType(status) {
      const map = {
        'PENDING': 'warning',
        'ASSIGNED': 'primary',
        'IN_PROGRESS': 'primary',
        'COMPLETED': 'success',
        'CANCELLED': 'info',
        'TIMEOUT': 'danger'
      }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = {
        'PENDING': '待分配',
        'ASSIGNED': '已分配',
        'IN_PROGRESS': '处理中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消',
        'TIMEOUT': '已超时'
      }
      return map[status] || status
    },
    completeTask(task) {
      this.$confirm('确认完成该任务?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.post(`/tasks/${task.id}/complete`, { workerId: task.assignedWorkerId }).then(res => {
          this.$message.success('任务已完成')
          this.loadTasks()
        })
      })
    }
  }
}
</script>
