package com.cdarobotics.cdalib.devices.actuators;

import com.cdarobotics.cdalib.controllers.Controller;
import com.cdarobotics.cdalib.controllers.NullController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class VelocityMotorDevice extends MotorDevice {

    private Controller controller;

    private double velocity;

    private double lastTime;

    public VelocityMotorDevice(DcMotorEx motor, Controller controller) {
        super(motor);
        this.controller = controller;
        velocity = this.controller.getTarget();
    }

    public VelocityMotorDevice(HardwareMap hardwareMap, String name, Controller controller) {
        super(hardwareMap, name);
        this.controller = controller;
        velocity = this.controller.getTarget();
    }

    public VelocityMotorDevice(DcMotorEx motor) {
        super(motor);
        this.controller = new NullController(0);
        velocity = 0;
    }

    public VelocityMotorDevice(HardwareMap hardwareMap, String name) {
        super(hardwareMap, name);
        this.controller = new NullController(0);
        velocity = 0;
    }

    public void setController(Controller controller) {
        this.controller = controller;
        velocity = this.controller.getTarget();
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
        controller.setTarget(velocity);
    }

    @Override
    public void update() {

        double currentTime = System.nanoTime() / 1000000000.0;
        double dt = currentTime - lastTime;
        lastTime = currentTime;

        this.setPower(controller.update(this.getCurrentVelocity(), dt));

        super.update();
    }
}
