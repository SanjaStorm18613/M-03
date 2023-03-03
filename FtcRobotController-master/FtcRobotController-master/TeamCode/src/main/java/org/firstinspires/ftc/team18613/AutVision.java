
package org.firstinspires.ftc.team18613;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.team18613.Vision.PipelineColors;
import org.firstinspires.ftc.team18613.Vision.VisionCtrl;

@Autonomous(name = "AutVision", group = "Linear Opmode")
public class AutVision extends LinearOpMode {

    VisionCtrl vision;
    PipelineColors detector;

    boolean bU = false, bD = false, bA = false, bL = false, bR = false;

    int line = 0, colun = 0, color = 0;
    boolean select = false;

    public void runOpMode() {

        vision = new VisionCtrl(this, hardwareMap, telemetry);
        /*detector = vision.getPipeline();

        while (!isStarted()) {
            telemetry.addData("Color Detected", detector.getColorDetected());
            telemetry.addData("Color Detected Area", detector.getcontourArea());
            telemetry.addLine();

            telemetry.addLine("Color 0 -");
            telemetry.addData("Up", Constants.Pipeline.COLOR_UP[0]);
            telemetry.addData("Low", Constants.Pipeline.COLOR_LOW[0]);
            telemetry.addLine();

            telemetry.addLine("Color 1 -");
            telemetry.addData("Up", Constants.Pipeline.COLOR_UP[1]);
            telemetry.addData("Low", Constants.Pipeline.COLOR_LOW[1]);

        }*/
        waitForStart();

        while (opModeIsActive()) {

            telemetry.addData("Color Detected", detector.getColorDetected());
            telemetry.addData("Color Detected Area", detector.getcontourArea());
            telemetry.addLine();

            telemetry.addLine("Color 0 -");
            telemetry.addData("Up", Constants.Pipeline.COLOR_UP[0]);
            telemetry.addData("Low", Constants.Pipeline.COLOR_LOW[0]);
            telemetry.addLine();

            telemetry.addLine("Color 1 -");
            telemetry.addData("Up", Constants.Pipeline.COLOR_UP[1]);
            telemetry.addData("Low", Constants.Pipeline.COLOR_LOW[1]);
            telemetry.update();

        }
        vision.stopStreaming();

    }
}


//*/