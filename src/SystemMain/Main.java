package SystemMain;

import DictionaryManager.*;
import Input.Input;
import java.io.IOException;

import java.util.Scanner;

public class Main {
    

    public static void main(String[] args) throws IOException {
    	
    	//start the commandline version
    	DictionaryPanel dictP = new DictionaryPanel();
    	dictP.CommandLineManager();
    	
    }
    
}