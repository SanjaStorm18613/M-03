package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Subsystem.DTMecanum;
import org.firstinspires.ftc.teamcode.Subsystem.Elevador;

@TeleOp(name="Robot", group="Linear Opmode")

public class Robot extends LinearOpMode {

    Elevador  elevador = new Elevador(hardwareMap);
    DTMecanum DTM       = new DTMecanum(hardwareMap);

    @Override
    public void runOpMode() {

        elevador.subir(gamepad1.a, gamepad1.x, gamepad1.y, gamepad1.b);
        DTM.drive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

    }
}

