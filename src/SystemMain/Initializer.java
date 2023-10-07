package SystemMain;

import DictionaryManager.DictionaryCommandline;
import DictionaryManager.DictionaryManagement;
import utils.Input.Input;

public class Initializer {
    private final DictionaryManagement manager;
    private final DictionaryCommandline dictCom;
    private final DictionaryPanel panel;

    /**
     * Constructor.
     * @param filePath Path to data txt file.
     */
    Initializer(String filePath) {
        manager = new DictionaryManagement();
        dictCom = new DictionaryCommandline(manager);
        panel = new DictionaryPanel(manager, dictCom, filePath);
    }
    
    void terminate() {
        Input.closeScanner();
    }

    DictionaryManagement getManager() {
        return manager;
    }

    DictionaryCommandline getCommandline() {
        return dictCom;
    }

    DictionaryPanel getPanel() {
        return panel;
    }
}
