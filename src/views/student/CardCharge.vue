<template>
  <div>
    <ul class="msg-box">
      <li>
        <h4>饭卡余额：{{ card.balance }}元</h4>
      </li>
      <li>
        <h4 style="margin-bottom: 15px;">充值金额</h4>
          <el-radio-group v-model="rechargeMethod.amount" @change="amountChange">
            <el-radio border :label="10">充值10元</el-radio>
            <el-radio border :label="20">充值20元</el-radio>
            <el-radio border :label="30">充值30元</el-radio>
            <el-radio border :label="50">充值50元</el-radio>
            <el-radio border :label="100">充值100元</el-radio>
            <el-radio border :label="''">自定义</el-radio>
          </el-radio-group>
      </li>
      <li>
        <h4 style="margin-bottom: 15px;">充值方式</h4>
          <el-radio-group v-model="rechargeMethod.rechargeType" @change="typeChange">
          <el-radio border :label="0">微信</el-radio>
          <el-radio border :label="1">支付宝</el-radio>
        </el-radio-group>
      </li>
      <img v-if="rechargeMethod.rechargeType == 1" src="@/assets/recharge-alipay.jpg" alt="" style="height: 300px; width: 200px;">
      <img v-else-if="rechargeMethod.rechargeType == 0" src="@/assets/recharge-wechat.jpg" alt="" style="height: 300px; width: 200px;">
      <li>
        <h4 style="margin-bottom: 15px;">充值金额</h4>
        <el-input :disabled="disabled" clearable v-model="rechargeMethod.amount" placeholder="请输入金额" style="width: 150px;"></el-input>
      </li>
    </ul>
    <div style="text-align: center; margin-top: 30px;">
      <el-button :loading="loading" type="primary" @click="recharge">确认充值</el-button>
    </div>

    <el-dialog title="温馨提示" :visible.sync="dialogFormVisible" width="30%">
      <span>充值金额必须非空且只能为正整数，请检查输入是否正确</span>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
 </template>

<script>
import axios from 'axios'
export default {
  data () {
    return {
      disabled: false, // 设置自定义充值时是否能够输入自定义金额
      // 充值方法
      rechargeMethod: {
        amount: null, // 充值金额
        rechargeType: null // 0为微信支付，1为支付宝支付
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
      loading: false,
      dialogFormVisible: false,
      rules: {
        amount: [
          { required: true, message: '请选择充值金额', trigger: 'change' }
          // {
          //   validator: (rule, value, callback) => {
          //     if (value === '') {
          //     // 用户选择了自定义金额，需要输入
          //       callback(new Error('请输入自定义金额'))
          //     } else if (isNaN(value) || !Number.isInteger(parseFloat(value)) || value <= 0) {
          //     // 输入的值不是正整数
          //       callback(new Error('请输入有效的正整数金额'))
          //     } else {
          //     // 通过校验
          //       callback()
          //     }
          //   },
          //   trigger: 'change'
          // }
        ]
      }
    }
  },
  created () {
    this.student = JSON.parse(window.localStorage.getItem('student'))
    this.getCard()
  },
  methods: {
    // 选择金额
    amountChange (value) {
      this.rechargeMethod.amount = value
      if (value === '') {
        this.disabled = false
      } else {
        this.disabled = true
      }
      // console.log(this.rechargeMethod.amount)
    },

    // 选择充值方式
    typeChange (value) {
      this.rechargeMethod.rechargeType = value
      // console.log(this.rechargeMethod.rechargeType)
    },

    // 判断值是否为正整数
    isInteger (value) {
      if (isNaN(value)) {
        return false // 不是数字
      }
      return Number.isInteger(parseFloat(value)) && parseInt(value) > 0
    },

    // 获取学生饭卡
    getCard () {
      axios.get('http://localhost:8080/card/' + this.student.cardNo).then((res) => {
        // console.log(res)
        this.card = res.data.data
        // console.log(this.card)
        this.rechargeMethod.amount = null
        this.rechargeMethod.rechargeType = null
      })
    },

    // 充值
    recharge () {
      if (this.card.state === '已挂失') {
        this.$message.error('当前饭卡处于挂失状态，无法进行此操作')
      } else if (this.isInteger(this.rechargeMethod.amount)) {
        this.$confirm('是否确认充值', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        }).then(() => {
          this.loading = true
          setTimeout(() => { this.loading = false }, 3000)
          this.card.balance = parseInt(this.rechargeMethod.amount) + parseInt(this.card.balance)
          axios.put('http://localhost:8080/card', this.card).then((res) => {
            // console.log(res)
            this.loading = false
            if (res.data.code) {
              this.$message.success(res.data.msg)
            } else {
              this.$message.error(res.data.msg)
            }
          }).finally(() => {
            this.getCard()
          })
        }).catch(() => {
          this.$message.info('操作取消')
        })
      } else {
        this.$message.warning('充值金额必须非空且只能为正整数，请检查输入是否正确')
      }
    }
  }
}
</script>

<style lang="less" scoped>
  .msg-box > li{
    list-style: none;
    border-bottom: 1px solid #c5c5c5;
    padding: 20px 10px;
  }

  .dialog-form {
    overflow: auto;
  }
</style>
