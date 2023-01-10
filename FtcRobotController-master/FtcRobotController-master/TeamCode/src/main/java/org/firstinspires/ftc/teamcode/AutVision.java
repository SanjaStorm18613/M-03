
package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystem.Braco;
import org.firstinspires.ftc.teamcode.Subsystem.DTMecanum;
import org.firstinspires.ftc.teamcode.Subsystem.Elevador;
import org.firstinspires.ftc.teamcode.Subsystem.Garra;
import org.firstinspires.ftc.teamcode.Subsystem.Yaw;
import org.firstinspires.ftc.teamcode.Vision.VisionCtrl;

@Autonomous(name = "AutVision", group = "Linear Opmode")
public class AutVision extends LinearOpMode {

    VisionCtrl vision;


    public void runOpMode() {

        vision = new VisionCtrl(this, hardwareMap, telemetry);


        while (!isStarted()) {
            telemetry.addData("color", vision.getColorDetected());
            telemetry.addData("Area", vision.getcontourArea());
            telemetry.update();

        }
        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("color", vision.getColorDetected());
            telemetry.addData("Area", vision.getcontourArea());
            telemetry.update();
        }
        vision.stopStreaming();

    }
}


//*/