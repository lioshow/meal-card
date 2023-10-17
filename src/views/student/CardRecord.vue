<template>
  <div>

    <!--分页组件-->
    <!-- <div class="pagination-container">
      <el-pagination
        background
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="total, prev, pager, next"
        :total="total">
      </el-pagination>
    </div> -->

    <!--饼图-->
    <div class="container">
      <div id="main" class="echarts-box"></div>
      <div class="_total">
        <h4>消费总计：{{ totalAmount }}元</h4>
      </div>
    </div>

    <el-button @click="dialogFormVisible = true">查看详细记录</el-button>

    <el-dialog title="饭卡记录" :visible.sync="dialogFormVisible">
      <div class="container">
        <el-form style="margin-left: -40px;" :model="searchForm" :rules="searchRules" ref="searchForm" label-width="100px">
          <el-form-item label="类型" prop="key">
            <el-select v-model="searchForm.key" placeholder="请选择搜索类型">
              <el-option label="金额" value="amount"></el-option>
              <el-option label="行为" value="action"></el-option>
            </el-select>
          </el-form-item>
          <div class="search">
            <el-form-item label="关键字">
              <el-input v-model="searchForm.value" placeholder="请输入搜索内容" style="width: 175px;"></el-input>
              <el-button :loading="loading" type="primary" icon="el-icon-search" class="_button" @click="handleSearch">搜索</el-button>
            </el-form-item>
          </div>
        </el-form>
      </div>

      <div>
        <el-table size="small" current-row-key="id" :data="dataList" stripe style="width: 100%;">
          <el-table-column type="index" label="序号" align="center"></el-table-column>
          <el-table-column prop="amount" label="金额" align="center"></el-table-column>
          <el-table-column prop="action" label="行为" align="center"></el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button :loading="loading" type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button :loading="loading" type="primary" @click="back">返 回</el-button>
      </div>
    </el-dialog>

    <!-- <el-dialog title="温馨提示" :visible.sync="dialogFormVisible" width="30%">
      <span>充值金额不可为空</span>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="reset">确 定</el-button>
      </span>
    </el-dialog> -->
  </div>

</template>

<script>
import axios from 'axios'
import * as echarts from 'echarts'
export default {
  data () {
    return {
      myChart: null, // 饼图
      dataList: [], // 存放饭卡所有数据
      list: [], // 存放消费数据
      // currentPage: 1, // 当前页码
      // pageSize: 5, // 每页的记录条数
      // total: 0, // 总记录条数
      // balance: '', // 原金额
      // 记录表单
      formData: {
        id: '',
        cardNo: '',
        amount: '', // 消费金额
        action: '' // 行为
      },
      // 搜索表单
      searchForm: {
        key: '',
        value: '',
        page: '',
        size: 5
      },
      searchRules: {
        key: [
          { required: true, message: '搜索类型不可为空', trigger: 'change' }
        ]
      },
      // 记录弹框
      dialogFormVisible: false,
      loading: false,
      // 判断是否搜索过，防止搜索后切换页码时调用getAll()
      // flag: false,
      // 当前学生
      student: {
        stuNo: '',
        password: '',
        stuName: '',
        gender: '',
        classInfo: '',
        cardNo: ''
      }
    }
  },
  created () {
    this.student = JSON.parse(window.localStorage.getItem('student'))
    // console.log(this.student)
    // console.log(1)
    // console.log(this.list)
    this.getAll()
  },
  computed: {
    totalAmount () {
      return this.list.reduce((sum, item) => sum + (-item.amount), 0)
    }
  },
  mounted () {
    // 消费记录不为空时更新图表
    // console.log(2)
    // console.log(this.list)
    // if (this.list.length > 0) {
    this.myChart = echarts.init(document.getElementById('main'))
    this.myChart.setOption({
      // 大标题
      title: {
        text: '饭卡消费记录',
        left: 'center'
      },
      // 提示框
      tooltip: {
        trigger: 'item'
      },
      // 图例
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      // 数据项
      series: [
        {
          name: '消费账单',
          type: 'pie',
          radius: '50%', // 半径
          data: [],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    })
    // }
  },
  methods: {
    // 获取饭卡记录列表信息
    getAll () {
      axios.get('http://localhost:8080/record', { params: this.student }).then((res) => {
        // console.log(res)
        this.dataList = res.data.data
        // 获取小于0的消费数据
        this.list = this.dataList.filter(item => item.amount < 0)
        // console.log(this.dataList)
        // console.log(3)
        // console.log(this.list)
        // this.total = res.data.data.total
        this.rechargeFormVisible = false
        this.formData = {}
        if (this.list.length > 0) {
          // 创建一个对象来存储合并后的数据
          const mergedData = {}

          // 遍历原始数据并合并重复项
          this.list.forEach(item => {
            const name = item.action
            const amount = item.amount
            if (mergedData[name]) {
              // 如果已经存在该名称，则将值相加
              mergedData[name] += amount
            } else {
              // 否则创建一个新名称
              mergedData[name] = amount
            }
          })
          // 将合并后的数据转换为 ECharts 饼图所需的格式
          const pieData = []
          for (const name in mergedData) {
            pieData.push({ value: -mergedData[name], name: name })
          }
          // 更新图表
          this.myChart.setOption({
            // 数据项
            series: [
              {
                data: pieData
              }
            ]
          })
        } else {
          this.myChart.setOption({
            // 数据项
            series: [
              {
                data: [{ value: 0, name: '暂无数据' }]
              }
            ]
          })
        }
      })
    },

    // 搜索操作
    handleSearch () {
      this.$refs.searchForm.validate(async (valid) => {
        if (valid) {
          this.loading = true
          setTimeout(() => { this.loading = false }, 3000)
          axios.post('http://localhost:8080/record', {
            searchForm: this.searchForm,
            student: this.student
          }).then((res) => {
            // console.log(res)
            this.loading = false
            if (res.data.code) {
              this.dataList = res.data.data
              // this.total = res.data.data.total
              this.$message.success(res.data.msg)
            } else {
              this.$message.error(res.data.msg)
            }
          })
        }
      })
    },

    // // 修改当前页码
    // handleCurrentChange (currentPage) {
    //   this.currentPage = currentPage
    //   if (!this.flag) this.getAll()
    //   else {
    //     this.searchForm.page = this.currentPage
    //     const params = {
    //       param1: this.searchForm,
    //       param2: this.student
    //     }
    //     axios.get('http://localhost:8080/record/search', { params }).then((res) => {
    //       // console.log(res)
    //       this.dataList = res.data.data.data
    //       this.total = res.data.data.total
    //     })
    //   }
    // },

    // 删除操作
    handleDelete (row) {
      this.$confirm('将永久删除当前信息，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.loading = true
        setTimeout(() => { this.loading = false }, 3000)
        axios.delete('http://localhost:8080/record/' + row.id).then((res) => {
          // 判断是否删除成功
          // console.log(res)
          this.loading = false
          if (res.data.code) {
            this.$message.success(res.data.msg)
          } else {
            this.$message.error(res.data.msg)
          }
        }).finally(() => {
          // 2.显示数据
          this.getAll()
        })
      }).catch(() => {
        this.$message.info('操作取消')
      })
    },

    // 取消操作
    cancel () {
      this.getAll()
      this.$message.info('操作取消')
    },

    // 返回操作
    back () {
      this.dialogFormVisible = false
      this.getAll()
    },

    // 关闭弹框重置表单
    reset () {
      // 关闭弹框
      this.dialogFormVisible = false
      // 关闭加载
      this.loading = false
    }
  }
}
</script>

<style lang="less" scoped>
  .container {
    position: relative;
    margin-top: 20px;
    border: 0px solid red;
  }

  .search {
    border: 0px solid red;
    width: 600px;
    height: 60px;
    top: -63px;
    position: relative;
    left: 250px;
  }

  ._button {
    position: relative;
    left: 10px;
  }

  .el-select {
    width: 160px;
    float: left;
  }
  .dialog-form {
    overflow: auto;
  }

  .pagination-container {
    margin-top: 20px;
    float: right;
  }

  .echarts-box {
    width: 600px;
    height: 400px;
    padding: 30px;
    margin: 0 auto;
    border: 1px solid #ccc;
  }

  ._total {
    position: absolute;
    right: 120px;
    margin-bottom: 500px;
  }
</style>
