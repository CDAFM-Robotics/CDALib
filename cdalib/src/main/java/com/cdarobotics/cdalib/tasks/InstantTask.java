package com.cdarobotics.cdalib.tasks;


import androidx.annotation.NonNull;

import java.util.function.Supplier;

/** A task that runs an action once in {@link #init()} and finishes immediately. */
public class InstantTask extends Task{

    private final Runnable onInit;

    /** @param init the action to run once before the task immediately finishes. */
    public InstantTask(Runnable init) {
        onInit = init;
    }

    @Override
    public void init() {
        onInit.run();
    }

    @Override
    public boolean run() {
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        return "Instant Task: " + onInit.toString();
    }
}
