const defaultTheme = require("tailwindcss/defaultTheme");
const formplugin = require('@tailwindcss/forms');
module.exports = {
  future: {
    // removeDeprecatedGapUtilities: true,
    // purgeLayersByDefault: true,
  },
  purge: false,
  theme: {
    extend: {
      fontFamily: {
        sans: ["Inter", ...defaultTheme.fontFamily.sans],
      },
    },
  },
  variants: {},
  plugins: [
    formplugin
  ,],
};
