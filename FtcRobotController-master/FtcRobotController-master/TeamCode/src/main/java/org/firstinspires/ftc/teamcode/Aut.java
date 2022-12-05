package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystem.Braco;
import org.firstinspires.ftc.teamcode.Subsystem.DTMecanum;
import org.firstinspires.ftc.teamcode.Subsystem.Elevador;
import org.firstinspires.ftc.teamcode.Subsystem.Garra;
import org.firstinspires.ftc.teamcode.Subsystem.Yaw;

@Autonomous(name = "Auto_M-03", group = "Linear Opmode")
public class Aut extends LinearOpMode {

    DTMecanum drive;
    Elevador elev;
    Yaw yaw;
    Garra garra;
    Braco braco;
    ElapsedTime time;

    public void runOpMode() {

        drive = new DTMecanum(hardwareMap);
        elev = new Elevador(hardwareMap, telemetry);
        braco = new Braco(hardwareMap, telemetry, elev);
        garra = new Garra(hardwareMap, elev, telemetry, braco);
        yaw = new Yaw(hardwareMap, elev, telemetry, garra);

        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        waitForStart();

        time.reset();
        time.startTime();

        while (true) {
            garra.setPos(0.15, 0.01, 0);

            if (time.time() <= 100) {
                elev.setPos(0.5, 0.7);
                telemetry.addData("status", "part 1");
            } else if (time.time() < 2000) {
                garra.setPos(0.1, 0.01, 0);
                telemetry.addData("status", "part 2");

            } else if (time.time() < 4000) {
                elev.setPos(0.0, 0.7);
                telemetry.addData("status", "part 3");

            } else if (!elev.getBusy()) {
                break;
            }

            telemetry.update();
        }

    }
}
