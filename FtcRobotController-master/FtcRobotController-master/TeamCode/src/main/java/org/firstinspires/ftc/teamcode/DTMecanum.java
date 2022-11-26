package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DTMecanum {

    DcMotor FE, FD, TE, TD;

    public DTMecanum (HardwareMap hardwareMap) {

        FE = hardwareMap.get(DcMotor.class, "FE");
        FD = hardwareMap.get(DcMotor.class, "FD");
        TE = hardwareMap.get(DcMotor.class, "TE");
        TD = hardwareMap.get(DcMotor.class, "TD");
    }

    public void drive(double x, double y, double yaw) {

        FE.setPower(y+x+yaw);
        FD.setPower(y-x-yaw);
        TE.setPower(y-x+yaw);
        TD.setPower(y+x-yaw);

    }

}
