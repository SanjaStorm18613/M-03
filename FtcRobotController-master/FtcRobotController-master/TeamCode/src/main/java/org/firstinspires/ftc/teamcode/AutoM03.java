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

import java.util.ArrayList;
import java.util.Arrays;

@Autonomous(name = "AutoM03", group = "Linear Opmode")
public class AutoM03 extends LinearOpMode {

    DTMecanum drive;
    Elevador eleve;
    Yaw yaw;
    Garra garra;
    Braco braco;
    ElapsedTime time;
    VisionCtrl vision;

    double parkArea = 0;

    public void runOpMode() {

        drive = new DTMecanum(telemetry, hardwareMap);
        eleve = new Elevador(telemetry, hardwareMap);
        braco = new Braco(telemetry, hardwareMap, eleve);
        garra = new Garra(telemetry, hardwareMap, eleve, braco);
        yaw = new Yaw(telemetry, hardwareMap, eleve, garra, braco);

        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        time.startTime();
/*
        vision = new VisionCtrl(this, hardwareMap, telemetry);

        while (!isStarted() && !isStopRequested()) {
            switch (vision.getColorDetected()) {
                case GREEN:
                    parkArea = -50;
                case BLACK:
                    parkArea = 0;
                case CIAN:
                    parkArea = 50;
            }
            telemetry.addData("Color Detected", vision.getColorDetected());
            telemetry.update();
        }
 //*/

        ArrayList<Double> steps = new ArrayList<>(Arrays.asList(
                1.,//yw
                50.,//dr
                0.,//yw
                -50.,//dr
                -1.//yw
        ));

        ArrayList<Boolean> forward = new ArrayList<>(Arrays.asList(
                false,
                false,
                false,
                false,
                true
        ));

        ArrayList<Integer> stepCtrl = new ArrayList<>(Arrays.asList(
                //0-dr, 1-el, 2-cl, 3-yw
                3,
                0,
                3,
                0,
                3
        ));


        waitForStart();


        while (opModeIsActive() && stepCtrl.size() != 0) {

            switch (stepCtrl.get(0)) {

                case 0:
                    drive.move(forward.get(0), 0.5, 1500, 10, steps.get(0));
                    if (!drive.getBusy() && stepCtrl.size() > 0) {
                        stepCtrl.remove(0);
                        steps.remove(0);
                        forward.remove(0);
                    }
                    break;

                case 1:
                    eleve.setPos(steps.get(0), 0.7);
                    if (!eleve.getBusy() && stepCtrl.size() > 0) {
                        stepCtrl.remove(0);
                        steps.remove(0);
                    }
                    break;

                case 2:
                    garra.setClaw(steps.get(0), 2000);
                    if (!garra.getBusy() && stepCtrl.size() > 0) {
                        stepCtrl.remove(0);
                        steps.remove(0);
                    }
                    break;

                case 3:
                    yaw.setPos(steps.get(0), .9);
                    if (!yaw.getBusy() && stepCtrl.size() > 0) {
                        stepCtrl.remove(0);
                        steps.remove(0);
                    }
                    break;
            }
            //*/

            telemetry.update();
        }
    }
}
/*
            .3,//el
            .0,//cl
            50.0,//dr-f
            .5,//el
            -20.0,//dr-t
            1.0,//cl
            20.0,//dr-t
            .0,//el
            20.0,//dr-f
            parkArea//dr-t
 */
