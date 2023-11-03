package com.zeus.utils.background;

import com.zeus.utils.log.Logger;
import javafx.concurrent.Task;

public class BackgroundTask {
    public static void perform(Runnable runnable) {
        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
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
        task.setOnSucceeded(e -> {
            thread.interrupt();
        });
    }
}
