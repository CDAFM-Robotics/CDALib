package com.cdarobotics.cdalib.tasks;

import androidx.annotation.NonNull;

/**
 * Runs all of its child tasks at once and completes as soon as any one of them finishes. The
 * remaining tasks are simply no longer polled.
 */
public class RaceTask extends Task {
    private final Task[] tasks;
    /** @param tasks the tasks to run at once; the group finishes as soon as any one finishes. */
    public RaceTask(Task... tasks) {
        this.tasks = tasks;
    }


    @Override
    public void init() {
        for (Task task : tasks) {
            task.init();
        }
    }

    @Override
    public boolean run() {
        for (int i = 0; i < tasks.length; i++) {
            if (tasks[i].run()) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        toReturn.append("Race Task {\n");
        boolean firstLoop = true;
        for (Task task : tasks) {
            if (!firstLoop) {
                toReturn.append(", ");
            }
            firstLoop = false;
            toReturn.append(task.toString());
        }
        return toReturn + "\n}";
    }
}
