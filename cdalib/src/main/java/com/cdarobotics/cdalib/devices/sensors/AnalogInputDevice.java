package com.cdarobotics.cdalib.devices.sensors;

import com.cdarobotics.cdalib.devices.Device;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * A {@link Device} wrapping an {@link AnalogInput} (e.g. a potentiometer or analog distance sensor).
 * The voltage is read once per {@link #update()} and cached.
 *
 * <p>Getters reflect the value from the most recent {@link #update()}.
 */
public class AnalogInputDevice extends Device {
    private final AnalogInput input;

    private double voltage;

    /** Wraps an already-resolved analog input. */
    public AnalogInputDevice(AnalogInput input) {
        this.input = input;
    }

    /** Resolves the analog input from the hardware map by configured name. */
    public AnalogInputDevice(HardwareMap hardwareMap, String name) {
        this(hardwareMap.get(AnalogInput.class, name));
    }

    /** @return the input voltage as of the last {@link #update()}. */
    public double getVoltage() {
        return voltage;
    }

    /** @return the maximum voltage this input can report (a fixed hardware constant). */
    public double getMaxVoltage() {
        return input.getMaxVoltage();
    }

    /** @return the wrapped SDK input, for operations not exposed here. */
    public AnalogInput getInput() {
        return input;
    }

    @Override
    public void update() {
        voltage = input.getVoltage();
    }
}
