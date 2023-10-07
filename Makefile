.SILENT:
all:
	javac -cp lib/* -sourcepath .\src\ -d bin src/SystemMain/Main.java
	java -cp "bin;lib\*" SystemMain.Main
compress:
	javac -cp ./lib/*.jar -sourcepath .\src\ -d bin src/SystemMain/Main.java
	jar -cfmv bin/Main.jar bin/manifest.txt lib -C bin/ .
	java -jar bin/Main.jar
clean:
	rmdir bin
copy:
	xcopy .\src\manifest.txt /D .\bin
printallfile:
	for /R "bin" %%F in (*) do ( \
        echo %%F \
    )