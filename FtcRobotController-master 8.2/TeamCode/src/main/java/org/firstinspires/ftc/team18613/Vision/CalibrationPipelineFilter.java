package org.firstinspires.ftc.team18613.Vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team18613.Constants;

import java.util.Arrays;

@TeleOp(name = "CalibrationPipelineFilter", group = "Linear Opmode")

public class CalibrationPipelineFilter extends LinearOpMode {

    SwitchCameraVisionCtrl webcam;
    PipelineColors detectorAuto;
    TrackingJunction detectorTele;

    int idValue = 0, idScalar = 0, idColor = 0, idPipeline = 0;
    double[][][][] newFilter;
    boolean A = true, Y = true, UP = true, DOWN = true, RIGHT = true, LEFT = true, action = false;
    String[] pipeline = {"AUTO", "TELE"}, scalar = {"LOW", "HIGH"}, color = {"VERDE", "AMARELO"},
            value = {"H", "L", "S"};

    public void runOpMode() {

        webcam = new SwitchCameraVisionCtrl(this, hardwareMap, telemetry);

        //detectorAuto = webcam.getPipelineAuto();
        detectorTele = webcam.getPipelineTele();
        detectorTele.setStreaming(true);

        newFilter = new double[][][][] {{Constants.Pipeline.AUTO_COLOR_LOW,
                                        Constants.Pipeline.AUTO_COLOR_UP},
                                        {Constants.Pipeline.TELE_COLOR_LOW,
                                        Constants.Pipeline.TELE_COLOR_UP}};

        while (!isStarted() && !isStopRequested()) {

            if (gamepad1.dpad_right && RIGHT) {
                if (idValue > 3) idValue = 0;
                else idValue++;
            }
            if (gamepad1.dpad_left && LEFT) {
                if (idValue < 1) idValue = 3;
                else idValue--;
            }

            if (gamepad1.dpad_up && UP) {
                if (idScalar > 0) {
                    idScalar = 0;
                    if (idColor > 0 || idPipeline == 1) {
                        idColor = 0;
                        if (idPipeline > 0) idPipeline = 0;
                        else idPipeline++;
                    } else idColor++;
                } else idScalar++;
            }
            if (gamepad1.dpad_down && DOWN) {
                if (idScalar < 1) {
                    idScalar = 1;
                    if (idColor < 1) {
                        idColor = idPipeline == 1 ? 0 : 1;
                        if (idPipeline < 1) idPipeline = 1;
                        else idPipeline--;
                    } else idColor--;
                } else idScalar--;
            }

            if (gamepad1.a && A) {
                newFilter[idPipeline][idScalar][idColor][idValue] -= 5;
                action = true;
            }
            if (gamepad1.y && Y) {
                newFilter[idPipeline][idScalar][idColor][idValue] += 5;
                action = true;
            }

            A = !gamepad1.a;
            Y = !gamepad1.y;
            UP = !gamepad1.dpad_up;
            DOWN = !gamepad1.dpad_down;
            RIGHT = !gamepad1.dpad_right;
            LEFT = !gamepad1.dpad_left;

            if (action) {
                //detectorAuto.setFilter(newFilter[0]);
                detectorTele.setFilter(newFilter[1]);
            }

            telemetry.addLine("     ^             ^          ^");
            telemetry.addLine(pipeline[idPipeline]+" | "+
                                    color[idPipeline == 1 ? 1 : idColor]+" | "+scalar[idScalar]);
            telemetry.addLine("     v             v          v");
            telemetry.addLine("< H | L | S >");
            telemetry.addLine(Arrays.toString(newFilter[idPipeline][idScalar][idPipeline == 1 ? 0 : idColor]));
            telemetry.addLine("--------------------------------------------");
            telemetry.addData(value[idValue], newFilter[idPipeline][idScalar][idPipeline == 1 ? 0 : idColor][idValue]);
            telemetry.update();
        }
        webcam.stopDetection();

        telemetry.update();
        for (int p = 0; p <= 1; p++) {
            for (int s = 0; s <= 1; s++) {
                for (int c = 0; c <= 1; c++) {
                    if (p == 1 && c == 1) continue;
                    telemetry.addLine(pipeline[p]+" | "+color[p == 1 ? 1 : c]+" | "+scalar[s]);
                    telemetry.addLine(Arrays.toString(newFilter[p][idScalar][p == 1 ? 0 : c]));
                    /*int i = 0;
                    for (double val : newFilter[p][s][p == 1 ? 0 : c]) {
                        telemetry.addData(value[i], val);
                        i++;
                    }*/
                    telemetry.addLine();
                }
            }
        }
        telemetry.update();

        while (opModeIsActive()) {
        }
    }
}
