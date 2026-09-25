package com.cdarobotics.cdalib.opmodes.modules;

import com.cdarobotics.cdalib.bindings.BindingManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

/**
 * A simple Module that handles driving with PedroPathing. It uses the built in functions for TeleOp driving in the follower and also uses four bindings, drive, strafe, turn, and slowMode. If using this module, the function setBindings will set the bindings automatically
 */

public class TeleOpPedroModule extends Module {

    private final Follower follower;
    private final BindingManager bindingManager;

    private final double slowModeMultiplier;

    /**
     * Creates a simple Module that handles driving for PedroPathing.
     * @param follower The Follower to be used for driving
     * @param startingPose The starting Pose for the Follower
     * @param bindingManager The BindingManager for the OpMode
     * @param slowModeMultiplier The multiplier for the slow mode
     */

    public TeleOpPedroModule(Follower follower, Pose startingPose, BindingManager bindingManager, double slowModeMultiplier) {
        this.follower = follower;
        follower.setPose(startingPose);
        this.bindingManager = bindingManager;

        this.slowModeMultiplier = slowModeMultiplier;
    }

    /**
     * A simple Module that handles driving for PedroPathing.
     * @param follower The Follower to be used for driving
     * @param startingPose The starting Pose for the Follower
     * @param bindingManager The BindingManager for the OpMode
     */
    public TeleOpPedroModule(Follower follower, Pose startingPose, BindingManager bindingManager) {
        this.follower = follower;
        follower.setPose(startingPose);
        this.bindingManager = bindingManager;

        this.slowModeMultiplier = 1;
    }

    /**
     * Registers the four driving inputs with the binding manager under the ids {@code "drive"},
     * {@code "strafe"}, {@code "turn"}, and {@code "slowMode"}.
     *
     * @param drive    forward/back input, typically the left stick Y
     * @param strafe   left/right input, typically the left stick X
     * @param turn     rotation input, typically the right stick X
     * @param slowMode when true, drive and strafe are scaled by the slow-mode multiplier
     */
    public void setBindings(DoubleSupplier drive, DoubleSupplier strafe, DoubleSupplier turn, BooleanSupplier slowMode) {
        bindingManager.addAnalog("drive", drive);
        bindingManager.addAnalog("strafe", strafe);
        bindingManager.addAnalog("turn", turn);
        bindingManager.addBinding("slowMode", slowMode);
    }

    @Override
    public void init() {
        follower.update();
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {
        // Pedro 3 has no separate startTeleopDrive() call — calling follower.manual(...) each loop
        // (see loop()) puts the follower into MANUAL mode on its own. The drive powers are applied
        // by follower.update(), which must run every loop.
    }

    @Override
    public void loop() {
        if (bindingManager.checkBinding("slowMode")) {
            follower.manual(bindingManager.checkAnalog("drive") * slowModeMultiplier, bindingManager.checkAnalog("strafe") * slowModeMultiplier, bindingManager.checkAnalog("turn"));
        }
        else {
            follower.manual(bindingManager.checkAnalog("drive"), bindingManager.checkAnalog("strafe"), bindingManager.checkAnalog("turn"));
        }
        // New in Pedro 3: ManualDrive.driveOrHold(follower, forward, lateral, turn) auto-holds the
        // robot's pose when the sticks are released. It calls follower.hold(), which needs a tuned
        // algorithm, so it's left opt-in rather than wired in here.
    }

    @Override
    public void stop() {
    }
}
