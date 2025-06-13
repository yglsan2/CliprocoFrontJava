import { defineConfig } from 'vite'

export default defineConfig({
  root: 'src/main/webapp',
  build: {
    outDir: '../../../target/cliproco',
    emptyOutDir: true,
    rollupOptions: {
      input: {
        main: 'src/main/webapp/index.jsp',
        connexion: 'src/main/webapp/WEB-INF/jsp/connexion.jsp',
        contact: 'src/main/webapp/WEB-INF/jsp/contact.jsp',
        deconnexion: 'src/main/webapp/WEB-INF/jsp/deconnexion.jsp',
        error: 'src/main/webapp/WEB-INF/jsp/error.jsp'
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
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `
          @import "./scss/_variables.scss";
          @import "./scss/_mixins.scss";
        `
      }
    }
  }
}) 