import { fileURLToPath, URL } from 'node:url'

import vue from '@vitejs/plugin-vue'
import { defineConfig, type Plugin } from 'vite'

// Vite's built-in JSON plugin only matches `.json` — this makes `.geojson`
// imports resolve as parsed objects the same way (so seoul-areas.geojson can
// be a plain `import` like any other data module).
function geojsonLoader(): Plugin {
  return {
    name: 'geojson-loader',
    transform(code, id) {
      if (!id.endsWith('.geojson')) return
      return { code: `export default ${code}`, map: null }
    },
  }
}

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue(), geojsonLoader()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    proxy: {
      // Backend (Spring Boot, :8080) has no CORS config yet — proxy dodges it in dev.
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
  optimizeDeps: {
    // maplibre-gl's internal tile worker breaks when esbuild pre-bundles it
    // for dev (the rewritten module resolves its own worker URL incorrectly).
    exclude: ['maplibre-gl'],
  },
})
