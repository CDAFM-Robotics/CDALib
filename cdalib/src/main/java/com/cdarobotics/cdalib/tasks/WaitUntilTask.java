package com.cdarobotics.cdalib.tasks;

import androidx.annotation.NonNull;

import java.util.function.BooleanSupplier;

/** A task that finishes as soon as the supplied condition becomes {@code true}. */
public class WaitUntilTask extends Task {
    private final BooleanSupplier toCheck;

    /** @param toCheck the condition to poll; the task finishes when it returns {@code true}. */
    public WaitUntilTask(BooleanSupplier toCheck) {
        this.toCheck = toCheck;
    }

    @Override
    public void init() {

    }

    @Override
    public boolean run() {
        return toCheck.getAsBoolean();
    }

    @NonNull
    @Override
    public String toString() {
        return "Wait Until Task: " + toCheck.toString();
    }
}
