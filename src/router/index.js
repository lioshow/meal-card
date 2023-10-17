import Vue from 'vue'
import VueRouter from 'vue-router'

import Login from '@/views/Login'
import Home from '@/views/Home'

import Admin from '@/views/admin/Admin'
import Student from '@/views/student/Student'

import AdminPersonalInfo from '@/views/admin/PersonalInfo'
import StudentManager from '@/views/admin/StudentManager'
import CardManager from '@/views/admin/CardManager'
import RecordManager from '@/views/admin/RecordManager'

import StudentPersonalInfo from '@/views/student/PersonalInfo'
import CardCharge from '@/views/student/CardCharge'
import CardLoss from '@/views/student/CardLoss'
import CardRecord from '@/views/student/CardRecord'
import CardConsumption from '@/views/student/CardConsumption'

Vue.use(VueRouter)

// 配置路由
const router = new VueRouter({
  routes: [
    {
      path: '/',
      component: Home,

      // 重定向
      redirect: '/login'
    },
    { path: '/login', name: '登录', component: Login },
    {
      path: '/admin',
      component: Admin,
      // 重定向，配置默认页面
      redirect: '/admin/studentManager',
      children: [
        { path: '/admin/personalInfo', name: '管理员信息', component: AdminPersonalInfo },
        { path: '/admin/studentManager', name: '学生管理', component: StudentManager },
        { path: '/admin/cardManager', name: '饭卡管理', component: CardManager },
        { path: '/admin/recordManager', name: '记录管理', component: RecordManager }
      ]
    },
    {
      path: '/student',
      component: Student,
      redirect: 'student/cardRecord',
      children: [
        { path: '/student/personalInfo', name: '学生个人信息', component: StudentPersonalInfo },
        { path: '/student/cardCharge', name: '饭卡充值', component: CardCharge },
        { path: '/student/cardLoss', name: '饭卡挂失', component: CardLoss },
        { path: '/student/cardRecord', name: '饭卡记录', component: CardRecord },
        { path: '/student/cardConsumption', name: '饭卡消费', component: CardConsumption }
      ]
    }
  ]
})

export default router
