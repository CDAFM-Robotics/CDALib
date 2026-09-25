package com.cdarobotics.cdalib.subsystems;

import com.cdarobotics.cdalib.devices.Device;

/**
 * A {@link Subsystem} backed by a single {@link Device}. The device is refreshed automatically
 * every loop via {@link #update()}; subclasses implement the rest of the lifecycle
 * (init/start/stop) and expose their own control methods on top of the device.
 */
public abstract class DeviceSubsystem extends Subsystem {
    /** The wrapped device, refreshed every loop by {@link #update()}. */
    protected final Device device;

    /**
     * Creates a subsystem backed by the given device.
     *
     * @param device the device this subsystem wraps and refreshes each loop.
     */
    protected DeviceSubsystem(Device device) {
        this.device = device;
    }

    @Override
    public void update() {
        device.update();
    }
}
