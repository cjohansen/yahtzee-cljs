# Yahtzee!

An implementation of Yahtzee in ClojureScript. The goal of this code-base is
mainly educational. I'm hoping to use it as an introduction to (dynamic)
functional programming, and how to create a functional design, as well as how to
decouple as much logic from rendering as possible. The goal is to write an
article or two demonstrating how this code could have been written in the first
place.

## Run locally

Playing Yahtzee is fun. You'll need the Clojure binary:

```sh
brew install clojure
```

And then:

```sh
clojure -A:dev -A:repl
```

Now you should be able to play Yahtzee on
[http://localhost:9500/](http://localhost:9500/).

## Run tests

Tests are available over the development server at
[http://localhost:9500/figwheel-extra-main/tests](http://localhost:9500/figwheel-extra-main/tests).
Optionally, you can run:

```sh
clojure -A:test --watch
```
