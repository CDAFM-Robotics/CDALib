package com.cdarobotics.cdalib.devices.actuators;

import com.cdarobotics.cdalib.controllers.Controller;
import com.cdarobotics.cdalib.controllers.NullController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class PositionMotorDevice extends MotorDevice {

    private int position;

    private Controller controller;

    private double lastTime;

    public PositionMotorDevice(DcMotorEx motor, Controller controller) {
        super(motor);
        this.controller = controller;
        position = (int) controller.getTarget();
    }

    public PositionMotorDevice(HardwareMap hardwareMap, String name, Controller controller) {
        super(hardwareMap, name);
        this.controller = controller;
        position = (int) controller.getTarget();
    }

    public PositionMotorDevice(DcMotorEx motor) {
        super(motor);
        this.controller = new NullController(0);
    }

    public PositionMotorDevice(HardwareMap hardwareMap, String name) {
        super(hardwareMap, name);
        this.controller = new NullController(0);
    }

    public void setController(Controller controller) {
        this.controller = controller;
        position = (int) this.controller.getTarget();
    }

    public void setPosition(int position) {
        this.position = position;
        controller.setTarget(position);
    }

    @Override
    public void update() {

        double currentTime = System.nanoTime() / 1000000000.0;
        double dt = currentTime - lastTime;
        lastTime = currentTime;

        this.setPower(controller.update(this.getCurrentPosition(), dt));

        super.update();
    }
}
