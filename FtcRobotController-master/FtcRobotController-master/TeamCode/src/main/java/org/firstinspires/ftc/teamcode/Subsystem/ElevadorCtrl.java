package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "ElevadorCtrl", group = "Linear Opmode")

public class ElevadorCtrl extends LinearOpMode {

    DcMotor e, d;

    public void runOpMode() {

        e = hardwareMap.get(DcMotor.class, "FE");
        d = hardwareMap.get(DcMotor.class, "FD");

        e.setDirection(DcMotorSimple.Direction.FORWARD);
        d.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            e.setPower(gamepad1.left_stick_y);
            d.setPower(gamepad1.left_stick_y);

        }
    }
}
