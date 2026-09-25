package com.cdarobotics.cdalib.tasks;

import androidx.annotation.NonNull;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.util.RobotLog;

/** A task that follows a PedroPathing {@link Path}, finishing when the follower is no longer busy. */
public class FollowPathTask extends Task {
    private final Follower follower;
    private final Path path;
    private final double speed;

    /**
     * Follows the path at full speed.
     *
     * @param follower the drivetrain follower
     * @param path     the path to follow
     */
    public FollowPathTask(Follower follower, Path path) {
        this.follower = follower;
        this.path = path;
        speed = 1;
    }

    /**
     * Follows the path at the given speed.
     *
     * @param follower the drivetrain follower
     * @param path     the path to follow
     * @param speed    the path-following speed multiplier, as a fraction of the robot's max speed
     *                 (1.0 is full speed)
     */
    public FollowPathTask(Follower follower, Path path, double speed) {
        this.follower = follower;
        this.path = path;
        this.speed = speed;
    }

    @Override
    public void init() {
        Path toFollow = path;
        // Pedro 3 dropped the per-follow speed argument of followPath(path, speed, holdEnd). A speed
        // cap is now a path modifier — ForesightConfig.maxPathSpeed, a fraction of the robot's max
        // speed — so apply it to a copy of the path. Only Foresight exposes this knob; other
        // algorithms follow at full speed.
        if (speed < 1.0 && follower.algorithm() instanceof Foresight) {
            toFollow = path.with(((Foresight) follower.algorithm()).config.maxPathSpeed.at(speed));
        }
        follower.follow(toFollow);
    }

    @Override
    public boolean run() {
        RobotLog.d("Robot Position: x: %.3f, y: %.3f, heading: %.3f", follower.pose().x(), follower.pose().y(), follower.pose().heading());
        RobotLog.d("Target Position: x: %.3f, y: %.3f, heading: %.3f", path.endPose().x(), path.endPose().y(), path.endPose().heading());
        return !follower.isBusy();
    }

    @NonNull
    @Override
    public String toString() {
        return "Follow Path Task: " + path.toString();
    }


}
