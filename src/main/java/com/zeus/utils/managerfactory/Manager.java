package com.zeus.utils.managerfactory;

import com.zeus.Managers.SystemApp.SystemManager;
import com.zeus.utils.config.Config;

/**
 * Manager class with purpose of manage.
 */
public abstract class Manager {
    protected Config config;
    protected Integer priority;
    private Runnable initRunnable;

    /**
     * Constructor. Set priority to -1,
     * add this instance to list of managers and load
     * config through {@link #setConfig()}.
     */
    public Manager() {
        priority = -1;
        SystemManager.addToCollection(this);
        setConfig();
    }

    /**
     * Abstract method initialize things needed.
     */
    public abstract void init();

    /**
     * Set execte a method when {@link #init()} is called.
     *
     * @param runnable Tasks to do.
     */
    protected void onInitializeManager(Runnable runnable) {
        this.initRunnable = runnable;
    }

    /**
     * Check if this class {@link #initRunnable} is null or not.
     *
     * @return true if {@link #initRunnable} is not null
     * false if {@link #initRunnable} is null.
     */
    public boolean isInitRunnable() {
        return initRunnable != null;
    }

    /**
     * Get method runnable.
     *
     * @return Method to run.
     */
    public Runnable getInitRunnable() {
        return initRunnable;
    }

    /**
     * Set config method.
     */
    protected abstract void setConfig();

    /**
     * Get priority.
     *
     * @return Priority.
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * Set priority.
     *
     * @param priority Priority.
     * @throws Exception Exception if error occur.
     */
    public void setPriority(Integer priority) throws Exception {
        if (priority == 0) throw new Exception("Priority cannot be 0.");
        this.priority = priority;
        SystemManager.getManagerList().remove(this);
        SystemManager.getManagerList().add(priority, this);
    }

    /**
     * Get the message when object is initializing.
     *
     * @return The message.
     */
    public String getInitMessage() {
        return "Creating " + this.getClass().getSimpleName();
    }
}
