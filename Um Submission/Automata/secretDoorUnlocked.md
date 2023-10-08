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

# Secret door logic (boolean secretDoorUnlocked)

## General description

The secret door logic is triggered when `<boolean> secretDoorUnlocked` is true and will replace the map with an empty map containing a dutch flag. It will also replace the green player symbol with a blue one.

The `<boolean> secretDoorUnlocked` is true if the player supplies the following input in order:
1. `y` (caseless check)
2. Nothing OR anything other than `exit` (caseless check)
3. `unlock` (caseless check)
4. Nothing OR anything other than `exit` (caseless check)
5. Mandatory `a`, `c` AND `m` plus optional `y` AND/OR `unlock` in any order (caseless check, repetition is possible)
6. Nothing OR anything other than `exit` (caseless check)
7. `open` (caseless check)

After point 7, the `<boolean> secretDoorUnlocked` is true and the secret door logic triggers.

<div style="page-break-after: always;"></div>

## Automaton
<img src="./src/automaton-secretDoorUnlocked.svg" alt="automaton-secretDoorUnlocked.svg" width="600"/>

<div style="page-break-after: always;"></div>

### Table

| State                 | y        | unlock   | a        | c        | m        | open     | exit     |
| --------------------- | -------- | -------- | -------- | -------- | -------- | -------- | -------- |
| $^{\rightarrow}q_{0}$ | $q_{1}$  | $q_{20}$ | $q_{20}$ | $q_{20}$ | $q_{20}$ | $q_{20}$ | $q_{20}$ |
| $q_{1}$               | $q_{1}$  | $q_{2}$  | $q_{1}$  | $q_{1}$  | $q_{1}$  | $q_{1}$  | $q_{19}$ |
| $q_{2}$               | $q_{2}$  | $q_{2}$  | $q_{3}$  | $q_{8}$  | $q_{13}$ | $q_{2}$  | $q_{19}$ |
| $q_{3}$               | $q_{3}$  | $q_{3}$  | $q_{3}$  | $q_{4}$  | $q_{6}$  | $q_{2}$  | $q_{19}$ |
| $q_{4}$               | $q_{4}$  | $q_{4}$  | $q_{4}$  | $q_{4}$  | $q_{5}$  | $q_{2}$  | $q_{19}$ |
| $q_{5}$               | $q_{5}$  | $q_{5}$  | $q_{5}$  | $q_{5}$  | $q_{5}$  | $q_{18}$ | $q_{19}$ |
| $q_{6}$               | $q_{6}$  | $q_{6}$  | $q_{6}$  | $q_{7}$  | $q_{6}$  | $q_{2}$  | $q_{19}$ |
| $q_{7}$               | $q_{7}$  | $q_{7}$  | $q_{7}$  | $q_{7}$  | $q_{7}$  | $q_{18}$ | $q_{19}$ |
| $q_{8}$               | $q_{8}$  | $q_{8}$  | $q_{9}$  | $q_{8}$  | $q_{11}$ | $q_{2}$  | $q_{19}$ |
| $q_{9}$               | $q_{9}$  | $q_{9}$  | $q_{9}$  | $q_{9}$  | $q_{10}$ | $q_{2}$  | $q_{19}$ |
| $q_{10}$              | $q_{10}$ | $q_{10}$ | $q_{10}$ | $q_{10}$ | $q_{10}$ | $q_{18}$ | $q_{19}$ |
| $q_{11}$              | $q_{11}$ | $q_{11}$ | $q_{12}$ | $q_{11}$ | $q_{11}$ | $q_{2}$  | $q_{19}$ |
| $q_{12}$              | $q_{12}$ | $q_{12}$ | $q_{12}$ | $q_{12}$ | $q_{12}$ | $q_{18}$ | $q_{19}$ |
| $q_{13}$              | $q_{13}$ | $q_{13}$ | $q_{16}$ | $q_{14}$ | $q_{13}$ | $q_{1}$  | $q_{19}$ |
| $q_{14}$              | $q_{14}$ | $q_{14}$ | $q_{15}$ | $q_{14}$ | $q_{14}$ | $q_{2}$  | $q_{19}$ |
| $q_{15}$              | $q_{15}$ | $q_{15}$ | $q_{15}$ | $q_{15}$ | $q_{15}$ | $q_{18}$ | $q_{19}$ |
| $q_{16}$              | $q_{16}$ | $q_{16}$ | $q_{16}$ | $q_{17}$ | $q_{16}$ | $q_{2}$  | $q_{19}$ |
| $q_{17}$              | $q_{17}$ | $q_{17}$ | $q_{17}$ | $q_{17}$ | $q_{17}$ | $q_{18}$ | $q_{19}$ |
| $^{*}q_{18}$          | $q_{18}$ | $q_{18}$ | $q_{18}$ | $q_{18}$ | $q_{18}$ | $q_{18}$ | $q_{18}$ |
| $q_{19}$              | $q_{19}$ | $q_{19}$ | $q_{19}$ | $q_{19}$ | $q_{19}$ | $q_{19}$ | $q_{19}$ |
| $q_{20}$              | $q_{20}$ | $q_{20}$ | $q_{20}$ | $q_{20}$ | $q_{20}$ | $q_{20}$ | $q_{20}$ |