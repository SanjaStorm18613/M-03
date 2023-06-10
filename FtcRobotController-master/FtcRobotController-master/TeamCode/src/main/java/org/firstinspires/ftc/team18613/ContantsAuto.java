package org.firstinspires.ftc.team18613;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ContantsAuto {

    public final Double DR = 0.0, DR_TURN = 1.0, EL = 2.0, CL = 3.0, PT = 4.0, YW = 5.0,
                        DR_FRONT = 0.0, DR_SIDE = 0.1;


    public ContantsAuto(){}

    public ArrayList<ArrayList<Double[]>> getAutoDemostracao(){

        return new ArrayList<>(Arrays.asList(

                new ArrayList<>(Collections.singletonList(new Double[] {0.0, CL}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {40.0, DR, DR_FRONT}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {40.0, DR, DR_SIDE}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {-80.0, DR, DR_SIDE}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {40.0, DR, DR_SIDE}))

                ,new ArrayList<>(Arrays.asList(new Double[] {360.0, DR_TURN}, new Double[]{Constants.Elevator.NV_2, EL}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {0.5, YW}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {-0.5, YW}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {0.0, YW}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {Constants.Claw.PITCH_HORIZONTAL, PT, 2000.0}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {1.0, CL}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {Constants.Claw.PITCH_UP, PT,500.0}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {0.0, CL}))

                ,new ArrayList<>(Arrays.asList(new Double[] {Constants.Elevator.NV_0, EL}, new Double[] {-40.0, DR, DR_FRONT}))
        ));
    }

}

/*  public ArrayList<Pair<Double, Integer>> getHeightSteps(Double parkArea, Double side){

        if (side < 0) {
            sideAjust = 0.45;
        } else {
            sideAjust = 0.6;
        }

        return new ArrayList<>(Arrays.asList(
                //0-dr, 1-el, 2-cl, 3-yw, 4-drEl, 5-pt, 6-ElPtDr
                new Pair<>(0., CL)
                ,new Pair<>(Constants.Elevator.NV_3 * .3, EL)
                ,new Pair<>(Constants.Claw.PITCH_UP, PT)
                ,new Pair<>(500.0, PT_TIME)

                ,new Pair<>(132., DR_EL)//{
                ,new Pair<>(Constants.Elevator.NV_3, EL)//}

                ,new Pair<>(sideAjust * side, YW)//afetado pelo lado
                ,new Pair<>(0.17, PT)
                ,new Pair<>(2000.0, PT_TIME)
                ,new Pair<>(1., CL)
                ,new Pair<>(Constants.Claw.PITCH_UP, PT)
                ,new Pair<>(1000.0, PT_TIME)

                ,new Pair<>(-65., DR_EL_CL_YW_DR)//{
                ,new Pair<>(Constants.Claw.ELEVADOR_UP, EL)
                ,new Pair<>(Constants.Claw.PITCH_LOWERED, PT)
                ,new Pair<>(1000.0, PT_TIME)
                ,new Pair<>(0., YW)
                ,new Pair<>(parkArea, DR)//}

                ,new Pair<>(Constants.Claw.PITCH_UP, PT)
                ,new Pair<>(1000., PT_TIME)
                ,new Pair<>(0., EL)
        ));
    }

    public ArrayList<Pair<Double, Integer>> getMidSteps(Double parkArea, Double side){

        if (side < 0) {
            sideAjust = 0.45;
        } else {
            sideAjust = 0.43;
        }

        return new ArrayList<>(Arrays.asList(
                //0-dr, 1-el, 2-cl, 3-yw, 4-drEl, 5-pt, 6-ElPtDr

                new Pair<>(0., CL)
                ,new Pair<>(Constants.Elevator.NV_2, EL)
                ,new Pair<>(Constants.Claw.PITCH_UP, PT)
                ,new Pair<>(500.0, PT_TIME)

                ,new Pair<>(64., DR)//{

                ,new Pair<>(sideAjust * side, YW)//afetado pelo lado
                ,new Pair<>(0.17, PT)
                ,new Pair<>(2000.0, PT_TIME)
                ,new Pair<>(1., CL)
                ,new Pair<>(Constants.Claw.PITCH_UP, PT)
                ,new Pair<>(1000.0, PT_TIME)

                //,new Pair<>(-15., dr)
                ,new Pair<>(Constants.Claw.ELEVADOR_UP, EL_YW)
                ,new Pair<>(0., YW)
                ,new Pair<>(Constants.Claw.PITCH_LOWERED, PT)
                ,new Pair<>(1000.0, PT_TIME)
                ,new Pair<>(parkArea, DR)

                ,new Pair<>(Constants.Claw.PITCH_UP, PT)
                ,new Pair<>(1000., PT_TIME)
                ,new Pair<>(0., EL)
        ));
    }

    public ArrayList<Pair<Double, Integer>> getParkOnlySteps(Double parkArea){

        return new ArrayList<>(Arrays.asList(
                //0-dr, 1-el, 2-cl, 3-yw, 4-drEl, 5-pt, 6-ElPtDr

                new Pair<>(0., CL)
                ,new Pair<>(Constants.Elevator.NV_1, EL)
                ,new Pair<>(Constants.Claw.PITCH_LOWERED, PT)
                ,new Pair<>(1000.0, PT_TIME)
                ,new Pair<>(Constants.Claw.ELEVATOR_UP_RETRAIN, EL)

                ,new Pair<>(65., DR)

                ,new Pair<>(parkArea, DR)

                ,new Pair<>(Constants.Claw.PITCH_UP, PT)
                ,new Pair<>(1000., PT_TIME)
                ,new Pair<>(0., EL)

        ));
    }*/

