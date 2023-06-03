package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.team18613.Constants;

@TeleOp(name = "ElevadorCtrl", group = "Linear Opmode")

public class ElevadorCtrl extends LinearOpMode {

    DcMotor e, d;

    public void runOpMode() {

        e = hardwareMap.get(DcMotor.class, "Elevador");
        //d = hardwareMap.get(DcMotor.class, "FD");

        e.setDirection(DcMotorSimple.Direction.REVERSE);
        e.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //d.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.dpad_up || gamepad2.dpad_up) e.setPower(0.4);
            else if (gamepad1.dpad_down || gamepad2.dpad_down) e.setPower(-0.4);
            else e.setPower(0.0);

            telemetry.addData("pos", e.getCurrentPosition() / (double) Constants.Elevator.CONVR);
            telemetry.update();

        }
    }
}
