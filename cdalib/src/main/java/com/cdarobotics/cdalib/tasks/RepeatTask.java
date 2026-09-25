package com.cdarobotics.cdalib.tasks;

import androidx.annotation.NonNull;

import java.util.function.Supplier;

/**
 * Repeatedly runs a freshly built task: each time the current task finishes, a new one is built from
 * the supplier and started immediately.
 *
 * <p><b>Warning:</b> this task has no termination condition of its own — {@link #run()} always
 * returns {@code false}. To stop it, compose it with a bounding task via {@link #raceWith(Task...)}
 * or {@link #withDeadline(Task)}.
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

    /**
     * When the current inner task finishes, builds and starts a new one.
     *
     * @return always {@code false}; this task never finishes on its own.
     */
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
