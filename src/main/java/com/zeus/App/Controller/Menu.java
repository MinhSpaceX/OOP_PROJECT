package com.zeus.App.Controller;

import com.zeus.Managers.Search.SearchManager;
import com.zeus.utils.controller.SearchController;
import com.zeus.utils.log.Logger;
import javafx.scene.control.*;

import javax.swing.text.html.ImageView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Menu extends SearchController {

    @Override
    protected void displayWordFromLabel(Label label) {
        try {
            ChangeToWordView(label);
        } catch (IOException e) {
            Logger.printStackTrace(e);
        }
    }

    @Override
    public void initialize() {
        trie = SearchManager.searchPath;
        Logger.info("Menu initialized.");
    }

    private void ChangeToWordView(Label label) throws IOException {
        SceneContainer sc = SceneContainer.sceneContainer;
        WordView.setMenuLabel(label);
        sc.changeView(WordView.class);
    }
}
