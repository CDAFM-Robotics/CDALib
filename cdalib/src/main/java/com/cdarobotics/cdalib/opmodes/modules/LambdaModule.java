package com.cdarobotics.cdalib.opmodes.modules;

/**
 * A {@link Module} whose four lifecycle phases (init/start/loop/stop) are supplied as lambdas, for
 * quick inline behavior without declaring a dedicated module class.
 */
public class LambdaModule extends Module {

    private final Runnable initLambda;
    private final Runnable startLambda;
    private final Runnable loopLambda;
    private final Runnable stopLambda;

    /**
     * @param init  runs once on OpMode init
     * @param start runs once when the OpMode starts
     * @param loop  runs every loop
     * @param stop  runs once when the OpMode ends
     */
    public LambdaModule(Runnable init, Runnable start, Runnable loop, Runnable stop) {
        initLambda = init;
        startLambda = start;
        loopLambda = loop;
        stopLambda = stop;
    }


    @Override
    public void init() {
        initLambda.run();
    }

    @Override
    public void start() {
        startLambda.run();
    }

    @Override
    public void loop() {
        loopLambda.run();
    }

    @Override
    public void stop() {
        stopLambda.run();
    }
}
