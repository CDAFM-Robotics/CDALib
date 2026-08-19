package com.cdarobotics.cdalib.tasks;

import androidx.annotation.NonNull;

import java.util.function.Supplier;

/**
 * WARNING:
 * This task has no termination condition. To terminate it you have to use another Task
 */

public class RepeatTask extends Task{

    private final Supplier<Task> taskSupplier;
    private Task currentTask;

    /** @param taskSupplier builds a fresh task each time the previous one finishes. */
    public RepeatTask(Supplier<Task> taskSupplier) {
        this.taskSupplier = taskSupplier;
    }

    @Override
    public void init() {
        currentTask = taskSupplier.get();
        currentTask.init();
    }

    @Override
    public boolean run() {
        if (currentTask.run()) {
            currentTask = taskSupplier.get();
            currentTask.init();
        }

        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return "Repeat Task: " + taskSupplier.toString();
    }
}
