import { defineConfig } from 'vite'

import legacy from '@vitejs/plugin-legacy'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  envDir: "env_config",
  plugins: [
    vue(),
    legacy({
      targets: ['ie >= 11'],
      additionalLegacyPolyfills: ['regenerator-runtime/runtime'],
    })
  ]
})
