package com.zeus.utils.managerfactory;

import com.zeus.utils.config.Config;
import com.zeus.utils.log.Logger;

public abstract class Manager implements Initialize {
    public Manager() {
        SystemManager.addToMap(this);
    }
}
