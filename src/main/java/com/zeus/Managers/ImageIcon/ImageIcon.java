package com.zeus.Managers.ImageIcon;

import com.zeus.Managers.SystemApp.SystemManager;
import com.zeus.utils.ConfigFactory.ConfigFactory;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.managerfactory.Manager;
import com.zeus.utils.managerfactory.ManagerFactory;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class ImageIcon extends Manager {
    @Override
    public void init() {

    }

    @Override
    protected void setConfig() {
        config = SystemManager.getConfigFactory().getConfig("Icon");
    }

    public Image getImage(String imageName) throws FileNotFoundException, UnsupportedEncodingException {
        return FileManager.loadImage(config.getProperty(imageName, String.class));
    }
}
