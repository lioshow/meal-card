<template>
  <div>
    <div class="container">
      <el-form style="margin-left: -40px;" :model="searchForm" :rules="searchRules" ref="searchForm" label-width="100px">
        <el-form-item label="类型" prop="key">
          <el-select v-model="searchForm.key" placeholder="请选择搜索类型">
            <el-option label="学号" value="stuNo"></el-option>
            <el-option label="姓名" value="stuName"></el-option>
            <el-option label="卡号" value="cardNo"></el-option>
            <el-option label="班级" value="classInfo"></el-option>
          </el-select>
        </el-form-item>
        <div class="search">
          <el-form-item label="关键字">
            <el-input v-model="searchForm.value" placeholder="请输入搜索内容" style="width: 175px;"></el-input>
            <el-button :loading="loading" type="primary" icon="el-icon-search" class="_button" @click="handleSearch">搜索</el-button>
            <el-button type="primary" icon="el-icon-plus" class="_button" @click="handleAppend">添加</el-button>
          </el-form-item>
        </div>
      </el-form>
    </div>
    <el-table size="small" current-row-key="id" :data="dataList" stripe style="width: 100%;">
      <el-table-column prop="stuNo" label="学号" align="center"></el-table-column>
      <el-table-column prop="password" label="密码" align="center"></el-table-column>
      <el-table-column prop="stuName" label="姓名" align="center"></el-table-column>
      <el-table-column prop="gender" label="性别" align="center"></el-table-column>
      <el-table-column prop="classInfo" label="班级" align="center"></el-table-column>
      <el-table-column prop="cardNo" label="卡号" align="center"></el-table-column>
      <el-table-column label="操作" align="center">
      <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button :loading="loading" type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
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

    <el-dialog title="添加学生信息" :visible.sync="dialogFormVisibleAppend" width="30%" :close-on-click-modal="false">
      <div class="dialog-form">
        <el-form label-width="100px" ref="formData" :model="formData" :rules="formRules">
          <el-form-item label="学号" prop="stuNo">
            <el-input v-model="formData.stuNo"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="formData.password"></el-input>
          </el-form-item>
          <el-form-item label="姓名" prop="stuName">
            <el-input v-model="formData.stuName"></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-input v-model="formData.gender"></el-input>
          </el-form-item>
          <el-form-item label="班级" prop="classInfo">
            <el-input v-model="formData.classInfo"></el-input>
          </el-form-item>
          <el-form-item label="卡号" prop="cardNo">
            <el-input v-model="formData.cardNo"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button :loading="loading" type="primary" @click="submitAppendForm">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="修改学生信息" :visible.sync="dialogFormVisibleEdit" width="30%" :close-on-click-modal="false">
      <div class="dialog-form">
        <el-form label-width="100px" ref="formData" :model="formData" :rules="formRules">
          <el-form label-width="100px" disabled>
            <el-form-item label="学号" prop="stuNo">
              <el-input v-model="formData.stuNo"></el-input>
            </el-form-item>
          </el-form>
          <el-form-item label="密码" prop="password">
            <el-input v-model="formData.password"></el-input>
          </el-form-item>
          <el-form-item label="姓名" prop="stuName">
            <el-input v-model="formData.stuName"></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-input v-model="formData.gender"></el-input>
          </el-form-item>
          <el-form-item label="班级" prop="classInfo">
            <el-input v-model="formData.classInfo"></el-input>
          </el-form-item>
        </el-form>
        <el-form label-width="100px" disabled>
          <el-form-item label="卡号" prop="cardNo">
            <el-input v-model="formData.cardNo"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button :loading="loading" type="primary" @click="submitEditForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  data () {
    return {
      dataList: [], // 存放学生所有数据
      currentPage: 1, // 当前页码
      pageSize: 5, // 每页的记录条数
      total: 0, // 总记录条数
      // 搜索表单
      searchForm: {
        key: '',
        value: '',
        page: '',
        size: 5
      },
      // 添加学生和编辑表单
      formData: {
        stuNo: '',
        password: '',
        stuName: '',
        gender: '',
        classInfo: '',
        cardNo: ''
      },
      formRules: {
        stuNo: [
          { required: true, message: '学号不可为空', trigger: 'blur' },
          { pattern: /^\d{8}$/, message: '学号必须为八位数字', trigger: 'blur' }
        ],
        stuName: [
          { required: true, message: '姓名不可为空', trigger: 'blur' },
          { pattern: '^[\\u4e00-\\u9fa5]+$', message: '姓名只能包含汉字', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不可为空', trigger: 'blur' }
        ],
        gender: [
          { required: true, message: '性别不可为空', trigger: 'blur' },
          { pattern: '^(男|女)$', message: '性别为男或者女', trigger: 'blur' }
        ],
        classInfo: [
          { required: true, message: '班级不可为空', trigger: 'blur' },
          { pattern: '^[\u4e00-\u9fa5]{1,4}\\d{4}$', message: '班级格式为汉字加4位数字，eg：软件2141', trigger: 'blur' }
        ],
        cardNo: [
          { required: true, message: '卡号不可为空', trigger: 'blur' },
          { pattern: /^\d{6}$/, message: '卡号必须为六位数字', trigger: 'blur' }
        ]
      },
      searchRules: {
        key: [
          { required: true, message: '搜索类型不可为空', trigger: 'change' }
        ]
      },
      // 添加弹框
      dialogFormVisibleAppend: false,
      // 编辑弹框
      dialogFormVisibleEdit: false,
      loading: false,
      // 判断是否搜索过，防止搜索后切换页码时调用getAll()
      flag: false
    }
  },
  created () {
    this.getAll()
  },
  methods: {
    // 获取学生列表信息
    getAll () {
      axios.get('http://localhost:8080/student/' + this.currentPage + '/' + this.pageSize).then((res) => {
        // console.log(res)
        this.dataList = res.data.data.data
        this.total = res.data.data.total
        this.dialogFormVisibleAppend = false
        this.dialogFormVisibleEdit = false
        this.formData = {}
      })
    },

    // 打开编辑信息弹框
    handleEdit (row) {
      axios.get('http://localhost:8080/student/' + row.stuNo).then((res) => {
        // console.log(res)
        if (res.data.code) {
          this.dialogFormVisibleEdit = true
          this.formData = res.data.data
        } else {
          this.$message.error(res.data.msg)
          this.getAll()
        }
      })
    },

    handleAppend () {
      this.dialogFormVisibleAppend = true
    },

    // 取消操作
    cancel () {
      this.getAll()
      this.$message.info('操作取消')
    },

    // 提交添加信息
    submitAppendForm () {
      this.$refs.formData.validate(async (valid) => {
        if (valid) {
          this.loading = true
          setTimeout(() => {
            this.loading = false
          }, 3000)
          axios.post('http://localhost:8080/student', this.formData).then((res) => {
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
        }
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

    // 提交修改信息
    submitEditForm () {
      axios.put('http://localhost:8080/student/update', this.formData).then((res) => {
        // console.log(res)
        // 判断是否修改成功
        if (res.data.code) {
          this.$message.success(res.data.msg)
        } else {
          this.$message.error(res.data.msg)
        }
      }).finally(() => {
        this.getAll()
      })
    },

    // 删除操作
    handleDelete (row) {
      this.$confirm('将永久删除当前信息，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.loading = true
        setTimeout(() => { this.loading = false }, 3000)
        axios.delete('http://localhost:8080/student/' + row.stuNo).then((res) => {
          // 判断是否删除成功
          // console.log(res)
          this.loading = false
          if (res.data.code) {
            this.$message.success(res.data.msg)
          } else {
            this.$message.error(res.data.msg)
          }
        }).finally(() => {
          if (parseFloat(this.total - 1) / parseFloat(this.pageSize) <= this.currentPage - 1) {
            this.currentPage = 1
          }
          this.getAll()
        })
      }).catch(() => {
        this.$message.info('操作取消')
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
</style>
