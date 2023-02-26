import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react({
    jsxImportSource: '@emotion/react',
    babel: {
      plugins: ['@emotion/babel-plugin'],
    },
  })],
  server: {
    port: 3030,
    proxy: {
      '/api': 'http://3.39.213.62:8080' // localhost에서 바꿔놨어용
    }
  }
})
