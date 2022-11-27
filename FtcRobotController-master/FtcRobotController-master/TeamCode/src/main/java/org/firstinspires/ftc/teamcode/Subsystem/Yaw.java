package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Yaw {

    DcMotor yaw;

    boolean next = false, prev = true;

    public Yaw(HardwareMap hardwareMap){

        yaw = hardwareMap.get(DcMotor.class,"yaw");

    }

    public void moveAng(boolean nxt, boolean prv){

        if (nxt){
        }
    }

}
