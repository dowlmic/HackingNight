#!/bin/sh

for f in *.java
do 
  echo "compiling: $f"
  javac -classpath ~/robocode/libs/robocode.jar $f || echo "$f failed to compile" || true
done
