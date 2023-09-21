<script type="text/javascript"
  src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_CHTML">
</script>
<script type="text/x-mathjax-config">
  MathJax.Hub.Config({
    tex2jax: {
      inlineMath: [['$','$'], ['\\(','\\)']],
      processEscapes: true},
      jax: ["input/TeX","input/MathML","input/AsciiMath","output/CommonHTML"],
      extensions: ["tex2jax.js","mml2jax.js","asciimath2jax.js","MathMenu.js","MathZoom.js","AssistiveMML.js", "[Contrib]/a11y/accessibility-menu.js"],
      TeX: {
      extensions: ["AMSmath.js","AMSsymbols.js","noErrors.js","noUndefined.js"],
      equationNumbers: {
      autoNumber: "AMS"
      }
    }
  });
</script>

# boolean secretDoorUnlocked

| State                 | 0       | 1       |
| --------------------- | ------- | ------- |
| $^{\rightarrow}q_{0}$ | $q_{0}$ | $q_{1}$ |
| $q_{1}$               | $q_{0}$ | $q_{2}$ |
| $q_{2}$               | $q_{0}$ | $q_{3}$ |
| $q_{3}$               | $q_{0}$ | $q_{4}$ |
| $q_{4}$               | $q_{0}$ | $q_{5}$ |
| $^{*}q_{5}$           | $q_{5}$ | $q_{5}$ |
