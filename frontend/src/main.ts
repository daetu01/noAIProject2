import { createPinia } from 'pinia'
import { createApp } from 'vue'

import App from './App.vue'
import router from './router'

import '@/utils/echarts'
import '@/assets/styles/main.css'

createApp(App).use(createPinia()).use(router).mount('#app')
