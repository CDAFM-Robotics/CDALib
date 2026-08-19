package com.cdarobotics.cdalib.devices.sensors;

import com.cdarobotics.cdalib.devices.Device;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * A {@link Device} wrapping a {@link DigitalChannel} configured as an input (e.g. a limit switch or
 * magnetic sensor). The channel is read once per {@link #update()} and cached.
 *
 * <p>Getters reflect the value from the most recent {@link #update()}.
 */
public class DigitalInputDevice extends Device {
    private final DigitalChannel channel;

    private boolean state;

    /** Wraps an already-resolved digital channel and puts it in input mode. */
    public DigitalInputDevice(DigitalChannel channel) {
        this.channel = channel;
        this.channel.setMode(DigitalChannel.Mode.INPUT);
    }

    /** Resolves the digital channel from the hardware map by configured name. */
    public DigitalInputDevice(HardwareMap hardwareMap, String name) {
        this(hardwareMap.get(DigitalChannel.class, name));
    }

    /** @return the channel state as of the last {@link #update()}. */
    public boolean getState() {
        return state;
    }

    /** @return the wrapped SDK channel, for operations not exposed here. */
    public DigitalChannel getChannel() {
        return channel;
    }

    @Override
    public void update() {
        state = channel.getState();
    }
}
