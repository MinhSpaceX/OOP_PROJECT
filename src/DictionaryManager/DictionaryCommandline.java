package DictionaryManager;


import Input.Input;

public class DictionaryCommandline {
    private DictionaryManagement manager;
    /**
     * Constructor.
     */
    public DictionaryCommandline (DictionaryManagement manager) {
        this.manager = manager;
    }
    
    /**
     * Function import value key of function SEARCH.
     * @return KEY.
     */
    public String cinKey() {
        return Input.getScanner().nextLine();
    }
    /**
     * Function to print all contents of the dictionary.
     * @param dict dictionary to print out.
     */
    public void ShowAllWords(DictionaryID id) {
        System.out.println("No | English | Vietnamese");
        int a=1;
        Dictionary dict = manager.getDictionary(id);
        for (Word w : dict.getDictionary()) {
            System.out.format("%s | %s | %s\n", a, w.GetWordTarget(), w.GetWordExplain());
            a++;
        }
    }

    /**
     * function build interface of menu.
     * @return the number of operation.
     */
    public int getInterface() {
        System.out.println("[0] Exit");
        System.out.println("[1] Add");
        System.out.println("[2] Remove");
        System.out.println("[3] Update");
        System.out.println("[4] Display");
        System.out.println("[5] Lookup");
        System.out.println("[6] Search");
        System.out.println("[7] Game");
        System.out.println("[8] Import from file");
        System.out.println("[9] Export to file");
        System.out.println("Your action: ");
        
        int input = Integer.parseInt(Input.getScanner().nextLine());
        if (input >= 0 && input<=9) {
            return input;
        } else {
            System.out.println("Action not support");
            return input;
        }
        
    }
    /**
     * Clear console.
     */
    public void clear() {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }
}
