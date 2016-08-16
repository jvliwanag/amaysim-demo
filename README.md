# amaysim-demo

# Requirements

* Java 8
* sbt (http://www.scala-sbt.org/)

# Building

1. Check-out the code

  ```
  $ git clone git@github.com:jvliwanag/amaysim-demo.git
  $ cd amaysim-demo
  ```
  
2. Create a distributable fat JAR

  ```
  $ sbt assembly # Creates a fat jar in: target/scala-2.11/amaysim-demo-assembly-0.1-SNAPSHOT.jar
  ```

# Usage

1. Build the jar or download a pre-built one [here](http://bit.ly/amaysim-demo).
2. Run

```
$ java -jar /path/to.jar --help

amaysim-demo 0.1
Usage: amaysim-demo [run-server|get-products|get-product] [options]

Command: run-server [options]

  -H, --host <value>       default: 127.0.0.1. env var: HOST
  -p, --port <value>       default: 9999. env var: PORT
  --page-size <value>      default: 2. env var: PAGE_SIZE
Command: get-products [options]

  --base-url <value>       default: https://amaysim-demo.herokuapp.com/
  --page <value>
Command: get-product [options]

  --base-url <value>       default: https://amaysim-demo.herokuapp.com/
  --product-code <value>
  --help                   prints this usage text
```

# Deployment

```
$ sbt assembly herokuDeploy
```

# Notes

* A demo server is available over at: https://amaysim-demo.herokuapp.com/products
* Output format is [HAL](http://stateless.co/hal_specification.html)

# TODOS

* Shrink down distributable
* Clean up links
