<template>
  <div class="info">
    <el-form :model="formData" label-width="100px" disabled>
      <el-form-item label="账号" prop="name">
        <el-input v-model="formData.name"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="formData.password"></el-input>
      </el-form-item>
    </el-form>
    <el-form label-width="100px">
      <el-form-item>
        <el-button type="primary" @click="updateForm">修改</el-button>
        <el-button :loading="loading" @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>

    <el-dialog title="修改密码" :visible.sync="dialogFormVisible" width="30%" :close-on-click-modal="false"><!--设为false防止点击空白关闭弹框-->
      <div class="dialog-form">
        <el-form disabled>
          <el-form-item label="账号" prop="name" label-width="100px">
            <el-input type="name" v-model="formData.name"></el-input>
          </el-form-item>
        </el-form>
        <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="100px">
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="ruleForm.password"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="checkPass">
            <el-input type="password" v-model="ruleForm.checkPass"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button :loading="loading" type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  data () {
    return {
      // 个人信息表单
      formData: {
        name: '',
        password: ''
      },
      // 修改密码表单
      ruleForm: {
        password: '',
        checkPass: ''
      },
      rules: {
        password: [
          { required: true, message: '密码不可为空', trigger: 'blur' }
        ],
        checkPass: [
          { required: true, message: '确认密码不可为空', trigger: 'blur' }
        ]
      },
      // 设置弹框是否可见
      dialogFormVisible: false,
      // 提交时开启加载，防止多次请求
      loading: false
    }
  },
  created () {
    this.getInfo()
  },
  methods: {
    getInfo () {
      this.formData = JSON.parse(window.localStorage.getItem('admin'))
    },
    // 打开修改弹框
    updateForm () {
      this.dialogFormVisible = true
      this.ruleForm.password = this.formData.password
      this.ruleForm.checkPass = this.formData.password
    },
    resetForm () {
      this.$confirm('确定重置密码吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        setTimeout(() => {
          this.loading = false
        }, 3000)
        axios.put('http://localhost:8080/admin/reset', this.formData).then((res) => {
          // console.log(res)
          this.loading = false
          if (res.data.code) {
            this.$message.success(res.data.msg)
            this.formData.password = res.data.data.password
          } else {
            this.$message.error(res.data.msg)
          }
        })
      }).catch(() => {
        this.$message.info('操作取消')
      })
    },
    cancel () {
      // 用户信息回显
      this.getInfo()
      // 关闭弹框
      this.dialogFormVisible = false
      this.$message.info('操作取消')
    },
    submitForm () {
      this.$refs.ruleForm.validate(async (valid) => {
        if (valid) {
          // 3秒后可重复请求
          this.loading = true
          setTimeout(() => {
            this.loading = false
          }, 3000)
          if (this.ruleForm.password !== this.ruleForm.checkPass) {
            this.$message.error('两次密码不一致')
          } else {
            this.formData.password = this.ruleForm.checkPass
            axios.put('http://localhost:8080/admin/update', this.formData).then((res) => {
              this.loading = false
              // console.log(res)
              this.$message.success(res.data.msg)
            }).finally(() => {
              this.dialogFormVisible = false
            })
          }
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
  .info {
    margin-top: 60px;
    margin-left: 330px;
    width: 300px;
    height: 500px;
    border: 0px solid;
  }

  .dialog-form {
    height: 30vh;
    overflow: auto;
  }

</style>
