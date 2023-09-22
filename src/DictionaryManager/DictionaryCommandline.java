package DictionaryManager;

import java.util.ArrayList;

public class DictionaryCommandline {

    public DictionaryCommandline(){}

    /****
     *
     * @param WordsSaver nhan dau vao arr xong in ra
     */
    public void ShowAllWords(ArrayList<Word> WordsSaver){
        for(Word w : WordsSaver){
            System.out.println( "| " + w.GetWordTarget() + " | " + w.GetWordExplain());
        }
    }
}
