import Vue from 'vue'
import VueRouter from 'vue-router'
import Dashboard from '../views/Dashboard.vue'
import Bikes from '../views/Bikes.vue'
import Parking from '../views/Parking.vue'
import Tasks from '../views/Tasks.vue'
import Workers from '../views/Workers.vue'
import Abnormal from '../views/Abnormal.vue'
import Statistics from '../views/Statistics.vue'

Vue.use(VueRouter)

const routes = [
  { path: '/', redirect: '/dashboard' },
  { path: '/dashboard', component: Dashboard },
  { path: '/bikes', component: Bikes },
  { path: '/parking', component: Parking },
  { path: '/tasks', component: Tasks },
  { path: '/workers', component: Workers },
  { path: '/abnormal', component: Abnormal },
  { path: '/statistics', component: Statistics }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
