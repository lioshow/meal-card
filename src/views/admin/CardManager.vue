<template>
  <div>
    <div class="container">
      <el-form style="margin-left: -40px;" :model="searchForm" :rules="searchRules" ref="searchForm" label-width="100px">
        <el-form-item label="类型" prop="key">
          <el-select v-model="searchForm.key" placeholder="请选择搜索类型">
            <el-option label="卡号" value="cardNo"></el-option>
            <el-option label="学号" value="stuNo"></el-option>
            <el-option label="姓名" value="stuName"></el-option>
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
    <el-table size="small" :cell-style="cellStyle" current-row-key="id" :data="dataList" stripe style="width: 100%;">
      <el-table-column prop="cardNo" label="卡号" align="center"></el-table-column>
      <el-table-column prop="stuNo" label="学号" align="center"></el-table-column>
      <el-table-column prop="stuName" label="姓名" align="center"></el-table-column>
      <el-table-column prop="balance" label="余额" align="center"></el-table-column>
      <el-table-column prop="state" label="状态" align="center"></el-table-column>
      <el-table-column label="操作" align="center">
      <template slot-scope="scope">
        <div class="button-group">
          <el-button type="primary" size="mini" @click="handleRecharge(scope.row)">充值</el-button>
          <el-button :loading="loading" type="success" size="mini" @click="unhook(scope.row)">解挂</el-button>
          <el-button :loading="loading" type="danger" size="mini" @click="hook(scope.row)">挂失</el-button>
        </div>
      </template>
      </el-table-column>
    </el-table>

    <!--分页组件-->
    <div class="pagination-container">
      <el-pagination
        background
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="total, prev, pager, next"
        :total="total">
      </el-pagination>
    </div>

    <el-dialog title="充值饭卡" :visible.sync="rechargeFormVisible" width="30%">
      <div class="dialog-form">
        <el-form label-width="100px">
          <el-form-item label="充值金额" prop="balance">
            <el-input v-model="formData.balance"></el-input>
          </el-form-item>
        </el-form>
        <!-- <img src="@/assets/recharge.jpg" alt="" style="height: 500px; width: 400px;"> -->
      </div>
      <div slot="footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button :loading="loading" type="primary" @click="recharge">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="温馨提示" :visible.sync="dialogFormVisible" width="30%">
      <span>充值金额必须非空且只能为正整数，请检查输入是否正确</span>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="reset">确 定</el-button>
      </span>
    </el-dialog>
  </div>

</template>

<script>
import axios from 'axios'
export default {
  data () {
    return {
      dataList: [], // 存放饭卡所有数据
      currentPage: 1, // 当前页码
      pageSize: 5, // 每页的记录条数
      total: 0, // 总记录条数
      balance: '', // 原金额
      // 饭卡表单
      formData: {
        cardNo: '',
        stuNo: '',
        balance: '', // 充值金额
        state: '' // 状态
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
      // 提示弹框
      dialogFormVisible: false,
      // 充值弹框
      rechargeFormVisible: false,
      loading: false,
      // 判断是否搜索过，防止搜索后切换页码时调用getAll()
      flag: false
    }
  },
  created () {
    this.getAll()
  },
  methods: {
    // 获取饭卡列表信息
    getAll () {
      axios.get('http://localhost:8080/student/' + this.currentPage + '/' + this.pageSize).then((res) => {
        // console.log(res)
        this.dataList = res.data.data.data
        this.total = res.data.data.total
        this.rechargeFormVisible = false
        this.formData = {}
      })
    },

    // 搜索操作
    handleSearch () {
      this.$refs.searchForm.validate(async (valid) => {
        if (valid) {
          this.loading = true
          setTimeout(() => { this.loading = false }, 3000)
          this.searchForm.page = 1
          axios.get('http://localhost:8080/student/search', { params: this.searchForm }).then((res) => {
            // console.log(res)
            this.loading = false
            this.flag = true
            if (res.data.code) {
              this.dataList = res.data.data.data
              this.total = res.data.data.total
              this.$message.success(res.data.msg)
            } else {
              this.$message.error(res.data.msg)
            }
          }).finally(() => {
            this.currentPage = 1
          })
        }
      })
    },

    // 修改当前页码
    handleCurrentChange (currentPage) {
      this.currentPage = currentPage
      if (!this.flag) this.getAll()
      else {
        this.searchForm.page = this.currentPage
        axios.get('http://localhost:8080/student/search', { params: this.searchForm }).then((res) => {
          // console.log(res)
          this.dataList = res.data.data.data
          this.total = res.data.data.total
        })
      }
    },

    // 充值
    handleRecharge (row) {
      if (row.state === '已挂失') {
        this.$message.error('当前饭卡处于挂失状态，无法进行此操作')
      } else {
        axios.get('http://localhost:8080/card/' + row.cardNo).then((res) => {
          // console.log(res)
          this.balance = row.balance
          // console.log(this.balance)
          if (res.data.code) {
            this.rechargeFormVisible = true
            // 将获取的结果存入饭卡表单，但只显示金额
            this.formData = res.data.data
            this.formData.balance = ''
            // 将表单设为空，直接输入充值金额
          } else {
            this.$message.error(res.data.msg)
            this.getAll()
          }
        })
      }
    },

    // 判断值是否为正整数
    isInteger (value) {
      if (isNaN(value)) {
        return false // 不是数字
      }
      return Number.isInteger(parseFloat(value)) && parseInt(value) > 0
    },

    // 充值操作
    recharge () {
      this.loading = true
      setTimeout(() => { this.loading = false }, 3000)
      if (this.isInteger(this.formData.balance)) {
        this.formData.balance = parseInt(this.formData.balance) + parseInt(this.balance)
        // console.log(this.formData.balance)
        axios.put('http://localhost:8080/card', this.formData).then((res) => {
          // console.log(res)
          this.loading = false
          if (res.data.code) {
            this.$message.success(res.data.msg)
          } else {
            this.$message.error(res.data.msg)
          }
        }).finally(() => {
          this.getAll()
        })
      } else {
        this.$message.warning('充值金额必须非空且只能为正整数，请检查输入是否正确')
      }
    },

    // 解挂
    unhook (row) {
      if (row.state === '正常') {
        this.$message.error('当前饭卡处于正常状态，无法进行此操作')
      } else {
        this.$confirm('是否解挂', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        }).then(() => {
          this.loading = true
          setTimeout(() => { this.loading = false }, 3000)
          this.formData.stuNo = row.stuNo
          this.formData.cardNo = row.cardNo
          this.formData.balance = row.balance
          this.formData.state = row.state
          axios.put('http://localhost:8080/card/unhook', this.formData).then((res) => {
            // console.log(res)
            this.loading = false
            if (res.data.code) {
              this.$message.success(res.data.msg)
            } else {
              this.$message.error(res.data.msg)
            }
          }).finally(() => {
            this.getAll()
          })
        }).catch(() => {
          this.$message.info('操作取消')
        })
      }
    },

    // 挂失操作
    hook (row) {
      if (row.state === '已挂失') {
        this.$message.error('当前饭卡处于挂失状态，无法进行此操作')
      } else {
        this.$confirm('是否立即挂失', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        }).then(() => {
          this.loading = true
          setTimeout(() => { this.loading = false }, 3000)
          this.formData.stuNo = row.stuNo
          this.formData.cardNo = row.cardNo
          this.formData.balance = row.balance
          this.formData.state = row.state
          axios.put('http://localhost:8080/card/loss', this.formData).then((res) => {
            // console.log(res)
            this.loading = false
            if (res.data.code) {
              this.$message.success(res.data.msg)
            } else {
              this.$message.error(res.data.msg)
            }
          }).finally(() => {
            this.getAll()
          })
        }).catch(() => {
          this.$message.info('操作取消')
        })
      }
    },

    // 取消操作
    cancel () {
      this.getAll()
      this.$message.info('操作取消')
    },

    // 关闭弹框重置表单
    reset () {
      // 关闭弹框
      this.dialogFormVisible = false
      // 关闭加载
      this.loading = false
    },

    cellStyle ({ row, column, rowIndex, columnIndex }) {
      if (column.property === 'state') {
        return row.state === '正常' ? 'color: green;' : 'color: red;'
      }
      return '' // 返回空字符串以应用默认样式
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

  .button-group {
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .button-group .el-button {
    margin-right: 5px; /* 可以调整按钮之间的间距 */
  }
</style>
