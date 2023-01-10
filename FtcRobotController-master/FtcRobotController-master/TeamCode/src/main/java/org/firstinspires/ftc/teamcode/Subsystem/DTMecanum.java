package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DTMecanum {

    Telemetry telemetry;

    double  s = Constantis.DTMecanum.SPEED,
            sY = Constantis.DTMecanum.YAW_SPEED,
            acclT = Constantis.DTMecanum.ACCELERATION,
            precs = Constantis.DTMecanum.PRECISION;


    Servo odmE, odmD;
    DcMotorEx FE, FD, TE, TD, encE, encD;

    boolean odmActv = false;
    double accl = 0;

    ElapsedTime pidT, moveT;
    PID pid;

    double giro, pos, setPoint = 0;
    ElapsedTime acel;
    boolean moveIsBusy = false;

    public DTMecanum(Telemetry t,  HardwareMap hardwareMap) {

        moveT = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        moveT.startTime();
        telemetry = t;

        //pid = new PID(0, 0, 0);

        odmE = hardwareMap.get(Servo.class, "odmE");
        odmD = hardwareMap.get(Servo.class, "odmD");

        odmE.setDirection(Servo.Direction.FORWARD);
        odmD.setDirection(Servo.Direction.FORWARD);

//Cria motores
        FE = hardwareMap.get(DcMotorEx.class, "FE");
        FD = hardwareMap.get(DcMotorEx.class, "FD");
        TE = hardwareMap.get(DcMotorEx.class, "TE");
        TD = hardwareMap.get(DcMotorEx.class, "TD");

        DcMotor[] motors = {FE, TE, FD, TD};


        for (int m = 0; m < 4; m++) {

            motors[m].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motors[m].setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            motors[m].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motors[m].setDirection(m > 1 ? DcMotor.Direction.FORWARD :
                    DcMotor.Direction.REVERSE);
        }

        encE = hardwareMap.get(DcMotorEx.class, "encE");
        encD = hardwareMap.get(DcMotorEx.class, "encE");

        resetEnc();

    }

    //Controle movimentação mecanum
    public void Control(double x, double y, double yaw) {

        if (!odmActv) {
            odmE.setPosition(1);
            odmD.setPosition(1);
            odmActv = true;
        }

        yaw *= s * sY;
        x   *= s;
        y   *= s;

        yaw = Math.round(yaw/precs) * precs;
        x   = Math.round(x/precs) * precs;
        y   = Math.round(y/precs) * precs;


        if (Math.abs(x) < 0.06 && Math.abs(y) < 0.06 && Math.abs(yaw) < 0.1) {
            accl = 0;
            moveT.reset();
        } else {
            accl = moveT.time();
        }

        telemetry.addData("moveT", accl);

        accl = Math.min(accl, acclT) / acclT;
        accl = Math.round(accl * 1000.0) / 1000.0;

//*/
        FE.setPower((y + x + yaw) * accl);
        FD.setPower((y - x - yaw) * accl);
        TE.setPower((y - x + yaw) * accl);
        TD.setPower((y + x - yaw) * accl);


    }

    public boolean move(double maxVel, double acelTime, double p, double sp) {

        if (!moveIsBusy) {
            resetEnc();
            setPoint = sp;
            moveIsBusy = true;
        }
        pos = (encE.getCurrentPosition() + encE.getCurrentPosition()) / 2.0;

        while (pos < setPoint + 10 || pos > setPoint - 10) {

            giro = (encE.getCurrentPosition() - encD.getCurrentPosition()) / p * 100;
            pos = pos / p * 100;
            setPower(pos, giro);

        }



        setPower(0);


        return moveIsBusy;
    }

    public void resetEnc(){
        encE.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encE.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        encD.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encD.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setPower(double p){
        FE.setPower(p);
        FD.setPower(p);
        TE.setPower(p);
        TD.setPower(p);
    }

    public void setPower(double p, double g){
        FE.setPower(p + g);
        FD.setPower(p - g);
        TE.setPower(p + g);
        TD.setPower(p - g);
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
