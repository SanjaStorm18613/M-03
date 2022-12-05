package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.Braco;
import org.firstinspires.ftc.teamcode.Subsystem.DTMecanum;
import org.firstinspires.ftc.teamcode.Subsystem.Elevador;
import org.firstinspires.ftc.teamcode.Subsystem.Garra;
import org.firstinspires.ftc.teamcode.Subsystem.Yaw;

@TeleOp(name = "TeleOp_M-03", group = "Linear Opmode")

public class Tele extends LinearOpMode {

    //
    DTMecanum drive;
    Elevador elev;
    Yaw yaw;
    Garra garra;
    Braco braco;

    public void runOpMode() {

        drive = new DTMecanum(hardwareMap);
        elev = new Elevador(hardwareMap, telemetry);
        braco = new Braco(hardwareMap, telemetry, elev);
        garra = new Garra(hardwareMap, elev, telemetry, braco);
        yaw = new Yaw(hardwareMap, elev, telemetry, garra);
        //*/

        waitForStart();


        while (opModeIsActive()) {

            drive.Control(
                    gamepad1.left_stick_x,
                    -gamepad1.left_stick_y,
                    gamepad1.right_stick_x);

            yaw.Control(
                    gamepad2.right_bumper,
                    gamepad2.left_bumper,
                    gamepad2.dpad_right,
                    gamepad2.dpad_left);

            braco.Control();

            garra.Control(
                    gamepad2.a,
                    gamepad1.a,
                    gamepad1.b,
                    gamepad1.x,
                    gamepad2.b,
                    gamepad2.right_stick_button);

            elev.Control(
                    gamepad2.y,
                    gamepad2.x,
                    gamepad1.dpad_up || gamepad2.dpad_up,
                    gamepad1.dpad_down || gamepad2.dpad_down);


            //*/

            telemetry.update();

        }
        drive.Control(0, 0, 0);
    }
}
