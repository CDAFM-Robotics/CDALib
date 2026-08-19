package com.cdarobotics.cdalib.tasks;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;

/** A task that commands the PedroPathing follower to hold a pose, finishing immediately after issuing the command. */
public class HoldPointTask extends Task {
    private final Follower follower;
    private final Pose holdPose;
    /**
     * @param follower the drivetrain follower
     * @param pose     the pose to hold
     */
    public HoldPointTask(Follower follower, Pose pose) {
        this.follower = follower;
        holdPose = pose;
    }

    @Override
    public void init() {
        follower.holdPoint(holdPose);
    }

    @Override
    public boolean run() {
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        return "Hold Point Task: " + holdPose.toString();
    }
}
