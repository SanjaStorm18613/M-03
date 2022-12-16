package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "ElevadorCtrl", group = "Linear Opmode")

public class ElevadorCtrl extends LinearOpMode {

    DcMotor elev;

    public void runOpMode() {

        elev = hardwareMap.get(DcMotor.class, "Elevador");
        elev.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            if(gamepad1.dpad_up || gamepad2.dpad_up) {
                elev.setPower(0.5);
            } else if (gamepad1.dpad_down || gamepad2.dpad_down) {
                elev.setPower(-0.5);
            } else {
                elev.setPower(0);
            }
        }
    }
}
