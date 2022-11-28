package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.DTMecanum;
import org.firstinspires.ftc.teamcode.Subsystem.Elevador;
import org.firstinspires.ftc.teamcode.Subsystem.Yaw;

@TeleOp(name = "TeleOp_M-03", group = "Linear Opmode")

public class Tele extends LinearOpMode {

    //
    //Garra garra = new Garra(hardwareMap);
    DTMecanum DT;
    Elevador elev;
    Yaw yaw;

    public void runOpMode() {

        DT = new DTMecanum(hardwareMap);
        elev = new Elevador(hardwareMap);
        yaw = new Yaw(hardwareMap);

        waitForStart();


        while (opModeIsActive()) {

            DT.drive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
            elev.Control(gamepad1.dpad_up, gamepad1.dpad_down, gamepad1.y, gamepad1.a);
            yaw.Control(gamepad1.b, gamepad1.x, gamepad1.dpad_right, gamepad1.dpad_left);


        }
        DT.drive(0, 0, 0);
    }
}
