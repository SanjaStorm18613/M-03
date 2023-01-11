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

@Autonomous(name = "AutoM03", group = "Linear Opmode")
public class AutoM03 extends LinearOpMode {

    DTMecanum drive;
    Elevador eleve;
    Yaw yaw;
    Garra garra;
    Braco braco;
    ElapsedTime time;
    VisionCtrl vision;

    double[] distances = {100, 50, -50, 20, 0};
    double[] rotations = {90, -90};

    int step = 0;

    public void runOpMode() {

        drive = new DTMecanum(telemetry, hardwareMap);
        eleve = new Elevador(telemetry, hardwareMap);
        braco = new Braco(telemetry, hardwareMap, eleve);
        garra = new Garra(telemetry, hardwareMap, eleve, braco);
        yaw = new Yaw(telemetry, hardwareMap, eleve, garra, braco);

        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
/*
        vision = new VisionCtrl(this, hardwareMap, telemetry);

        while (!isStarted() && !isStopRequested()) {
            switch (vision.getColorDetected()) {
                case GREEN:
                    distances[4] = -50;
                case BLACK:
                    distances[4] = 0;
                case CIAN:
                    distances[4] = 50;
            }
            telemetry.addData("Color Detected", vision.getColorDetected());
            telemetry.update();
        }
 */
        waitForStart();

        while (opModeIsActive()) {

            drive.move(false, 0.9, 1500, 10, distances[0]);

            if (!drive.isBusy()) {
                step = Math.min(distances.length - 1, step + 1);
            }

            telemetry.addData("IDX", step);
            telemetry.addData("isBusy", drive.isBusy());
            drive.getTelemetry();
            telemetry.update();
        }
    }
}
