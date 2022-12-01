package org.firstinspires.ftc.teamcode;

import android.util.Log;

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
    //Garra garra = new Garra(hardwareMap);
    DTMecanum drive;
    Elevador elev;
    Yaw yaw;
    Garra garra;
    Braco braco;

    public void runOpMode() {

        drive = new DTMecanum(hardwareMap);
        yaw = new Yaw(hardwareMap);
        elev = new Elevador(hardwareMap);
        garra = new Garra(hardwareMap, elev);
        braco = new Braco(hardwareMap, elev);

        waitForStart();


        while (opModeIsActive()) {

            drive.drive(
                    gamepad1.left_stick_x,
                    -gamepad1.left_stick_y,
                    gamepad1.right_stick_x);

            elev.Control(
                    gamepad2.dpad_up,
                    gamepad2.dpad_down,
                    gamepad1.dpad_up || gamepad2.y,
                    gamepad1.dpad_down || gamepad2.x);

            braco.Control();

            yaw.Control(
                    gamepad2.right_bumper,
                    gamepad2.left_bumper,
                    gamepad2.dpad_right,
                    gamepad2.dpad_left);

            garra.Control(
                    gamepad2.right_stick_button,
                    gamepad2.y,
                    gamepad2.a,
                    gamepad2.b,
                    gamepad1.a);


        }
        drive.drive(0, 0, 0);
    }
}
