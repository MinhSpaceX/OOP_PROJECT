.SILENT:
all:
	javac -cp src -d bin src/SystemMain/Main.java
	java -cp bin SystemMain.Main
compress:
	javac -cp src -d bin src/SystemMain/Main.java
	jar -cfmv bin/Main.jar bin/manifest.txt -C bin/ .
	java -jar bin/Main.jar