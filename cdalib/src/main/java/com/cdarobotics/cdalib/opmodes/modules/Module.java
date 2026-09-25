package com.cdarobotics.cdalib.opmodes.modules;

/**
 * A reusable, OpMode-shaped unit of behavior. A module has the same lifecycle as an OpMode
 * ({@link #init()}, {@link #init_loop()}, {@link #start()}, {@link #loop()}, {@link #stop()}) and is
 * driven automatically once installed with {@link com.cdarobotics.cdalib.opmodes.ModularOpMode#installModule}.
 * Use it to package program logic that can be shared across multiple OpModes.
 */
public abstract class Module {
    /** Called once when the OpMode initializes. */
    public abstract void init();

    /**
     * Called repeatedly during the OpMode init phase, after {@link #init()} and before
     * {@link #start()}, while the driver is still on the init screen.
     */
    public abstract void init_loop();

    /** Called once when the OpMode starts (play pressed). */
    public abstract void start();

    /** Called every loop. */
    public abstract void loop();

    /** Called once when the OpMode ends. */
    public abstract void stop();
}
