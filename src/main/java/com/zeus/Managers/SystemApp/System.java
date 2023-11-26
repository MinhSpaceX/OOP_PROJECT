package com.zeus.Managers.SystemApp;

import com.zeus.App.Window.App;
import com.zeus.utils.managerfactory.Manager;

public class System extends Manager {
    /**
     * Initializes the application by setting the main window using the configuration.
     */
    @Override
    public void init() {
        App.setWindow(config);
    }

    /**
     * Sets the configuration for the window using the "WindowConfig" configuration.
     */
    @Override
    protected void setConfig() {
        config = SystemManager.getConfigFactory().getConfig("WindowConfig");
    }
}
