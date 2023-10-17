<template>
  <div>
    <h2>饭卡管理系统</h2>
    <el-form ref="loginForm" :model="form" :rules="rules" label-width="80px" class="login-box">
      <h3 class="login-title">欢迎登录</h3>
      <el-form-item label="帐号" prop="username">
        <el-input type="text" placeholder="请输入账号" v-model="form.username"/>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" placeholder="请输入密码" v-model="form.password" prefix-icon="el-icon-lock" show-password/>
      </el-form-item>
      <el-form-item>
          <el-radio v-model="type" label="Student">学生</el-radio>
          <el-radio style="left: 80px;" v-model="type" label="Admin">管理员</el-radio>
      </el-form-item>
      <el-form-item>
        <el-button :loading="loading" type="primary" @click="login">登录</el-button>
        <el-button style="position: relative; left: 80px;" @click="register">注册</el-button>
      </el-form-item>
    </el-form>

    <el-dialog title="温馨提示" :visible.sync="dialogVisible" width="30%">
      <span>请选择身份</span>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="reset">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="温馨提示" :visible.sync="registerVisible" width="30%">
      <span>请联系管理员进行注册-_-!</span>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="reset">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'LoginIndex',
  data () {
    return {
      // 表单数据
      form: {
        username: '',
        password: ''
      },
      // 类型
      type: '',
      // 表单验证，需要在 el-form-item 元素中增加 prop 属性
      // required: 是否必填项，message：不通过时的提示信息，trigger: blur失去焦点触发，change值改变时触发
      rules: {
        username: [
          { required: true, message: '账号不可为空', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不可为空', trigger: 'blur' }
        ]
      },
      // 对话框显示和隐藏
      dialogVisible: false,
      registerVisible: false,
      //  登录时加载，防止多次点击
      loading: false
    }
  },
  methods: {
    // 对学生和管理员登录方法进行封装
    // 管理员登录
    adminLogin () {
      axios.get('http://localhost:8080/admin/login', { params: this.form }).then((res) => {
        // console.log(res)
        // 登录成功的处理逻辑
        this.loading = false
        if (res.data.code) {
          this.$message.success(res.data.msg)
          localStorage.setItem('admin', JSON.stringify(res.data.data))
          this.$router.push({ path: '/admin' })
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },

    // 学生登录
    studentLogin () {
      axios.get('http://localhost:8080/student/login', { params: this.form }).then((res) => {
        // console.log(res)
        this.loading = false
        if (res.data.code) {
          this.$message.success(res.data.msg)
          localStorage.setItem('student', JSON.stringify(res.data.data))
          this.$router.push({ path: '/student' })
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    // 登录
    login () {
      // 表单校验
      this.$refs.loginForm.validate(async (valid) => {
        if (valid) {
          // 打开加载属性，防止多次请求，3秒后可重新请求
          this.loading = true
          setTimeout(() => {
            this.loading = false
          }, 3000)
          if (this.type === 'Admin') {
            // 勾选管理员
            this.adminLogin()
          } else if (this.type === 'Student') {
            // 勾选学生
            this.studentLogin()
          } else {
            // 未勾选，弹出提示框
            this.dialogVisible = true
          }
        }
      })
    },

    register () {
      this.registerVisible = true
    },

    // 关闭弹框重置表单
    reset () {
      // 关闭弹框
      this.dialogVisible = false
      this.registerVisible = false
      // 关闭加载
      this.loading = false
    }
  }
}
</script>
<style lang="less" scoped>
  .login-box {
    border: 1px solid #DCDFE6;
    width: 350px;
    margin: 180px auto;
    padding: 35px 35px 15px 35px;
    border-radius: 5px;
    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
    box-shadow: 0 0 25px #909399;
  }

  .login-title {
    text-align: center;
    margin: 0 auto 40px auto;
    color: #303133;
  }
</style>
