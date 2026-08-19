package com.cdarobotics.cdalib.tasks;

import androidx.annotation.NonNull;

/** A task that finishes after a fixed delay (in milliseconds) has elapsed since {@link #init()}. */
public class SleepTask extends Task {

    private final long delay;

    /** @param msDelay how long to wait, in milliseconds, before the task finishes. */
    public SleepTask(long msDelay) {
        delay = msDelay;
    }

    private long initTime;
    @Override
    public void init() {
        initTime = System.currentTimeMillis();
    }

    @Override
    public boolean run() {
        return System.currentTimeMillis() - initTime >= delay;
    }

    @NonNull
    @Override
    public String toString() {
        return "Sleep Task: " + delay;
    }
}
