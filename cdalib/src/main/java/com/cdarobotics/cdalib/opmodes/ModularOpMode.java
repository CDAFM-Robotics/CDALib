package com.cdarobotics.cdalib.opmodes;

import com.cdarobotics.cdalib.bindings.BindingManager;
import com.cdarobotics.cdalib.opmodes.modules.Module;
import com.cdarobotics.cdalib.subsystems.Subsystem;
import com.cdarobotics.cdalib.tasks.TaskMaster;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.LinkedList;
import java.util.List;

/**
 * Base class for OpModes assembled from reusable parts. Subsystems and modules are registered in
 * {@link #preload()} and then driven automatically through their lifecycles: init on OpMode init,
 * start when play is pressed, and update/loop every cycle. Also manages Lynx hub bulk caching, so
 * each loop performs a single bulk read of all sensors — see {@link #setBulkCachingMode}.
 *
 * <p>PhotonFTC is available for teams that want even lower loop times: annotate your concrete
 * OpMode subclass with {@code @}{@link com.outoftheboxrobotics.photoncore.Photon Photon} to have
 * PhotonCore swap in its optimized motor/servo controllers. Photon pairs with the default
 * {@code MANUAL} bulk-caching mode used here (one cache clear per {@link #loop()}); leave the mode
 * on {@code MANUAL} when Photon is enabled.
 */
public abstract class ModularOpMode extends OpMode {

    private final TaskMaster taskMaster = new TaskMaster();
    private final LinkedList<Subsystem> subsystems = new LinkedList<>();
    private final LinkedList<Module> modules = new LinkedList<>();

    private List<LynxModule> lynxModules;
    private LynxModule.BulkCachingMode bulkCachingMode = LynxModule.BulkCachingMode.MANUAL;

    protected final BindingManager bindingManager = new BindingManager();

    /**
     * Sets the Lynx (Control/Expansion Hub) bulk-caching mode. The default, {@code MANUAL}, reads
     * every sensor and encoder off the hub in a single bulk transaction per loop — the cache is
     * cleared once at the top of {@link #loop()}, so each device's {@code update()} afterward is
     * served from that snapshot instead of hitting the bus. Call from {@link #preload()} to change it.
     */
    protected void setBulkCachingMode(LynxModule.BulkCachingMode mode) {
        this.bulkCachingMode = mode;
    }

    /** Register a subsystem to be driven by this OpMode. Call from {@link #preload()}. */
    protected void registerSubsystem(Subsystem subsystem) {
        subsystems.add(subsystem);
    }

    /** Install a module to be driven by this OpMode. Call from {@link #preload()}. */
    protected void installModule(Module module) {
        modules.add(module);
    }


    /**
     * Preload is a function run before the initialization of the subsystems and the modules. The main use for this function is to register the subsystems and install the modules.
     */
    protected abstract void preload();


    @Override
    public void init() {
        preload();

        lynxModules = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : lynxModules) {
            hub.setBulkCachingMode(bulkCachingMode);
        }

        for (Subsystem subsystem : subsystems) {
            subsystem.init();
        }
        for (Module module : modules) {
            module.init();
        }
    }

    public void init_loop() {
        for (Subsystem subsystem : subsystems) {
            subsystem.init_loop();
        }
        for (Module module : modules) {
            module.init_loop();
        }
    }

    @Override
    public void start() {
        for (Subsystem subsystem : subsystems) {
            subsystem.start();
        }
        for (Module module : modules) {
            module.start();
        }
        taskMaster.update();
    }

    @Override
    public void loop() {
        // Bulk read: in MANUAL mode, clearing the cache once per loop means the first hardware read
        // pulls every sensor/encoder off the hub in one transaction, and the rest are served from it.
        if (bulkCachingMode == LynxModule.BulkCachingMode.MANUAL) {
            for (LynxModule hub : lynxModules) {
                hub.clearBulkCache();
            }
        }

        for (Subsystem subsystem : subsystems) {
            subsystem.update();
        }
        for (Module module : modules) {
            module.loop();
        }
        taskMaster.update();
    }

    @Override
    public void stop() {
        for (Subsystem subsystem : subsystems) {
            subsystem.stop();
        }
        for (Module module : modules) {
            module.stop();
        }
    }
}
