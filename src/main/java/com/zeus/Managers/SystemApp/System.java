package com.zeus.Managers.SystemApp;

import com.zeus.App.Window.App;
import com.zeus.utils.managerfactory.Manager;

public class System extends Manager {
    @Override
    public void init() {
        App.setWindow(config);
    }

    @Override
    protected void setConfig() {
        config = SystemManager.getConfigFactory().getConfig("WindowConfig");
    }
}
