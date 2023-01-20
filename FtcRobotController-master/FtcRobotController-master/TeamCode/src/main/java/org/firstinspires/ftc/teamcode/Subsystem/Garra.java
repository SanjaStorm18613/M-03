package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Garra {

    double pDropPos = Constantis.Garra.PITCH_DROP,
            pHorzPos = Constantis.Garra.HORIZONTAL,
            pElevPos = Constantis.Garra.ELEVADOR_UP,
            rUpPos = Constantis.Garra.ROLL_UP,
            pTranstTime = Constantis.Garra.TRANSITION_TIME;

    Servo roll, clawD, clawE, pitch;
    Elevador elev;
    Braco braco;
    ElapsedTime time;
    Telemetry telemetry;

    boolean cOpen = false, pUp = true, rUp = true, pColet = false, rColet = false, pDrop = false;
    boolean SPIN = true, COLVERT = true, COLFRONT = true, COLSIDE = true, RETAIN = true, DROP = true;
    boolean elevNvCol = true, pColetSts = false, init, cIsBusy = true;

    double cTargetPos = 0, pVel = 0, elevAjst = 0, pPos = 0, pTime = 0;

    int cStatus = 0, cPrevStatus = 0;


    public Garra(Telemetry t, HardwareMap hardwareMap, Elevador e, Braco b) {

        roll  = hardwareMap.get(Servo.class, "Roll");
        clawD = hardwareMap.get(Servo.class, "GarraD");
        clawE = hardwareMap.get(Servo.class, "GarraE");
        pitch = hardwareMap.get(Servo.class, "Pitch");

        clawD.setDirection(Servo.Direction.FORWARD);
        clawE.setDirection(Servo.Direction.REVERSE);

        roll.setDirection(Servo.Direction.FORWARD);
        pitch.setDirection(Servo.Direction.FORWARD);

        elev = e;
        telemetry = t;
        braco = b;

        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        time.startTime();

        init = true;

    }

    public void Control(boolean spin, boolean colVert, boolean colFront, boolean colSide, boolean retain, boolean drop) {

        elevNvCol = elev.getNv() == 0;

        //#region ENTREGA E COLETA
        if (retain && RETAIN) {
            pUp = !pUp;
            cOpen = false;
            pColet = false;
            rColet = false;
            pDrop = !pDrop;

        }
        if (drop && DROP) cOpen = !cOpen;

        //#endregion

        //#region ENTREGA

        if (spin && SPIN) {
            pColet = false;
            rColet = false;
            rUp = !rUp;

        }

        //#endregion

        //#region COLETA

        if ((colVert || colFront || colSide) && elevNvCol) {
            pUp = false;
            pDrop = false;
            //rUp = true;


            if (colVert && COLVERT) {
                pColet = false;
                rColet = false;
                cOpen = !cOpen;
                cPrevStatus = cStatus;
                cStatus = 1;

            } else if (colFront && COLFRONT) {
                pColet = true;
                rColet = false;
                cOpen = !cOpen;
                cPrevStatus = cStatus;
                cStatus = 2;

            } else if (colSide && COLSIDE) {
                pColet = true;
                rColet = true;
                cOpen = !cOpen;
                cPrevStatus = cStatus;
                cStatus = 3;

            }
        }
        //#endregion

        COLSIDE = !colSide;
        COLFRONT = !colFront;
        COLVERT = !colVert;
        SPIN = !spin;
        RETAIN = !retain;
        DROP = !drop;

        // Controle de velocidade por tempo
        if (pColetSts == !pColet) {
            time.reset();
            init = false;
        }
        pColetSts = pColet;

        pTranstTime = Constantis.Garra.TRANSITION_TIME;
        if (pUp) pTranstTime += 500;

        pTime = Math.min(time.time(), pTranstTime);

        if (elevNvCol && !init) {
            pVel = Math.round(Math.min(pTime / pTranstTime, 1) * 1000)/1000.0;

            if (!pColet) pVel = 1 - pVel;

        } else pVel = 0;


        // Controle do elevador para coletar cone caido
        double pMid = pTranstTime / 2.0;
        elevAjst = -Math.abs(pTime - pMid) / pMid + 1;
        elevAjst = Math.max(elevAjst, 0);


        // Controle da garra para fechar em cima do cone caido
        cOpen = cOpen || cStatus != cPrevStatus;

        if (pColet) {
            if (elev.getCorrentPos() < 500) braco.setAjt(.18);
            else braco.setAjt(-1);

            if (cOpen) {
                elevAjst = pElevPos;
                claw(true);
            } else if (elev.getCorrentPos() < 80) claw(false);

        } else {
            claw(cOpen);
            braco.setAjt(-1);
        }

        // Aplica o controle do elevador
        if (!init) elev.setAjt(elevNvCol, elevAjst * pElevPos);


        // Controle do pitch
        pPos = (Constantis.Garra.PITCH_FALLEN - pHorzPos) * pVel; // Abaixa/levanta o pitch para coleta caido/horizontal pelo tempo
        if (pUp) pPos += pDropPos + pHorzPos * pVel; // Guarda a garra
        else if (pDrop) pPos += pHorzPos -.3; // Abaixa para coleta horizontal
        else pPos += pHorzPos; // Abaixa a garra para entrega
        pPos += braco.getPos(); // Controle proporcinal ao braço

        pitch.setPosition(pPos);

        // Controla roll apos movimento do pitch
        if (pTime >= pTranstTime || !elevNvCol || init) {
            if (rColet) roll.setPosition(Constantis.Garra.ROLL_SIDE_CONE);
            else roll.setPosition(rUp ? rUpPos : Constantis.Garra.ROLL_DOWN);

        }

/*
        telemetry.addData("elevAjst", elevAjst);
        telemetry.addData("cOpen", cOpen);
        telemetry.addData("pVel", pVel);
        telemetry.addData("claw", clawD.getPosition());
        telemetry.addData("roll", roll.getPosition());
        telemetry.addData("pitch", pitch.getPosition());
//*/
    }

    public void setClaw(double c, int t) {

        claw(c == 1.0);

        roll.setPosition(rUpPos);
        pitch.setPosition(pDropPos + 0.1);

        if (cTargetPos != clawD.getPosition() && !cIsBusy) {
            time.reset();
            cIsBusy = true;
        }
        cTargetPos = clawD.getPosition();

        cIsBusy = time.time() < t;

        telemetry.addData("cCorrentPos", cTargetPos);
        telemetry.addData("time", time.time());

    }

    public boolean getBusy(){
        return cIsBusy;
    }

    public void claw(boolean p) {
        clawD.setPosition(p ? Constantis.Garra.CLAW_OPEN : Constantis.Garra.CLAW_CLOSE);
        clawE.setPosition(p ? Constantis.Garra.CLAW_OPEN : Constantis.Garra.CLAW_CLOSE);

    }
}






