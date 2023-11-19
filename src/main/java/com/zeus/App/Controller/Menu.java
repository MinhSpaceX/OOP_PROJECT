package com.zeus.App.Controller;

import com.zeus.App.SearchManager;
import com.zeus.utils.controller.SearchController;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.SystemManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.zeus.utils.file.FileManager.getPathFromFile;

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
        sc.changeView("/com/zeus/fxml/WordView.fxml");
    }
}
