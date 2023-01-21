
package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystem.Braco;
import org.firstinspires.ftc.teamcode.Subsystem.Constantis;
import org.firstinspires.ftc.teamcode.Subsystem.DTMecanum;
import org.firstinspires.ftc.teamcode.Subsystem.Elevador;
import org.firstinspires.ftc.teamcode.Subsystem.Garra;
import org.firstinspires.ftc.teamcode.Subsystem.Yaw;
import org.firstinspires.ftc.teamcode.Vision.PipelineColors;
import org.firstinspires.ftc.teamcode.Vision.VisionCtrl;

@Autonomous(name = "AutVision", group = "Linear Opmode")
public class AutVision extends LinearOpMode {

    VisionCtrl vision;
    PipelineColors detector;

    boolean bU = false, bD = false, bA = false, bL = false, bR = false;

    int line = 0, colun = 0, color = 0;
    boolean select = false;

    public void runOpMode() {

        vision = new VisionCtrl(this, hardwareMap, telemetry);
        detector = vision.getPipeline();

        while (!isStarted()) {
            telemetry.addData("Color Detected", detector.getColorDetected());
            telemetry.addData("Color Detected Area", detector.getcontourArea());
            telemetry.addLine();

            telemetry.addLine("Color 0 -");
            telemetry.addData("Up", Constantis.Pipeline.COLOR_UP[0]);
            telemetry.addData("Low", Constantis.Pipeline.COLOR_LOW[0]);
            telemetry.addLine();

            telemetry.addLine("Color 1 -");
            telemetry.addData("Up", Constantis.Pipeline.COLOR_UP[1]);
            telemetry.addData("Low", Constantis.Pipeline.COLOR_LOW[1]);

        }
        //waitForStart();

        while (opModeIsActive()) {

            telemetry.addData("Color Detected", detector.getColorDetected());
            telemetry.addData("Color Detected Area", detector.getcontourArea());
            telemetry.addLine();

            telemetry.addLine("Color 0 -");
            telemetry.addData("Up", Constantis.Pipeline.COLOR_UP[0]);
            telemetry.addData("Low", Constantis.Pipeline.COLOR_LOW[0]);
            telemetry.addLine();

            telemetry.addLine("Color 1 -");
            telemetry.addData("Up", Constantis.Pipeline.COLOR_UP[1]);
            telemetry.addData("Low", Constantis.Pipeline.COLOR_LOW[1]);
            telemetry.update();

        }
        vision.stopStreaming();

    }
}


//*/