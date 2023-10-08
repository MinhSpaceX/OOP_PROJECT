@echo off
set "destination_folder=bin/lib"
if not exist "%destination_folder%" (
    mkdir "%destination_folder%"
)

set "destination_folder=bin/resource"
if not exist "%destination_folder%" (
    mkdir "%destination_folder%"
)
javac -cp src -d out src/SystemMain/Main.java
jar -cfmv bin/Main.jar compile/manifest.txt -C out/ .
java -jar bin/Main.jar