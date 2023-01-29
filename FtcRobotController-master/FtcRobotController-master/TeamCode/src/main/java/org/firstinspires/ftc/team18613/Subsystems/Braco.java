package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;
import org.firstinspires.ftc.team18613.TeleOpM03;

public class Braco extends Subsystem {

    private final Servo E, D;
    private final Elevador elev;

    private double ajt = -1;
    private double calPos = 0;

    public Braco(Elevador elev) {

        //Servos direito e esquerdo do braço
        E = TeleOpM03.hm.get(Servo.class, "E");
        D = TeleOpM03.hm.get(Servo.class, "D");

        D.setDirection(Servo.Direction.FORWARD);
        E.setDirection(Servo.Direction.REVERSE);

        this.elev = elev;
    }

    public void control() {
        double posRangeBraco = Constants.Braco.MAX_POS - Constants.Braco.MIN_POS;
        double posPercentElev = (elev.getCurrentPos() / ((Constants.Elevador.NV_3/0.9) * Constants.Elevador.CONVR));
        double pos = (posPercentElev * posRangeBraco) + Constants.Braco.MIN_POS;

        pos = Math.min(pos, Constants.Braco.MAX_POS);
        pos = Math.max(pos, Constants.Braco.MIN_POS);

        pos = Math.round(pos * 1000.0) / 1000.0;

        pos = ajt == -1 ? pos : ajt;

        calPos = pos;

        E.setPosition(pos);
        D.setPosition(pos);

        TeleOpM03.tel.addData("braco", E.getPosition());
    }

    public double getPos(){
        return calPos - Constants.Braco.MIN_POS;
    }

    public void setAjt(double p) {
        ajt = p;
    }

}