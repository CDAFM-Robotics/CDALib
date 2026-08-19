package com.cdarobotics.cdalib.subsystems;

import java.util.LinkedList;

/**
 * A {@link Subsystem} that groups several child subsystems and fans each lifecycle call
 * (init/start/update/stop) out to all of them. Register children with
 * {@link #registerSubsystem(Subsystem)}.
 */
public abstract class CompoundSubsystem extends Subsystem{
    private final LinkedList<Subsystem> subsystems = new LinkedList<>();

    /** Creates an empty compound subsystem; add children with {@link #registerSubsystem(Subsystem)}. */
    public CompoundSubsystem() {

    }

    /** Adds a child subsystem that will receive all lifecycle calls. */
    public void registerSubsystem(Subsystem subsystem) {
        subsystems.add(subsystem);
    }

    @Override
    public void init() {
        for (Subsystem subsystem : subsystems) {
            subsystem.init();
        }
    }

    @Override
    public void start() {
        for (Subsystem subsystem : subsystems) {
            subsystem.start();
        }
    }

    @Override
    public void update() {
        for (Subsystem subsystem : subsystems) {
            subsystem.update();
        }
    }

    @Override
    public void stop() {
        for (Subsystem subsystem : subsystems) {
            subsystem.stop();
        }
    }
}
