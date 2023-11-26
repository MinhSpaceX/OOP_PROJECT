package com.zeus.utils.background;

import com.zeus.utils.log.Logger;
import javafx.concurrent.Task;

/**
 * Class to perform background tasks.
 */
public class BackgroundTask {
    /**
     * Perform a given task in another thread.
     *
     * @param runnable The task to run in background.
     */
    public static void perform(Runnable runnable) {
        Task<String> task = new Task<>() {
            @Override
            protected String call() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    Logger.error(e.getMessage());
                    return "fail";
                }
                return "success";
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
        task.setOnSucceeded(e -> thread.interrupt());
    }
}
