import { defineConfig } from 'vite'
import { resolve } from 'path'

export default defineConfig({
  root: '.',
  build: {
    outDir: 'dist',
    emptyOutDir: true,
    rollupOptions: {
      input: {
        main: resolve(__dirname, 'index.html'),
        clients: resolve(__dirname, 'clients.html'),
        prospects: resolve(__dirname, 'prospects.html')
      }
    }
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  },
  resolve: {
    alias: {
      '@': resolve(__dirname, './js'),
      '@styles': resolve(__dirname, './scss'),
      '@images': resolve(__dirname, './Image')
    }
  },
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `@import "./scss/styles.scss";`
      }
    }
  },
  optimizeDeps: {
    include: ['@/config.mjs', '@/auth.mjs', '@/validation.mjs']
  }
}) 