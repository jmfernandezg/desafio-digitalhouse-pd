/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#0F766E',
          dark: '#134E4A',
        },
        accent: '#F97316',
        background: '#F0F9FF',
        text: '#475569',
      },
      fontFamily: {
        sans: ['Fira Sans', 'sans-serif'],
      },
      boxShadow: {
        'custom': '0 2px 4px rgba(0, 0, 0, 0.1)',
      },
    },
  },
  plugins: [],
}