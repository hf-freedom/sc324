const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 3005,
    proxy: {
      '/api': {
        target: 'http://localhost:8005',
        changeOrigin: true
      }
    }
  }
})
