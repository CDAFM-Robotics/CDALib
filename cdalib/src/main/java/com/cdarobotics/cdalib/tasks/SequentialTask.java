package com.cdarobotics.cdalib.tasks;

import androidx.annotation.NonNull;

/**
 * Runs its child tasks one after another, advancing to the next only when the current one finishes.
 * Completes when the last child finishes (or immediately if there are no children).
 */
public class SequentialTask extends Task {
    private final Task[] tasks;
    private int currentTask = 0;
    /** @param tasks the tasks to run in order, one after another. */
    public SequentialTask(Task... tasks) {
        this.tasks = tasks;
    }

    @Override
    public void init() {
        if (tasks.length > 0) {
            tasks[0].init();
        }
    }

    @Override
    public boolean run() {
        if (currentTask >= tasks.length) {
            return true;
        }
        if (tasks[currentTask].run()) {
            currentTask++;
            if (currentTask >= tasks.length) {
                return true;
            }
            tasks[currentTask].init();
        }
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        toReturn.append("Sequential Task {\n");
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
