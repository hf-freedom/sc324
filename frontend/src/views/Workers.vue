<template>
  <div class="workers">
    <h2>运维人员</h2>
    <el-card>
      <el-table :data="workers" border stripe>
        <el-table-column prop="name" label="姓名" width="100"></el-table-column>
        <el-table-column prop="phone" label="电话" width="130"></el-table-column>
        <el-table-column prop="area" label="负责区域" width="100"></el-table-column>
        <el-table-column label="任务限额" width="100">
          <template slot-scope="scope">
            {{ scope.row.currentTaskCount }}/{{ scope.row.taskLimit }}
          </template>
        </el-table-column>
        <el-table-column label="任务进度" width="180">
          <template slot-scope="scope">
            <el-progress :percentage="Math.round(scope.row.currentTaskCount / scope.row.taskLimit * 100)" :status="scope.row.currentTaskCount >= scope.row.taskLimit ? 'exception' : 'success'"></el-progress>
          </template>
        </el-table-column>
        <el-table-column prop="isOnline" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isOnline ? 'success' : 'info'">{{ scope.row.isOnline ? '在线' : '离线' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="viewTasks(scope.row)">查看任务</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="运维人员任务列表" :visible.sync="taskDialogVisible" width="800px">
      <el-table :data="workerTasks" border stripe>
        <el-table-column prop="id" label="任务ID" width="180"></el-table-column>
        <el-table-column prop="taskType" label="类型" width="120">
          <template slot-scope="scope">
            <el-tag>{{ getTaskTypeText(scope.row.taskType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="150"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import request from '../api/request'

export default {
  name: 'Workers',
  data() {
    return {
      workers: [],
      taskDialogVisible: false,
      workerTasks: []
    }
  },
  mounted() {
    this.loadWorkers()
    this.timer = setInterval(this.loadWorkers, 5000)
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    loadWorkers() {
      request.get('/workers').then(res => {
        this.workers = res.data
      })
    },
    viewTasks(worker) {
      request.get('/tasks', { params: { workerId: worker.id } }).then(res => {
        this.workerTasks = res.data
        this.taskDialogVisible = true
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
    }
  }
}
</script>
