package com.cdarobotics.cdalib.subsystems;

import com.cdarobotics.cdalib.opmodes.ModularOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

/**
 * A {@link Subsystem} that reads the IMU on its own background thread so the main loop never blocks
 * on it.
 *
 * <p>The universal {@link IMU} is an I2C device, and I2C is <em>not</em> covered by Lynx bulk
 * caching — so a normal {@code getRobotYawPitchRollAngles()} in the loop costs a full blocking
 * transaction (several milliseconds) every cycle. This subsystem instead spins a reader thread that
 * polls the IMU continuously and publishes each reading to a cached snapshot; the getters return
 * that snapshot in constant time, removing IMU latency from every loop.
 *
 * <p>Register it with {@code registerSubsystem(...)} in your {@code preload()} — {@link
 * ModularOpMode} then starts the reader thread on {@code start()} and stops it on OpMode end. The
 * IMU must already be initialized (via {@code imu.initialize(...)} with your hub orientation) before
 * this subsystem is registered; like {@link com.cdarobotics.cdalib.devices.sensors.IMUDevice} it
 * does not initialize the IMU itself.
 *
 * <p>All hardware access to the IMU (the periodic read and {@link #resetYaw()}) is serialized on an
 * internal lock, so it is safe to call {@link #resetYaw()} from the loop thread while the reader
 * thread is running. Getters return {@code 0} until the first reading lands.
 */
public class ThreadedIMU extends Subsystem {

    /** Default gap between reads (~200 Hz), far faster than any loop, without pegging a core. */
    public static final long DEFAULT_POLL_INTERVAL_MS = 10;

    private final IMU imu;
    private final long pollIntervalMs;

    // Guards all hardware access to the IMU so the reader thread and resetYaw() never overlap.
    private final Object imuLock = new Object();

    // Latest snapshot, published by the reader thread and read by the loop thread. A YawPitchRollAngles
    // is an immutable snapshot, so publishing a fresh reference through this volatile is race-free.
    private volatile YawPitchRollAngles angles;

    private volatile boolean running;
    private Thread readerThread;

    /** Wraps an already-initialized IMU, polling at the {@linkplain #DEFAULT_POLL_INTERVAL_MS default rate}. */
    public ThreadedIMU(IMU imu) {
        this(imu, DEFAULT_POLL_INTERVAL_MS);
    }

    /**
     * Wraps an already-initialized IMU with a custom poll interval.
     *
     * @param pollIntervalMs milliseconds the reader sleeps between reads; {@code 0} polls as fast as
     *                       possible (busy-spins a core — prefer a small non-zero value).
     */
    public ThreadedIMU(IMU imu, long pollIntervalMs) {
        this.imu = imu;
        this.pollIntervalMs = pollIntervalMs;
    }

    /** Resolves the IMU from the hardware map by configured name. It must still be initialized separately. */
    public ThreadedIMU(HardwareMap hardwareMap, String name) {
        this(hardwareMap.get(IMU.class, name));
    }

    /** Resolves the IMU from the hardware map by name, with a custom poll interval. */
    public ThreadedIMU(HardwareMap hardwareMap, String name, long pollIntervalMs) {
        this(hardwareMap.get(IMU.class, name), pollIntervalMs);
    }

    @Override
    public void init() {
        // The IMU is initialized by the caller; nothing to grab here.
    }

    /** Launches the reader thread. Idempotent — calling twice while running does nothing. */
    @Override
    public void start() {
        if (running) {
            return;
        }
        running = true;
        readerThread = new Thread(this::readLoop, "cdalib-imu-reader");
        readerThread.setDaemon(true);
        readerThread.start();
    }

    @Override
    public void update() {
        // Intentionally empty: the reader thread keeps the snapshot fresh; getters read it directly.
    }

    /** Signals the reader thread to stop and waits briefly for it to finish. */
    @Override
    public void stop() {
        running = false;
        if (readerThread != null) {
            readerThread.interrupt();
            try {
                readerThread.join(250);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            readerThread = null;
        }
    }

    private void readLoop() {
        while (running) {
            YawPitchRollAngles reading;
            synchronized (imuLock) {
                reading = imu.getRobotYawPitchRollAngles();
            }
            angles = reading;

            if (pollIntervalMs > 0) {
                try {
                    Thread.sleep(pollIntervalMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    /** @return the most recent yaw (heading) in the requested unit, or {@code 0} before the first read. */
    public double getYaw(AngleUnit unit) {
        YawPitchRollAngles snapshot = angles;
        return snapshot == null ? 0 : snapshot.getYaw(unit);
    }

    /** @return the most recent pitch in the requested unit, or {@code 0} before the first read. */
    public double getPitch(AngleUnit unit) {
        YawPitchRollAngles snapshot = angles;
        return snapshot == null ? 0 : snapshot.getPitch(unit);
    }

    /** @return the most recent roll in the requested unit, or {@code 0} before the first read. */
    public double getRoll(AngleUnit unit) {
        YawPitchRollAngles snapshot = angles;
        return snapshot == null ? 0 : snapshot.getRoll(unit);
    }

    /**
     * Resets the yaw so the current heading reads as zero. Safe to call from the loop thread — it is
     * serialized against the reader thread's IMU access.
     */
    public void resetYaw() {
        synchronized (imuLock) {
            imu.resetYaw();
        }
    }

    /**
     * @return the wrapped SDK IMU. Direct calls from the loop thread race with the reader thread;
     * synchronize on nothing exposed here — prefer the getters and {@link #resetYaw()}.
     */
    public IMU getImu() {
        return imu;
    }
}
