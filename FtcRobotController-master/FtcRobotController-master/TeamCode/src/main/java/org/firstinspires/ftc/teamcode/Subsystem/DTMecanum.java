package org.firstinspires.ftc.teamcode.Subsystem;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DTMecanum {

    DcMotor FE, FD, TE, TD;
    DcMotor[] motors = {FE, TE, FD, TD};

    public DTMecanum (@NonNull HardwareMap hardwareMap) {

        FE = hardwareMap.get(DcMotor.class, "FE");
        FD = hardwareMap.get(DcMotor.class, "FD");
        TE = hardwareMap.get(DcMotor.class, "TE");
        TD = hardwareMap.get(DcMotor.class, "TD");

        for (int m = 0; m < motors.length; m++) {
            motors[m].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motors[m].setDirection(m < 2 ? DcMotorSimple.Direction.FORWARD :
                                            DcMotorSimple.Direction.REVERSE);
        }
    }

    public void drive(double x, double y, double yaw) {

        FE.setPower(y+x+yaw);
        FD.setPower(y-x-yaw);
        TE.setPower(y-x+yaw);
        TD.setPower(y+x-yaw);

    }
}
