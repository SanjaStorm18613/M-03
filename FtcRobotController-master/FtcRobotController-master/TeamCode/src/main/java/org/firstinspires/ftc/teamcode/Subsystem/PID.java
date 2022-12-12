package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PID {

    Constantis.DTMecanum Const;

    ElapsedTime pidT;
    double erro, deltaEr, d, i = 0, antErro = 0, Pg, Ig, Dg;


    public PID (double p,double i, double d){

        Pg = p;
        Ig = i;
        Dg = d;

    }

    public double PIDresult(double status, double sp) {

        erro    = sp - status;
        deltaEr = erro - antErro;
        d       = deltaEr / pidT.time();
        i       += erro * pidT.time();

        double P = erro * Pg;
        double I = i    * Ig;
        double D = d    * Dg;

        antErro = erro;

        return P + I + D;

    }

    public double sg(double var){

        return var >= 0 ? 1 : -1;

    }

}
