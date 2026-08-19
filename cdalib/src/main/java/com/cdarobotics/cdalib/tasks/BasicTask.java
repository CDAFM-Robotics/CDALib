package com.cdarobotics.cdalib.tasks;

import androidx.annotation.NonNull;

import java.util.function.BooleanSupplier;

/**
 * A task built from lambdas: {@code init} runs once, then {@code run} is polled each loop and
 * returns {@code true} when the task is finished.
 */
public class BasicTask extends Task {
    private final Runnable onInit;
    private final BooleanSupplier onRun;

    /**
     * @param init runs once when the task starts
     * @param run  polled each loop; return {@code true} when the task is finished
     */
    public BasicTask(Runnable init, BooleanSupplier run) {
        onInit = init;
        onRun = run;
    }

    @Override
    public void init() {
        onInit.run();
    }

    @Override
    public boolean run() {
        return onRun.getAsBoolean();
    }

    @NonNull
    @Override
    public String toString() {
        return "Basic Task: " + onInit.toString() + ", " + onRun.toString();
    }
}
