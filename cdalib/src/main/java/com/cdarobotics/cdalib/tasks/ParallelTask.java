package com.cdarobotics.cdalib.tasks;

import androidx.annotation.NonNull;

/**
 * Runs all of its child tasks at once. Each child is polled until it finishes; the group completes
 * when every child has finished.
 */
public class ParallelTask extends Task{
    private final boolean[] status;
    private final Task[] tasks;
    /** @param tasks the tasks to run at once; the group finishes when all of them finish. */
    public ParallelTask(Task... tasks) {
        this.tasks = tasks;
        status = new boolean[tasks.length];
    }


    @Override
    public void init() {
        for (Task task : tasks) {
            task.init();
        }
    }

    @Override
    public boolean run() {
        boolean toReturn = true;
        for (int i = 0; i < tasks.length; i++) {
            if (!status[i] && !tasks[i].run()) {
                toReturn = false;
            }
            else {
                status[i] = true;
            }
        }
        return toReturn;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        toReturn.append("Parallel Task {\n");
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
