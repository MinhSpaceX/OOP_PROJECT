package com.zeus.System;

import com.zeus.App.Window.App;
import com.zeus.ConfigFactory.ConfigFactory;
import com.zeus.utils.config.Config;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.Manager;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class System extends Manager {
    public void run(String[] args) {
        App.run(args);
    }

    @Override
    public void init(Config config) {
        App.setWindow(config);

    }
}
