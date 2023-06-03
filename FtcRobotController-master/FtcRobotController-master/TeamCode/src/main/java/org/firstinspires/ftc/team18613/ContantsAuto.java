package org.firstinspires.ftc.team18613;

import org.firstinspires.ftc.team18613.utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class ContantsAuto {

    private final int PT_TIME =-1, DR = 0, EL = 1, CL = 2, PT = 3, YW = 4, DR_EL = 5,
            DR_EL_CL_YW_DR = 6, EL_YW = 7;

    private Double sideAjust = 0.0;

    public ContantsAuto(){}

    public ArrayList<Pair<Double, Integer>> getHeightSteps(Double parkArea, Double side){

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
    }

    public ArrayList<Pair<Double, Integer>> getApresentationSteps(){
        return new ArrayList<>(Arrays.asList(
                //0-dr, 1-el, 2-cl, 3-yw, 4-drEl, 5-pt, 6-ElPtDr

                new Pair<>(0., CL)
                ,new Pair<>(Constants.Claw.PITCH_UP, PT)
                ,new Pair<>(500.0, PT_TIME)

                ,new Pair<>(40., DR)
                ,new Pair<>(40., DR)
                ,new Pair<>(-80., DR)
                ,new Pair<>(40., DR)

                ,new Pair<>(Constants.Elevator.NV_2, EL)

                ,new Pair<>(0.5, YW)
                ,new Pair<>(-0.5, YW)
                ,new Pair<>(0.0, YW)

                ,new Pair<>(Constants.Claw.PITCH_HORIZONTAL, PT)
                ,new Pair<>(2000.0, PT_TIME)
                ,new Pair<>(1., CL)
                ,new Pair<>(Constants.Claw.PITCH_UP, PT)
                ,new Pair<>(500.0, PT_TIME)
                ,new Pair<>(0., CL)

                ,new Pair<>(-40., DR_EL)
                ,new Pair<>(0., EL)

        ));
    }

    public ArrayList<Boolean> getParkOnlySide() {
        return new ArrayList<>(Arrays.asList(
                false
                ,true
        ));
    }

    public ArrayList<Boolean> getHightDRSide() {
        return new ArrayList<>(Arrays.asList(
                false
                ,false
                ,true
        ));
    }

    public ArrayList<Boolean> getMidDRSide() {
        return new ArrayList<>(Arrays.asList(
                false
                ,true
        ));
    }

    public ArrayList<Boolean> getApresentationSide() {
        return new ArrayList<>(Arrays.asList(
                false
                ,true
                ,true
                ,true
                ,false

        ));
    }
}
