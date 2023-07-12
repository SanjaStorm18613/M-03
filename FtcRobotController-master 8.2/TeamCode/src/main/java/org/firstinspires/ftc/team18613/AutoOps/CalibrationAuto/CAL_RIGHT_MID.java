package org.firstinspires.ftc.team18613.AutoOps.CalibrationAuto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team18613.AutoOps.ConstantsAuto;

@TeleOp(name = "CAL_RIGHT_MID", group = "LinearOpMode")
public class CAL_RIGHT_MID extends LinearOpMode {

    CalibrationAutonomousBase autonomous;

    public void runOpMode() {

        ConstantsAuto cAuto = new ConstantsAuto();

        autonomous = new CalibrationAutonomousBase(this, cAuto);
        autonomous.initiation();
        autonomous.setSteps(cAuto.getAutoMid(false, autonomous.getColorParkArea()));

        while (opModeIsActive() && !autonomous.isFinished()) {

            autonomous.execution();

        }
    }
}