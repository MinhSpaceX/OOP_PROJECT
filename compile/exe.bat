@echo off
set "destination_folder=bin/lib"
if not exist "%destination_folder%" (
    mkdir "%destination_folder%"
)

set "destination_folder=bin/resource"
if not exist "%destination_folder%" (
    mkdir "%destination_folder%"
)
.\compile\jar.bat
.\launch4j\launch4jc.exe .\config.xml
.\bin\Main.exe