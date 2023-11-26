package com.zeus.Managers.ImageIcon;

import com.zeus.Managers.SystemApp.SystemManager;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.managerfactory.Manager;
import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class ImageIcon extends Manager {
    @Override
    public void init() {
    }

    /**
     * Sets the configuration for handling icon-related properties.
     * Uses the SystemManager to retrieve the configuration
     * from the "Icon" category.
     */
    @Override
    protected void setConfig() {
        config = SystemManager.getConfigFactory().getConfig("Icon");
    }

    /**
     * Retrieves an image using the specified imageName.
     */
    public Image getImage(String imageName) throws FileNotFoundException, UnsupportedEncodingException {
        return FileManager.loadImage(config.getProperty(imageName, String.class));
    }
}
