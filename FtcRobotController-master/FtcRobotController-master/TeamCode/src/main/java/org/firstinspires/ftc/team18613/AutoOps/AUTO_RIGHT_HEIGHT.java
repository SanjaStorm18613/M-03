package org.firstinspires.ftc.team18613.AutoOps;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "AUTO_RIGHT_HEIGHT", group = "LinearOpMode")
public class AUTO_RIGHT_HEIGHT extends LinearOpMode {

    AutonomousBase autonomous;

    public void runOpMode() {

        ContantsAuto cAuto = new ContantsAuto();

        autonomous = new AutonomousBase(this, cAuto);
        autonomous.initiation();
        autonomous.setSteps(cAuto.getAutoHeigth(false, autonomous.getColorParkArea()));

        while (opModeIsActive() && autonomous.isFinished()) {

            autonomous.execution();

        }
    }
}