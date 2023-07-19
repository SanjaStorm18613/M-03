package org.firstinspires.ftc.team18613.Vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team18613.Constants;

import java.util.Arrays;

@TeleOp(name = "CalibrationTeleFilter", group = "Linear Opmode")

public class CalibrationTeleFilter extends LinearOpMode {

    VisionCtrl webcam;
    TrackingJunction detector;

    int idValue = 0, idScalar = 0;
    double[][] newFilter;
    boolean A = true, Y = true, UP = true, DOWN = true, RIGHT = true, LEFT = true, action = false;
    String[] scalar = {"LOW", "HIGH"}, value = {"H", "L", "S"};

    public void runOpMode() {
        detector = new TrackingJunction();

        webcam = new VisionCtrl(this, hardwareMap, telemetry, "Webcam 2");
        webcam.setPepiline(detector);

        detector.setStreaming(true);

        newFilter = new double[][] {Constants.Pipeline.TELE_COLOR_LOW, Constants.Pipeline.TELE_COLOR_UP};

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
                } else idScalar++;
            }
            if (gamepad1.dpad_down && DOWN) {
                if (idScalar < 1) {
                    idScalar = 1;
                } else idScalar--;
            }

            if (gamepad1.a && A) {
                newFilter[idScalar][idValue] -= 5;
                action = true;
            }
            if (gamepad1.y && Y) {
                newFilter[idScalar][idValue] += 5;
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

            telemetry.addLine("     ^");
            telemetry.addLine(scalar[idScalar]);
            telemetry.addLine("     v");
            telemetry.addLine("< H | L | S >");
            telemetry.addLine(Arrays.toString(newFilter[idScalar]));
            telemetry.addLine("--------------------------------------------");
            telemetry.addData(value[idValue], newFilter[idScalar][idValue]);
            telemetry.update();
        }

        webcam.stopDetection();

        while (opModeIsActive()) {
            for (int s = 0; s <= 1; s++) {
                    telemetry.addLine(scalar[s]);
                    telemetry.addLine(Arrays.toString(newFilter[s]));
                    telemetry.addLine();
                }

            telemetry.update();
        }
    }
}
