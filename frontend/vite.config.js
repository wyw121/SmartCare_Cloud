import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'
import { defineConfig } from 'vite'

export default defineConfig({
  plugins: [
    vue()
  ],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  server: {
    port: 3000,
    host: true,
    proxy: {
      '/elderly': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      },
      '/doctor': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      },
      '/health-warning': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      },
      '/dashboard': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      },
      '/reports': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      },
      '/system': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      }
    }
  },
  build: {
    target: 'es2015',
    outDir: 'dist',
    assetsDir: 'assets',
    sourcemap: false,
    chunkSizeWarningLimit: 1500,
    rollupOptions: {
      output: {
        chunkFileNames: 'assets/js/[name]-[hash].js',
        entryFileNames: 'assets/js/[name]-[hash].js',
        assetFileNames: 'assets/[ext]/[name]-[hash].[ext]'
      }
    }
  }
})
