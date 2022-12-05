package org.firstinspires.ftc.teamcode.Subsystem;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class DTMecanum {

    Constantis.DTMecanum Const;

    DcMotor FE, FD, TE, TD;

    ElapsedTime pidT;
    PID pid = new PID();


    public DTMecanum (@NonNull HardwareMap hardwareMap) {


        //pidT = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

//Cria motores
        FE = hardwareMap.get(DcMotor.class, "FE");
        FD = hardwareMap.get(DcMotor.class, "FD");
        TE = hardwareMap.get(DcMotor.class, "TE");
        TD = hardwareMap.get(DcMotor.class, "TD");
        DcMotor motors[] = {FE, TE, FD, TD};

        FE.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FD.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FE.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FD.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        TE.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TE.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        for (int m = 0; m < 4; m++) {
            motors[m].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motors[m].setDirection(m > 1 ? DcMotor.Direction.FORWARD :
                                            DcMotor.Direction.REVERSE);

        }
    }

//Controle movimentação mecanum
    public void Control(double x, double y, double yaw) {

        FE.setPower((y+x+yaw) * 0.6);
        FD.setPower((y-x-yaw) * 0.6);
        TE.setPower((y-x+yaw) * 0.6);
        TD.setPower((y+x-yaw) * 0.6);

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
