package com.cdarobotics.cdalib.devices.sensors;

import com.cdarobotics.cdalib.devices.Device;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * A {@link Device} wrapping a {@link DistanceSensor}. The distance is read once per {@link #update()}
 * (in centimetres) and cached; {@link #getDistance(DistanceUnit)} converts the cached reading into
 * any requested unit without touching hardware again.
 *
 * <p>Getters reflect the value from the most recent {@link #update()}.
 */
public class DistanceSensorDevice extends Device {
    private final DistanceSensor sensor;

    private double distanceCm;

    /** Wraps an already-resolved distance sensor. */
    public DistanceSensorDevice(DistanceSensor sensor) {
        this.sensor = sensor;
    }

    /** Resolves the distance sensor from the hardware map by configured name. */
    public DistanceSensorDevice(HardwareMap hardwareMap, String name) {
        this(hardwareMap.get(DistanceSensor.class, name));
    }

    /** @return the last measured distance, converted into the requested unit. */
    public double getDistance(DistanceUnit unit) {
        return unit.fromUnit(DistanceUnit.CM, distanceCm);
    }

    /** @return the last measured distance in centimetres. */
    public double getDistanceCm() {
        return distanceCm;
    }

    /** @return the wrapped SDK sensor, for operations not exposed here. */
    public DistanceSensor getSensor() {
        return sensor;
    }

    @Override
    public void update() {
        distanceCm = sensor.getDistance(DistanceUnit.CM);
    }
}
