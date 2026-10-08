import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Proxy hacia Tomcat (puerto 8080) para evitar problemas de CORS.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/auth': {
        target: 'http://localhost:8080/AA5EV01LoginService',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/auth/, ''),
      },
      '/stock': {
        target: 'http://localhost:8080/StockControlAPI',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/stock/, ''),
      },
    },
  },
})
