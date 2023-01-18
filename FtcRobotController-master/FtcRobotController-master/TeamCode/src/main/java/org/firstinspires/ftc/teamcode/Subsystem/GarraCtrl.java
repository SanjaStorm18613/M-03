package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "GarraCtrl", group = "Linear Opmode")

public class GarraCtrl extends LinearOpMode {

    Servo e, d;

    public void runOpMode() {

        e = hardwareMap.get(Servo.class, "GarraE");
        d = hardwareMap.get(Servo.class, "GarraD");

        //d = hardwareMap.get(DcMotor.class, "FD");

        e.setDirection(Servo.Direction.FORWARD);
        d.setDirection(Servo.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.dpad_up || gamepad2.dpad_up) {
                d.setPosition(0.0);
                e.setPosition(0.0);
            }
            else {
                e.setPosition(1);
                d.setPosition(1);

            }


        }
    }
}
