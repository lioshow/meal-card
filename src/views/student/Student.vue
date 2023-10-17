<template>
  <!--  -->
<el-container class="container">
  <el-header class="header">
    <div class="icon">
      <i class="el-icon-s-home"></i>
    </div>
    <div class="title">湖北经济学院饭卡管理系统-学生端</div>
    <div class="userinfoContainer">
      <span class="userinfo1">欢迎您，</span>
      <el-dropdown size="medium">
        <span class="el-dropdown-link userinfo2">
          {{ student.stuName }}<i class="el-icon-arrow-down el-icon--right userinfo2"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item icon="el-icon-s-fold" @click.native="logout">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </el-header>
  <el-container>
    <el-aside class="aside">
      <el-menu class="el-menu-vertical-demo" router>
        <el-menu-item index="/student/personalInfo">
          <i class="el-icon-user-solid"></i>
          <span slot="title">个人信息</span>
        </el-menu-item>
        <el-submenu index="">
          <template slot="title">
            <i class="el-icon-s-tools"></i>
            <span>饭卡管理</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="/student/cardCharge" :class="$route.path=='/student/cardCharge'?'is-active':''">
              <i class="el-icon-wallet"></i>
              <span>饭卡充值</span>
            </el-menu-item>
            <el-menu-item index="/student/cardLoss" :class="$route.path=='/student/cardLoss'?'is-active':''">
              <i class="el-icon-s-operation"></i>
              <span>饭卡挂失</span>
            </el-menu-item>
            <!-- <el-menu-item index="/student/cardConsumption" :class="$route.path=='/student/cardConsumption'?'is-active':''">
              <i class="el-icon-bank-card"></i>
              <span slot="title">饭卡消费</span>
            </el-menu-item> -->
            <el-menu-item index="/student/cardRecord" :class="$route.path=='/student/cardRecord'?'is-active':''">
              <i class="el-icon-pie-chart"></i>
              <span>饭卡记录</span>
            </el-menu-item>
          </el-menu-item-group>
        </el-submenu>
      </el-menu>
    </el-aside>
    <el-main>
      <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/student/personalInfo'}">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{ this.$router.currentRoute.name }}</el-breadcrumb-item>
      </el-breadcrumb>
      <router-view></router-view>
    </el-main>
  </el-container>
</el-container>
</template>

<script>
export default {
  name: 'StudentIndex',

  data () {
    return {
      student: ''
    }
  },

  created () {
    this.student = JSON.parse(window.localStorage.getItem('student'))
  },

  methods: {
    logout () {
      this.$confirm('确定退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('student')
        this.$message.success('退出成功')
        this.$router.replace({ path: '/' })
      }).catch(() => {
        this.$message.info('操作取消')
      })
    }
  }
}

</script>

<style scoped>
  /** 封装css */
  @import '@/common/style.css'
</style>
