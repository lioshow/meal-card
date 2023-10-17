<template>
  <!--  -->
<el-container class="container">
  <el-header class="header">
    <div class="icon">
      <i class="el-icon-s-home"></i>
    </div>
    <div class="title">湖北经济学院饭卡管理系统-管理员</div>
    <div class="userinfoContainer">
      <span class="userinfo1">欢迎您，</span>
      <el-dropdown size="medium">
        <span class="el-dropdown-link userinfo2">
          {{admin.name}}<i class="el-icon-arrow-down el-icon--right userinfo2"></i>
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
        <el-menu-item index="/admin/personalInfo">
          <i class="el-icon-user-solid"></i>
          <span slot="title">个人信息</span>
        </el-menu-item>
        <el-submenu index="">
          <template slot="title">
            <i class="el-icon-s-tools"></i>
            <span>系统管理</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="/admin/studentManager" :class="$route.path=='/admin/studentManager'?'is-active':''">
              <i class="el-icon-user"></i>
              <span>学生管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/cardManager" :class="$route.path=='/admin/cardManager'?'is-active':''">
              <i class="el-icon-bank-card"></i>
              <span>饭卡管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/recordManager" :class="$route.path=='/admin/recordManager'?'is-active':''">
              <i class="el-icon-pie-chart"></i>
              <span>记录管理</span>
            </el-menu-item>
          </el-menu-item-group>
        </el-submenu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-main>
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/admin/personalInfo'}">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{ this.$router.currentRoute.name }}</el-breadcrumb-item>
        </el-breadcrumb>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</el-container>
</template>

<script>
export default {
  name: 'AdminIndex',

  data () {
    return {
      admin: ''
    }
  },

  created () {
    this.admin = JSON.parse(window.localStorage.getItem('admin'))
  },
  methods: {
    logout () {
      this.$confirm('确定退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('admin')
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
