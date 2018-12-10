package org.onap.ccsdk.apps.controllerblueprints.service.utils;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CloseCondition {

    AtomicInteger tasksSubmitted = new AtomicInteger(0);
    AtomicInteger tasksCompleted = new AtomicInteger(0);
    AtomicBoolean allTaskssubmitted = new AtomicBoolean(false);

    /**
     * notify all tasks have been subitted, determine of the file channel can be closed
     * @return true if the asynchronous file stream can be closed
     */
    public boolean canCloseOnComplete() {
        allTaskssubmitted.set(true);
        return tasksCompleted.get() == tasksSubmitted.get();
    }

    /**
     * notify a task has been submitted
     */
    public void onTaskSubmitted() {
        tasksSubmitted.incrementAndGet();
    }

    /**
     * notify a task has been completed
     * @return true if the asynchronous file stream can be closed
     */
    public boolean onTaskCompleted() {
        boolean allSubmittedClosed = tasksSubmitted.get() == tasksCompleted.incrementAndGet();
        return allSubmittedClosed && allTaskssubmitted.get();
    }
}
