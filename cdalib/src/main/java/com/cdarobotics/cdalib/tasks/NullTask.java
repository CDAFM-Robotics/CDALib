package com.cdarobotics.cdalib.tasks;

import androidx.annotation.NonNull;

/** A task that does nothing and finishes immediately; the idle placeholder held by {@link TaskMaster}. */
public class NullTask extends Task{
    @Override
    public void init() {}

    @Override
    public boolean run() {
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        return "Null Task";
    }


}
