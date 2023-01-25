package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "EncoderTest", group = "Linear Opmode")

public class EncoderTest extends LinearOpMode {

    DcMotor e, d;

    int setPoint = 50;
    double erro = 0;

    public void runOpMode() {

        e = hardwareMap.get(DcMotor.class, "encE");
        d = hardwareMap.get(DcMotor.class, "encD");

        e.setDirection(DcMotorSimple.Direction.REVERSE);
        d.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            erro = setPoint - e.getCurrentPosition();

            erro *= 0.001;
            e.setPower(erro);

            telemetry.addData("power", e.getPower());
            telemetry.addData("pos", e.getCurrentPosition());

            telemetry.update();

        }
    }
}
