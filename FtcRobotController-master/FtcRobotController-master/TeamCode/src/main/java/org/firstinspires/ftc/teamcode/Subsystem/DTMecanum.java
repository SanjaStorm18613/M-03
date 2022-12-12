package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DTMecanum {

    Telemetry telemetry;

    Constantis.DTMecanum dtMVar;

    DcMotorEx FE, FD, TE, TD;

    ElapsedTime pidT;
    PID pid;

    public DTMecanum(Telemetry t,  HardwareMap hardwareMap) {

        telemetry = t;

        //pid = new PID(0, 0, 0);

//Cria motores
        FE = hardwareMap.get(DcMotorEx.class, "FE");
        FD = hardwareMap.get(DcMotorEx.class, "FD");
        TE = hardwareMap.get(DcMotorEx.class, "TE");
        TD = hardwareMap.get(DcMotorEx.class, "TD");
        DcMotor motors[] = {FE, TE, FD, TD};


        for (int m = 0; m < 4; m++) {

            motors[m].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motors[m].setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            motors[m].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motors[m].setDirection(m > 1 ? DcMotor.Direction.FORWARD :
                    DcMotor.Direction.REVERSE);


        }
    }

    //Controle movimentação mecanum
    public void Control(double x, double y, double yaw) {

        yaw = Math.pow(yaw, 2.5) * dtMVar.SPEED * dtMVar.YAW_SPEED;
        x   = Math.pow(x, 2.5) * dtMVar.SPEED;
        y   = Math.pow(y, 2.5) * dtMVar.SPEED;

       /*
        yaw = s * 0.7;
        x   = s;
        y   = s;
//*/
        FE.setPower(y + x + yaw);
        FD.setPower(y - x - yaw);
        TE.setPower(y - x + yaw);
        TD.setPower(y + x - yaw);

    }
/*
    public void andar(double dis, double rot, double mxvel) {

        dis *= 28;
        double encMed = (FE.getCurrentPosition() + FD.getCurrentPosition()) / 2;
        double vel;

        while (dis+10 > encMed && encMed > dis-10){

            vel = pid.PIDCorrect(encMed, dis);
            if (Math.abs(vel) > mxvel) vel = mxvel * sg(vel);


            FE.setPower(vel);
            FD.setPower(vel);
            TE.setPower(vel);
            TD.setPower(vel);

        }


    }


//*/
}
