const purgecss = require("@fullhuman/postcss-purgecss")({
  content: ["src/cinemart/**/*.cljs", "public/index.html"],
  defaultExtractor: (content) =>
    content.match(/((sm|md|lg|xl|hover):)?[\w-/]+/g),
});

module.exports = {
  plugins: [
    require("tailwindcss"),
    require("autoprefixer"),
    require("cssnano")(),
    ...(process.env.NODE_ENV === "production" ? [purgecss] : []),
  ],
};
