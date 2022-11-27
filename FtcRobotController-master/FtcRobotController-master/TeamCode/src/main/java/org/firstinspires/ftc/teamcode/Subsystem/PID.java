package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PID {

    Constantis.DTMecanum Const;

    ElapsedTime pidT;
    double erro, deltaEr, d, i = 0, antErro = 0;


    public PID (){


    }

    public double PIDCorrect(double status, double sp) {

        double erro    = sp - status;
        double deltaEr = erro - antErro;
        double d       = deltaEr / pidT.time();
        i       += erro * pidT.time();

        double P = erro * Const.P;
        double I = i    * Const.I;
        double D = d    * Const.D;

        antErro = erro;

        return P + I + D;

    }

    public double sg(double var){

        return var >= 0 ? 1 : -1;

    }

}
