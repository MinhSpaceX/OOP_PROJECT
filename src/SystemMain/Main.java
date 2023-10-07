package SystemMain;

public class Main {


    public static void main(String[] args) {
        //start the commandline version
        Initializer init = new Initializer("src/dictionary.txt");
        DictionaryPanel panel = init.getPanel();
        panel.CommandLineManager();
        init.terminate();
    }

}