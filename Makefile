all:
	javac -cp src -d bin src/SystemMain/Main.java
	java -cp bin SystemMain.Main