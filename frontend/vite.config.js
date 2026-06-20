import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      // Any request starting with /api will be forwarded to your backend
      '/api': {
        target: 'http://localhost:8080', // ⚠️ CHANGE 8080 TO YOUR BACKEND PORT
        changeOrigin: true,
        // secure: false, // You might need this if your backend uses HTTPS locally
      }
    }
  }
})