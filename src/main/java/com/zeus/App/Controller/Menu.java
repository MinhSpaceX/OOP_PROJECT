package com.zeus.App.Controller;

import com.zeus.Managers.Search.SearchManager;
import com.zeus.utils.controller.SearchController;
import com.zeus.utils.log.Logger;
import javafx.scene.control.Label;

import java.io.IOException;

/**
 * Controller of the menu interface when user open the application
 */
public class Menu extends SearchController {

    /**
     * get the input from search bar then change to {@link WordView} with
     * {@link #ChangeToWordView(Label)} method
     *
     * @param label index of the word that user want to search.
     */
    @Override
    protected void displayWordFromLabel(Label label) {
        try {
            ChangeToWordView(label);
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }

    /**
     * This method is called by the FXMLLoader when initialization is complete.
     */
    @Override
    public void initialize() {
        trie = SearchManager.searchPath;
        Logger.info("Menu initialized.");
    }

    /**
     * this method will change to {@link WordView} whn called
     *
     * @param label index of the word to display
     */
    private void ChangeToWordView(Label label) {
        SceneContainer sc = SceneContainer.sceneContainer;
        WordView.setMenuLabel(label);
        sc.changeView(WordView.class);
    }
}
