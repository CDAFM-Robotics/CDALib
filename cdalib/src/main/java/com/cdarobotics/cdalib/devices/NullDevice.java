package com.cdarobotics.cdalib.devices;

/**
 * A no-op {@link Device} whose {@link #update()} does nothing. Useful as a placeholder for optional
 * or missing hardware, letting a subsystem hold a non-null device reference and run unchanged when a
 * real device is not present.
 */
public class NullDevice extends Device {
    /** Does nothing — this device has no hardware to read from or write to. */
    @Override
    public void update() {
    }
}
