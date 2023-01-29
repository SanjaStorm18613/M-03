package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;
import org.firstinspires.ftc.team18613.TeleOpM03;

public class Garra extends Subsystem {

    private final Servo roll, clawD, clawE, pitch;
    private final Elevador elev;
    private final Braco braco;
    private final ElapsedTime time;

    private double pTranstTime = Constants.Garra.TRANSITION_TIME;

    private double pVel = 0, elevCtrl = 0, pPos = Constants.Garra.PITCH_DROP, pTime = 0,
            pPrevPos = Constants.Garra.PITCH_DROP, rPos = Constants.Garra.ROLL_UP, cTargetPos = 0, cont = 0, ppp =0;

    private boolean cOpen = false;
    private boolean SPIN = true, COLVERT = true, COLFRONT = true, COLSIDE = true, RETAIN = true, DROP = true;
    private boolean elevNvCol = true, cIsBusy = false, init, retrain = false, elevAjt = false;

    int cStatus = 0, cPrevStatus = 0;

    public Garra(Elevador elev, Braco braco) {

        roll  = TeleOpM03.hm.get(Servo.class, "Roll");
        clawD = TeleOpM03.hm.get(Servo.class, "GarraD");
        clawE = TeleOpM03.hm.get(Servo.class, "GarraE");
        pitch = TeleOpM03.hm.get(Servo.class, "Pitch");

        clawD.setDirection(Servo.Direction.FORWARD);
        clawE.setDirection(Servo.Direction.REVERSE);

        roll.setDirection(Servo.Direction.FORWARD);
        pitch.setDirection(Servo.Direction.FORWARD);

        this.elev = elev;
        this.braco = braco;

        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        init = true;

    }

    public void Control(
            boolean spin,
            boolean colVert,
            boolean colFront,
            boolean colSide,
            boolean retain,
            boolean drop,
            double angle) {

        elevNvCol = elev.getNiveis() == 0;

        if (elev.getNiveis() > 0 && elev.getCurrentPos() > 1000) {
            pPos = Constants.Garra.PITCH_DROP;
            rPos = rPos == Constants.Garra.ROLL_SIDE_CONE ? Constants.Garra.ROLL_UP : rPos;
        }

        //if (drop && DROP) cOpen = !cOpen;
        //copiloto
        if (retain && RETAIN && elevNvCol) {
            cOpen = false;
            if (Math.abs(elev.getTargetPosition()) > 0 && pPos == Constants.Garra.HORIZONTAL) pPos = Constants.Garra.PITCH_DROP;
            else pPos = Constants.Garra.PITCH_FALLEN;
            rPos = rPos == Constants.Garra.ROLL_SIDE_CONE ? Constants.Garra.ROLL_UP : rPos;
            retrain = true;

        }

        //if (drop && DROP) cOpen = !cOpen;

        if (spin && SPIN) {
            rPos = rPos != Constants.Garra.ROLL_UP ? Constants.Garra.ROLL_UP : Constants.Garra.ROLL_DOWN;
            cOpen = false;
        }

        // piloto
        if ((colVert || colFront || colSide) && elevNvCol) {
            retrain = false;
            elevAjt = false;

            if (colVert && COLVERT) {
                rPos = rPos == Constants.Garra.ROLL_SIDE_CONE ? Constants.Garra.ROLL_UP : rPos;
                cOpen = !cOpen;
                cPrevStatus = cStatus;
                cStatus = 1;
                pPos = Constants.Garra.HORIZONTAL;

            } else if (colFront && COLFRONT) {
                rPos = rPos == Constants.Garra.ROLL_SIDE_CONE ? Constants.Garra.ROLL_UP : rPos;
                cOpen = !cOpen;
                cPrevStatus = cStatus;
                cStatus = 2;
                pPos = Constants.Garra.PITCH_FALLEN;

            } else if (colSide && COLSIDE) {
                rPos = Constants.Garra.ROLL_SIDE_CONE;
                cOpen = !cOpen;
                cPrevStatus = cStatus;
                cStatus = 3;
                pPos = Constants.Garra.PITCH_FALLEN;

            }
        }

        cOpen = cOpen || cStatus != cPrevStatus;

        COLSIDE = !colSide;
        COLFRONT = !colFront;
        COLVERT = !colVert;
        SPIN = !spin;
        RETAIN = !retain;
        DROP = !drop;

        if (pPrevPos != pPos) {
            init = false;
            if (pPos == Constants.Garra.PITCH_FALLEN || pPrevPos == Constants.Garra.PITCH_FALLEN) {
                time.reset();
                cont = pPrevPos;
            }
        }
        pPrevPos = pPos;


        if (Math.abs(pPos-pPrevPos) == Math.abs(Constants.Garra.PITCH_DROP - Constants.Garra.PITCH_FALLEN)) pTranstTime = Constants.Garra.TRANSITION_TIME;
        else pTranstTime = Constants.Garra.TRANSITION_TIME / 1.5;
        pTime = Math.min(time.time(), pTranstTime);

        if (elevNvCol && !init) pVel = Math.round(Math.min(pTime / pTranstTime, 1) * 1000) / 1000.0;
        else pVel = 1;

        pVel = Math.pow(pVel, 1.8);
        TeleOpM03.tel.addData("pVel", pVel);


        double pMid = pTranstTime / 2.0;
        elevCtrl = -Math.abs(pTime - pMid) / pMid + 1;
        elevCtrl = Math.max(elevCtrl, 0);

        TeleOpM03.tel.addData("retrain", retrain);

        if (retrain) {
            claw(cOpen);
            braco.setAjt(-1);
            elevAjt = true;

        } else if (pPos == Constants.Garra.PITCH_FALLEN) {

            if (cOpen) {
                elevCtrl = 1;
                claw(true);

            } else if (elev.getCurrentPos() < 80) {
                claw(false);
                elevAjt = true;
                braco.setAjt(-1);

            } else if (elev.getCurrentPos() < 700 && !elevAjt) braco.setAjt(.18);

        } else {
            claw(cOpen);
            braco.setAjt(-1);
            elevAjt = false;
        }

        if (!init) {
            if (elevCtrl > 0) {
                elev.setAjt(elevNvCol, Math.max(Constants.Garra.ELEVADOR_UP, ppp));
            } else {
                elev.setAjt(elevNvCol, Math.max(elevAjt ? .25 : 0, ppp));
            }
        }

        if (pVel == 1) cont = pPrevPos;

        double calc = .8 * Math.pow(angle, 2.5) * (Constants.Garra.HORIZONTAL - Constants.Garra.PITCH_DROP);

        if (init) pitch.setPosition(Constants.Garra.PITCH_DROP + braco.getPos() + (pPos == Constants.Garra.PITCH_FALLEN ? 0 : calc));
        else pitch.setPosition(cont + (pPos - cont) * pVel + braco.getPos() + (pPos == Constants.Garra.PITCH_FALLEN  ? 0 : calc));

        if (pVel == 1 || !elevNvCol || init || pitch.getPosition() > .4 && pitch.getPosition() < .6) roll.setPosition(rPos);

        //telemetry.addData("pitch", pitch.getPosition());
        //telemetry.addData("roll", roll.getPosition());

        TeleOpM03.tel.addData("p1", cont + (pPos - cont) * pVel);
        TeleOpM03.tel.addData("p2", (cont + (pPos - cont) * pVel) + braco.getPos());

        TeleOpM03.tel.addData("pPos", pPos);
        TeleOpM03.tel.addData("getTargetPosition", elev.getTargetPosition());

    }

    public void setClaw(double c, int t) {

        claw(c == 1.0);

        roll.setPosition(Constants.Garra.ROLL_UP);
        pitch.setPosition(Constants.Garra.PITCH_DROP + 0.1);

        if (cTargetPos != clawD.getPosition() && !cIsBusy) {
            time.reset();
            cIsBusy = true;
        }
        cTargetPos = clawD.getPosition();

        cIsBusy = time.time() < t;

    }

    public boolean getBusy(){
        return cIsBusy;
    }

    public void claw(boolean p) {
        clawD.setPosition(p ? Constants.Garra.CLAW_OPEN : Constants.Garra.CLAW_CLOSE);
        clawE.setPosition(p ? Constants.Garra.CLAW_OPEN : Constants.Garra.CLAW_CLOSE);

    }

    public void openClaw(){
        clawD.setPosition(Constants.Garra.CLAW_OPEN);
        clawD.setPosition(Constants.Garra.CLAW_OPEN);
    }

    public void closeClaw(){
        clawD.setPosition(Constants.Garra.CLAW_CLOSE);
        clawD.setPosition(Constants.Garra.CLAW_CLOSE);
    }

    public void setElev (double p) {
        ppp = p;
    }

}






