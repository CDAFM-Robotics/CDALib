package com.cdarobotics.cdalib.tasks;

import androidx.annotation.NonNull;

import java.util.function.Supplier;

/**
 * A task that defers building its inner task until init() is called at runtime.
 * Use this when the task depends on state that isn't known at construction time
 * (e.g., obelisk pattern from AprilTag detection).
 */
public class DeferredTask extends Task {

    private final Supplier<Task> taskSupplier;
    private Task innerTask;

    /** @param taskSupplier builds the inner task lazily, when {@link #init()} is first called. */
    public DeferredTask(Supplier<Task> taskSupplier) {
        this.taskSupplier = taskSupplier;
    }

    @Override
    public void init() {
        innerTask = taskSupplier.get();
        innerTask.init();
    }

    @Override
    public boolean run() {
        return innerTask.run();
    }

    @NonNull
    @Override
    public String toString() {
        return "Deferred Task: " + (innerTask != null ? innerTask.toString() : "not yet built");
    }
}
