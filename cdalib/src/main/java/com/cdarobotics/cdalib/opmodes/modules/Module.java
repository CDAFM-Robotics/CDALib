package com.cdarobotics.cdalib.opmodes.modules;

/**
 * A simple class that acts as a modular version of an OpMode. It can be used to make modular parts of programs that can be used in multiple places.
 */
public abstract class Module {
    /** Called once when the OpMode initializes. */
    public abstract void init();

    /** Called once when the OpMode starts (play pressed). */
    public abstract void start();

    /** Called every loop. */
    public abstract void loop();

    /** Called once when the OpMode ends. */
    public abstract void stop();
}
