package org.firstinspires.ftc.team18613.AutoOps;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "AUTO_DEMONSTRACAO", group = "LinearOpMode")
public class AUTO_DEMONSTRACAO extends LinearOpMode {

    AutonomousBase autonomous;

    public void runOpMode() {

        ConstantsAuto cAuto = new ConstantsAuto();

        autonomous = new AutonomousBase(this, cAuto);
        autonomous.initiation();
        autonomous.setSteps(cAuto.getAutoDemonstrator());

        while (opModeIsActive() && !autonomous.isFinished()) {

            autonomous.execution();

        }
    }
}