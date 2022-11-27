package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Subsystem.DTMecanum;
import org.firstinspires.ftc.teamcode.Subsystem.Elevador;

@TeleOp(name="TeleOp_M-03", group="Linear Opmode")

public class Tele extends LinearOpMode {

    //
    //Garra garra = new Garra(hardwareMap);
    DTMecanum DT;
    Elevador Elev;

    public void runOpMode() {

        DT = new DTMecanum(hardwareMap);
        Elev = new Elevador(hardwareMap);

        waitForStart();


        while (opModeIsActive()) {

            //DT.drive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
            Elev.nivel(gamepad1.dpad_up, gamepad1.dpad_down, gamepad1.y, gamepad1.a);

        }
        DT.drive(0, 0, 0);
    }
}
