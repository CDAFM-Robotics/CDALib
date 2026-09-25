package com.cdarobotics.cdalib.opmodes.modules;

/**
 * A no-op {@link Module} whose lifecycle methods do nothing. Useful as a placeholder or default
 * where a module is required but no behavior is wanted.
 */
public class NullModule extends Module {
    @Override
    public void init() {

    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {

    }
}
