package DictionaryManager;

public class DictionaryCommandline {
    private final DictionaryManagement manager;

    /**
     * Constructor.
     */
    public DictionaryCommandline(DictionaryManagement manager) {
        this.manager = manager;
        System.out.printf("DictionaryCommandline created.\n");
    }

    /**
     * Function to print all contents of a dictionary.
     *
     * @param dict dictionary to print out.
     */
    public void ShowAllWords(DictionaryID id) {
        System.out.println("No | English | Vietnamese");
        int a = 1;
        Dictionary dict = manager.getDictionary(id);
        for (Word w : dict.getDictionary()) {
            System.out.format("%s | %s | %s\n", a, w.GetWordTarget(), w.GetWordExplain());
            a++;
        }
    }


}
