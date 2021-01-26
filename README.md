# Babashka Gungnir pod

[Babashka](https://github.com/borkdude/babashka) pod for interacting with
[Gungnir](https://github.com/kwrooijen/gungnir).

## Install

The following installation methods are available:

- Use the latest version from the
  [pod-registry](https://github.com/babashka/pod-registry). This is done by
  calling `load-pod` with a qualified keyword:

  ``` clojure
  (require '[babashka.pods :as pods])
  (pods/load-pod kwrooijen/gungnir "0.0.1")
  ```

  Babashka will automatically download the pod if it is not available on your system yet.

## Compatibility

Pods from this repo require babashka v0.0.96 or later.

### Build

Run `script/compile`

### Test

Run `script/test`.

## Author / License

Released under the [MIT License] by [Kevin William van Rooijen].

[Kevin William van Rooijen]: https://twitter.com/kwrooijen

[MIT License]: https://github.com/kwrooijen/gungnir/blob/master/LICENSE
