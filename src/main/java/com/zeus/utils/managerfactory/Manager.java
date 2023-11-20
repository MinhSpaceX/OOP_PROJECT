package com.zeus.utils.managerfactory;

import com.zeus.Managers.SystemApp.SystemManager;
import com.zeus.utils.config.Config;

public abstract class Manager {
    protected Config config;
    protected Integer priority;
    private Runnable initRunnable;

    public Manager() {
        priority = -1;
        SystemManager.addToCollection(this);
        setConfig();
    }

    public abstract void init();

    protected void onInitializeManager(Runnable runnable) {
        this.initRunnable = runnable;
    }

    public boolean isInitRunnable() {
        return initRunnable != null;
    }

    public Runnable getInitRunnable() {
        return initRunnable;
    }

    protected abstract void setConfig();

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) throws Exception {
        if (priority == 0) throw new Exception("Priority cannot be 0.");
        this.priority = priority;
        SystemManager.getManagerList().remove(this);
        SystemManager.getManagerList().add(priority, this);
    }

    public String getInitMessage() {
        return "Creating " + this.getClass().getSimpleName();
    }
}
