@echo off
set "destination_folder=bin/lib"
if not exist "%destination_folder%" (
    mkdir "%destination_folder%"
)

set "destination_folder=bin/resource"
if not exist "%destination_folder%" (
    mkdir "%destination_folder%"
)

xcopy .\lib\ .\bin\lib /E /Y
xcopy .\resource .\bin\resource /E /Y
javac -cp src -d out src/SystemMain/Main.java
java -cp "out;lib/*" SystemMain.Main