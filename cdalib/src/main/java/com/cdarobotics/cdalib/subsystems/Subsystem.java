package com.cdarobotics.cdalib.subsystems;

/**
 * A robot mechanism with a fixed lifecycle: {@link #init()} once on OpMode init, {@link #start()}
 * once when the OpMode starts, {@link #update()} every loop, and {@link #stop()} once when it ends.
 */
public abstract class Subsystem {
    /** Called once when the OpMode initializes. Grab hardware and set starting state here. */
    public abstract void init();

    /**
     * Called repeatedly during the OpMode init phase, after {@link #init()} and before
     * {@link #start()}, while the driver is still on the init screen. Use it for work that must keep
     * running until play is pressed, such as reporting sensor readings or waiting on hardware.
     */
    public abstract void init_loop();

    /** Called once when the OpMode starts (play pressed). */
    public abstract void start();

    /** Called every loop. Read inputs and drive the mechanism here. */
    public abstract void update();

    /** Called once when the OpMode ends. Safe or release the mechanism here. */
    public abstract void stop();
}
