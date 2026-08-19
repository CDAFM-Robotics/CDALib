package com.cdarobotics.cdalib.devices.sensors;

import com.cdarobotics.cdalib.devices.Device;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

/**
 * A {@link Device} wrapping the universal {@link IMU}. The orientation is read once per
 * {@link #update()} and cached, so reading yaw/pitch/roll multiple times in a loop costs a single
 * hardware read.
 *
 * <p>The IMU must be initialized (via {@code imu.initialize(...)} with your hub's orientation on the
 * robot) before it is used — this wrapper does not initialize it. Getters reflect the value from the
 * most recent {@link #update()}, and return {@code 0} before the first update.
 */
public class IMUDevice extends Device {
    private final IMU imu;

    private YawPitchRollAngles angles;

    /** Wraps an already-initialized IMU. */
    public IMUDevice(IMU imu) {
        this.imu = imu;
    }

    /** Resolves the IMU from the hardware map by configured name. It must still be initialized separately. */
    public IMUDevice(HardwareMap hardwareMap, String name) {
        this(hardwareMap.get(IMU.class, name));
    }

    /** @return the cached yaw (heading) in the requested unit. */
    public double getYaw(AngleUnit unit) {
        return angles == null ? 0 : angles.getYaw(unit);
    }

    /** @return the cached pitch in the requested unit. */
    public double getPitch(AngleUnit unit) {
        return angles == null ? 0 : angles.getPitch(unit);
    }

    /** @return the cached roll in the requested unit. */
    public double getRoll(AngleUnit unit) {
        return angles == null ? 0 : angles.getRoll(unit);
    }

    /** Resets the yaw so the current heading reads as zero. */
    public void resetYaw() {
        imu.resetYaw();
    }

    /** @return the wrapped SDK IMU, for operations not exposed here. */
    public IMU getImu() {
        return imu;
    }

    @Override
    public void update() {
        angles = imu.getRobotYawPitchRollAngles();
    }
}
