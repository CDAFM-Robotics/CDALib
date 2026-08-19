package com.cdarobotics.cdalib.tasks;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.util.RobotLog;

/** A task that follows a PedroPathing {@link PathChain}, finishing when the follower is no longer busy. */
public class FollowPathTask extends Task {
    private final Follower follower;
    private final PathChain path;
    private final double speed;

    /**
     * Follows the path at full speed.
     *
     * @param follower  the drivetrain follower
     * @param pathChain the path to follow
     */
    public FollowPathTask(Follower follower, PathChain pathChain) {
        this.follower = follower;
        this.path = pathChain;
        speed = 1;
    }

    /**
     * @param follower  the drivetrain follower
     * @param pathChain the path to follow
     * @param speed     the path-following speed multiplier (1.0 is full speed)
     */
    public FollowPathTask(Follower follower, PathChain pathChain, double speed) {
        this.follower = follower;
        this.path = pathChain;
        this.speed = speed;
    }

    @Override
    public void init() {
        follower.followPath(path, speed, false);
    }

    @Override
    public boolean run() {
        RobotLog.d("Robot Position: x: %.3f, y: %.3f, heading: %.3f", follower.getPose().getX(), follower.getPose().getY(), follower.getHeading());
        RobotLog.d("Target Position: x: %.3f, y: %.3f, heading: %.3f", path.endPose().getX(), path.endPose().getY(), path.endPose().getHeading());
        return !follower.isBusy();
    }

    @NonNull
    @Override
    public String toString() {
        return "Follow Path Task: " + path.toString();
    }


}
