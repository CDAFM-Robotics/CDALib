package com.cdarobotics.cdalib.devices;

/**
 * A hardware wrapper that is refreshed once per loop via {@link #update()}. Actuator devices cache
 * their commanded state and flush it to hardware in {@link #update()} (write caching); sensor
 * devices read hardware in {@link #update()} and cache the value for the rest of the loop.
 *
 * <p>Devices are typically owned by a {@link com.cdarobotics.cdalib.subsystems.DeviceSubsystem},
 * which calls {@link #update()} automatically every loop.
 */
public abstract class Device {
    /** Flush pending writes to, or refresh cached reads from, the underlying hardware. */
    public abstract void update();
}
