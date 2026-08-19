# CDALib

A FIRST Tech Challenge (FTC) robot-code library built on top of [PedroPathing](https://pedropathing.com/)
and [PhotonFTC](https://github.com/Eeshwar-Krishnan/PhotonFTC). CDALib provides a structured way to
build OpModes out of reusable parts — subsystems, devices, controllers, bindings, and a task/command
system — with Lynx hub bulk caching and low loop times handled for you.

[![](https://jitpack.io/v/CDAFM-Robotics/CDALib.svg)](https://jitpack.io/#CDAFM-Robotics/CDALib)

## Features

- **`ModularOpMode`** — a base OpMode that drives registered subsystems and modules through their
  init / start / update lifecycles automatically, and manages Lynx (Control/Expansion Hub) bulk
  caching so each loop does a single bulk read.
- **Subsystems & devices** — wrappers for motors, servos, CR servos, and sensors (IMU, color,
  distance, touch, analog/digital input, REV Color Sensor V3).
- **Controllers** — PID, feedforward, and composable controllers.
- **Task system** — sequential, parallel, race, deadline, repeat, deferred, and path-following tasks.
- **Bindings** — declarative gamepad button/analog bindings, including toggles.
- **PedroPathing & PhotonFTC** are exposed transitively — you get them by depending on CDALib.

## Installation

CDALib is distributed through [JitPack](https://jitpack.io). Add the JitPack repository, then the
dependency.

### 1. Add the JitPack repository

In your FTC project's **`settings.gradle`** (or the top-level `build.gradle`'s `allprojects` /
`dependencyResolutionManagement` block), add JitPack to the repositories:

```gradle
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### 2. Add the dependency

In **`TeamCode/build.gradle`**, add:

```gradle
dependencies {
    implementation 'com.github.CDAFM-Robotics:CDALib:VERSION'
}
```

Replace `VERSION` with a released tag (for example `v0.1.0`) or a commit hash. The exact,
copy-pasteable snippet for each release is shown on the
[JitPack page](https://jitpack.io/#CDAFM-Robotics/CDALib).

> **Note:** CDALib pulls in PedroPathing and PhotonFTC transitively, so you do not need to add those
> separately. The FTC SDK (`RobotCore`, `Hardware`) is provided by the Robot Controller app at
> runtime and is not bundled.

## Quick start

Create an OpMode that extends `ModularOpMode`, register your subsystems in `preload()`, and CDALib
runs the lifecycle for you:

```java
import com.cdarobotics.cdalib.opmodes.ModularOpMode;
import com.outoftheboxrobotics.photoncore.Photon;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Photon                                   // optional: enable PhotonFTC for lower loop times
@TeleOp(name = "My TeleOp (CDALib)")
public class MyTeleOp extends ModularOpMode {

    private MyDriveSubsystem drive;

    @Override
    protected void preload() {
        drive = new MyDriveSubsystem(hardwareMap);
        registerSubsystem(drive);         // driven through init/start/update automatically
        // installModule(...) to add modules, bindingManager for gamepad bindings
    }
}
```

## Requirements

- FTC SDK `11.1.0`+ (2025–2026 season)
- Android `minSdk 24`, `compileSdk 36`
- JDK 17 to build from source (AGP 8.13)

## License

CDALib is released under the [MIT License](LICENSE).
