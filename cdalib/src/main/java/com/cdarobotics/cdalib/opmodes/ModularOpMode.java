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

    /** Drives queued tasks; polled once in {@link #start()} and again every {@link #loop()}. */
    private final TaskMaster taskMaster = new TaskMaster();
    /** Subsystems registered via {@link #registerSubsystem}, driven in registration order. */
    private final LinkedList<Subsystem> subsystems = new LinkedList<>();
    /** Modules installed via {@link #installModule}, driven in installation order. */
    private final LinkedList<Module> modules = new LinkedList<>();

    /** Every Lynx hub in the hardware map, discovered in {@link #init()} for bulk-cache management. */
    private List<LynxModule> lynxModules;
    /** Bulk-caching mode applied to every hub; defaults to {@code MANUAL}. See {@link #setBulkCachingMode}. */
    private LynxModule.BulkCachingMode bulkCachingMode = LynxModule.BulkCachingMode.MANUAL;

    /**
     * Shared binding manager for this OpMode. Modules such as
     * {@link com.cdarobotics.cdalib.opmodes.modules.BindingModule} and
     * {@link com.cdarobotics.cdalib.opmodes.modules.TeleOpPedroModule} register and query gamepad
     * bindings against it. Exposed to subclasses so they can wire their own bindings.
     */
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
     * Hook run once at the very start of {@link #init()}, before any subsystem or module is
     * initialized. Subclasses implement it to build the robot: register subsystems with
     * {@link #registerSubsystem}, install modules with {@link #installModule}, and optionally change
     * the bulk-caching mode via {@link #setBulkCachingMode}.
     */
    protected abstract void preload();


    /**
     * Runs {@link #preload()}, applies the bulk-caching mode to every Lynx hub, then calls
     * {@link Subsystem#init()} on each registered subsystem and {@link Module#init()} on each
     * installed module, in registration order.
     */
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

    /**
     * Called repeatedly while the driver is on the init screen. Forwards to
     * {@link Subsystem#init_loop()} on each subsystem and {@link Module#init_loop()} on each module.
     */
    public void init_loop() {
        for (Subsystem subsystem : subsystems) {
            subsystem.init_loop();
        }
        for (Module module : modules) {
            module.init_loop();
        }
    }

    /**
     * Called once when play is pressed. Calls {@link Subsystem#start()} on each subsystem and
     * {@link Module#start()} on each module, then polls the task master once.
     */
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

    /**
     * The main run loop. In {@code MANUAL} bulk-caching mode, clears every hub's bulk cache once so
     * the loop's first hardware read pulls all sensors in a single transaction. Then calls
     * {@link Subsystem#update()} on each subsystem and {@link Module#loop()} on each module, and
     * polls the task master.
     */
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

    /**
     * Called once when the OpMode ends. Calls {@link Subsystem#stop()} on each subsystem and
     * {@link Module#stop()} on each module so they can safe or release hardware.
     */
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
