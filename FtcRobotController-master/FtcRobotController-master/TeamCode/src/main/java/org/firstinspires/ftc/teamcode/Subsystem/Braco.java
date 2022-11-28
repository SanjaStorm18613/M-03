package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Braco {

    Servo E, D;
    Elevador elv;

    double ajs;
    double pos;//Posição do braço

    public Braco (HardwareMap hardwareMap, Elevador e){

//Servos direito e esquerdo do braço
        E = hardwareMap.get(Servo.class, "L");
        D = hardwareMap.get(Servo.class, "R");

        elv = e;

    }

    public void Control(){

        //elv.pos = Posição do elevador (de 0 a 1)
        if (elv.ElvPos() < 100) {

            pos = 0.1 + ajs;

//Limites do braço
            if (pos > 0.8) pos = 0.8;
            if (pos < 0.1) pos = 0.1;

            E.setPosition(pos);
            D.setPosition(1 - pos);//Servos giram inversos (E=0 -> D=1)

        } else {

            pos = 0.8 + ajs;

            if (pos > 0.8) pos = 0.8;
            if (pos < 0.1) pos = 0.1;

            E.setPosition(0.8 + ajs);
            D.setPosition(1 - 0.8 + ajs);

        }
    }

    public void Ajuste (boolean up, boolean dow){

//Controle manual do braço
        ajs += up ? 0.1 : dow ? -0.1 : 0;

    }
}