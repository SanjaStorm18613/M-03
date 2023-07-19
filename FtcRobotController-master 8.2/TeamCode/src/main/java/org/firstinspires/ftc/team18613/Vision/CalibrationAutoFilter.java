package org.firstinspires.ftc.team18613.Vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team18613.Constants;

import java.util.Arrays;

@TeleOp(name = "CalibrationAutoFilter", group = "Linear Opmode")

public class CalibrationAutoFilter extends LinearOpMode {

    VisionCtrl webcam;
    PipelineColors detector;

    int idValue = 0, idScalar = 0, idColor = 0;
    double[][][] newFilter;
    boolean A = true, Y = true, UP = true, DOWN = true, RIGHT = true, LEFT = true, action = false;
    String[] scalar = {"LOW", "HIGH"}, color = {"VERDE", "AMARELO"},
            value = {"H", "L", "S"};

    public void runOpMode() {
        detector = new PipelineColors();

        webcam = new VisionCtrl(this, hardwareMap, telemetry, "Webcam 1");
        webcam.setPepiline(detector);

        detector.setStreaming(true);

        newFilter = new double[][][] {Constants.Pipeline.AUTO_COLOR_LOW, Constants.Pipeline.AUTO_COLOR_UP};

        while (!isStarted() && !isStopRequested()) {

            if (gamepad1.dpad_right && RIGHT) {
                if (idValue > 1) idValue = 0;
                else idValue++;
            }
            if (gamepad1.dpad_left && LEFT) {
                if (idValue < 1) idValue = 2;
                else idValue--;
            }

            if (gamepad1.dpad_up && UP) {
                if (idScalar > 0) {
                    idScalar = 0;
                    if (idColor > 0) {
                        idColor = 0;
                    } else idColor++;
                } else idScalar++;
            }
            if (gamepad1.dpad_down && DOWN) {
                if (idScalar < 1) {
                    idScalar = 1;
                    if (idColor < 1) {
                        idColor = 1;
                    } else idColor--;
                } else idScalar--;
            }

            if (gamepad1.a && A) {
                newFilter[idScalar][idColor][idValue] -= 5;
                action = true;
            }
            if (gamepad1.y && Y) {
                newFilter[idScalar][idColor][idValue] += 5;
                action = true;
            }

            A = !gamepad1.a;
            Y = !gamepad1.y;
            UP = !gamepad1.dpad_up;
            DOWN = !gamepad1.dpad_down;
            RIGHT = !gamepad1.dpad_right;
            LEFT = !gamepad1.dpad_left;

            if (action) {
                detector.setFilter(newFilter);
            }

            telemetry.addLine("     ^             ^");
            telemetry.addLine(color[idColor]+" | "+scalar[idScalar]);
            telemetry.addLine("     v             v");
            telemetry.addLine("< H | L | S >");
            telemetry.addLine(Arrays.toString(newFilter[idScalar][idColor]));
            telemetry.addLine("--------------------------------------------");
            telemetry.addData(value[idValue], newFilter[idScalar][idColor][idValue]);
            telemetry.update();
        }

        webcam.stopDetection();

        while (opModeIsActive()) {
            for (int s = 0; s <= 1; s++) {
                    for (int c = 0; c <= 1; c++) {
                        telemetry.addLine(color[c]+" | "+scalar[s]);
                        telemetry.addLine(Arrays.toString(newFilter[s][c]));
                        telemetry.addLine();
                    }
                }

            telemetry.update();
        }
    }
}
