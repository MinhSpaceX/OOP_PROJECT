package DictionaryManager;

public class DictionaryCommandline {
    private DictionaryManagement manager;
    /**
     * Constructor.
     */
    public DictionaryCommandline (DictionaryManagement manager) {
        this.manager = manager;
    }


    /**
     * Function to print all contents of a dictionary.
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
     * Function to search for words start with the key value.
     * @param key : value to pass in.
     * @return 0: No word found.
     * <p>
     * 1: One or many words found.
     */
    public int dictionarySearcher(String key) {
        
        return 0;
    }
}
