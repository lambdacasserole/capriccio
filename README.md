# Capriccio
A toolkit for the creation of correct-by-construction arithmetic languages.

![Logo](assets/logo.svg)

## Overview
Capriccio is a toolkit for turning functions defined and verified using the [Hoare Advanced Homework Assistant (HAHA)](http://haha.mimuw.edu.pl/) into functions callable from a simple, interpreted arithmetic language. The logo has the alchemical symbol for a half an ounce in the background, because that's about how much caffeine I was running on when I finished it up.

## Prerequisites
Before you do anything else, you need to make sure the latest version `humoresque.jar` is installed to the same folder as `capriccio.py`. Capriccio relies on Humoresque for HAHA-Java transpilation and won't function without it. The following software is also required:
* JDK 11.0.4 \[[^](https://www.oracle.com/technetwork/java/javase/downloads/index.html)\] or later (earlier versions might also work)
* Python 3 \[[^](https://www.python.org/)\]
* Make (install using `sudo apt-get install build-essential` on Debian-like distros)

## Usage
Using the Capriccio toolkit requires three distinct steps, which are outlined below.

### Specification/Implementation
The first thing you need to do is specify and verify your functions using HAHA in the `/src` folder. It's your responsibility to make sure that all verification conditions for your functions are properly generated/discharged from HAHA, because neither Capriccio nor Humoresque will do any of that checking for you. All functions from all files in this folder will be automatically extracted to Java and integrated into the generated interpreter, so feel free to create one function per file, or put all your functions in the same file, it doesn't matter.

A sample file `sum.haha` that adds two numbers together is included for demonstration purposes.

### Compilation
You'll now need to run the following in order to build your interpreter:

```
make
```

A few things will happen:
* Capriccio will use Humoresque to translate your HAHA functions into Java, and individually write each of these into its own function class in `./build`. You can recognise these classes by the suffix `Function`. They implement the `NamedFunction` interface.
* Each of these functions will be registered with the interpreter in `/build/Main.java`.
* The Java in `./build` will be compiled into classes.
* Finally, these classes will be packaged up into an executable JAR file at `/build/capriccio.jar`.

### Running
Your programming language is now ready! The syntax is very simple (see `/examples/simple.math`). Write a file in this simple arithmetic language using any functions you specified in HAHA, and run it like so (this example can be run from `/build`, executing the `/examples/simple.math`):

```
java -jar capriccio.jar ../examples/simple.math
```

## Limitations
This toolkit is currently subject to some limitations:
* You won't be developing the next Clojure, Haskell or F# with this toolkit. The core of the interpreter (tokeniser, parser, evaluator etc.) is extremely simple.
* Generated languages are **not** lisps: lists are not supported, programs are not data, there are calls and there are atoms (no s-expressions).
* Integers are the only supported data type.
* There is no notion of variable binding/function definition/closures or any functional goodies like that.

## Contributing
For most intents and purposes, this project is considered to fulfil its original use case. Bug fixes and suggestions are welcome, however, from any member of the community.

## Acknowledgements
A big thank you to the team at the [University of Warsaw](https://mimuw.edu.pl/en) behind [HAHA](http://haha.mimuw.edu.pl/). This really is an awesome tool for teaching software verification! By name, they're:
* [Tadeusz Sznuk](http://www.mimuw.edu.pl/~tsznuk/)
* Jacek Chrząszcz
* [Aleksy Schubert](http://www.mimuw.edu.pl/~alx/)
* Jakub Zakrzewski
