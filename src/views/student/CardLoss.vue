<template>
  <div class="info">
    <el-descriptions class="margin-top" title="个人信息" direction="vertical" :column="3" :size="size" border>
    <template slot="extra">
    <el-button :loading="loading" type="primary" @click="loss">立即挂失</el-button>
    </template>
    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-user"></i>姓名
      </template>
      {{ formData.stuName }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-s-custom"></i>学号
      </template>
      {{ formData.stuNo }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-s-data"></i>卡号
      </template>
      {{ formData.cardNo }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-male"></i>性别
      </template>
      {{ formData.gender }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-school"></i>班级
      </template>
      {{ formData.classInfo }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-wallet"></i>余额
      </template>
      {{ formData.balance }}元
    </el-descriptions-item>
    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-edit"></i>状态
      </template>
      <span :class="cardStatusClass">{{ formData.state }}</span>
    </el-descriptions-item>
  </el-descriptions>
  </div>
</template>
<script>
import axios from 'axios'
export default {
  data () {
    return {
      dataList: [], // 表格数据
      // 表单信息
      formData: {
        stuName: '',
        stuNo: '',
        cardNo: '',
        gender: '',
        classInfo: '',
        balance: '',
        state: ''
      },
      // 当前学生信息
      student: {
        stuNo: '',
        password: '',
        stuName: '',
        gender: '',
        classInfo: '',
        cardNo: ''
      },
      // 当前饭卡信息
      card: {
        cardNo: '',
        stuNo: '',
        balance: '',
        state: ''
      },
      loading: false
    }
  },
  created () {
    this.student = JSON.parse(window.localStorage.getItem('student'))
    this.init()
  },
  computed: {
    cardStatusClass () {
      return this.formData.state === '正常' ? 'green-text' : 'red-text'
    }
  },
  methods: {
    // 初始化所有信息
    init () {
      axios.get('http://localhost:8080/card/' + this.student.cardNo).then((res) => {
        // console.log(res)
        this.card = res.data.data
        // console.log(this.card)
        this.dataList = res.data.data
        this.formData.stuName = this.student.stuName
        this.formData.stuNo = this.student.stuNo
        this.formData.cardNo = this.student.cardNo
        this.formData.gender = this.student.gender
        this.formData.classInfo = this.student.classInfo
        this.formData.balance = this.card.balance
        this.formData.state = this.card.state
      })
    },

    // 挂失操作
    loss () {
      if (this.card.state === '已挂失') {
        this.$message.error('当前饭卡处于挂失状态，无法进行此操作')
      } else {
        this.$confirm('是否立即挂失', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        }).then(() => {
          this.loading = true
          setTimeout(() => { this.loading = false }, 3000)
          // console.log(this.formData.state)
          axios.put('http://localhost:8080/card/loss', this.card).then((res) => {
            // console.log(res)
            this.loading = false
            if (res.data.code) {
              this.$message.success(res.data.msg)
            } else {
              this.$message.error(res.data.msg)
            }
          }).finally(() => {
            this.init()
          })
        }).catch(() => {
          this.$message.info('操作取消')
        })
      }
    }
  }
}
</script>

<style lang="less" scoped>
  .info {
    margin-top: 60px;
    // margin-left: 330px;
    // width: 500px;
    // border: 0px solid;
  }

  .el-button {
    margin-left: 120px;
    width: 160px;
  }

  .green-text {
    color: green;
  }

  .red-text {
    color: red;
  }
</style>
