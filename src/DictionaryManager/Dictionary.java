package DictionaryManager;

import java.util.ArrayList;
import java.util.Vector;

public class Dictionary {
    private ArrayList<Word> WordsSaver;
    public Dictionary(){
        WordsSaver = new ArrayList<Word>();
    }

    /****
     *
     * @param word nhan vao mot tu kieu Word roi cho vao arr
     */
    public void GetAllWords(Word word){
        WordsSaver.add(word);
    }


    public ArrayList<Word> ReturnWordSaver() {
        return  WordsSaver;
    }
}
