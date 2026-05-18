<template>
  <div class="abnormal">
    <h2>异常记录</h2>
    <el-card>
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="状态">
          <el-select v-model="searchForm.resolved" placeholder="全部" clearable @change="loadRecords">
            <el-option label="未处理" :value="false"></el-option>
            <el-option label="已处理" :value="true"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <el-table :data="records" border stripe>
        <el-table-column prop="id" label="记录ID" width="180"></el-table-column>
        <el-table-column prop="bikeId" label="车辆ID" width="100"></el-table-column>
        <el-table-column prop="parkingSpotId" label="停车点ID" width="120"></el-table-column>
        <el-table-column prop="area" label="区域" width="80"></el-table-column>
        <el-table-column prop="description" label="异常描述" min-width="200"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
        <el-table-column prop="resolved" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.resolved ? 'success' : 'danger'">{{ scope.row.resolved ? '已处理' : '未处理' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="resolveTime" label="处理时间" width="160"></el-table-column>
        <el-table-column label="操作" width="100">
          <template slot-scope="scope">
            <el-button size="mini" type="success" @click="resolveRecord(scope.row)" v-if="!scope.row.resolved">处理</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import request from '../api/request'

export default {
  name: 'Abnormal',
  data() {
    return {
      records: [],
      searchForm: {
        resolved: null
      }
    }
  },
  mounted() {
    this.loadRecords()
    this.timer = setInterval(this.loadRecords, 5000)
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    loadRecords() {
      request.get('/abnormal-records', { params: this.searchForm }).then(res => {
        this.records = res.data
      })
    },
    resolveRecord(record) {
      this.$confirm('确认处理该异常?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.post(`/abnormal-records/${record.id}/resolve`).then(res => {
          this.$message.success('异常已处理')
          this.loadRecords()
        })
      })
    }
  }
}
</script>
