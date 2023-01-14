package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "ElevadorCtrl", group = "Linear Opmode")

public class ElevadorCtrl extends LinearOpMode {

    DcMotor e, d;

    public void runOpMode() {

        e = hardwareMap.get(DcMotor.class, "Elevador");
        //d = hardwareMap.get(DcMotor.class, "FD");

        e.setDirection(DcMotorSimple.Direction.FORWARD);
        //d.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.dpad_up || gamepad2.dpad_up) e.setPower(0.4);
            else if (gamepad1.dpad_down || gamepad2.dpad_down) e.setPower(-0.4);
            else e.setPower(0.0);


        }
    }
}
