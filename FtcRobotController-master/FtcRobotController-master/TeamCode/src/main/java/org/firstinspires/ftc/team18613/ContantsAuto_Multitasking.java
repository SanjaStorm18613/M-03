package org.firstinspires.ftc.team18613;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ContantsAuto_Multitasking {

    public final Double DR = 0.0, DR_TURN = 1.0, EL = 2.0, CL = 3.0, PT = 4.0, YW = 5.0,
                        DR_FRONT = 0.0, DR_SIDE = 0.1;


    public ContantsAuto_Multitasking(){}

    public ArrayList<ArrayList<Double[]>> getAutoDemostracao(){

        return new ArrayList<>(Arrays.asList(

                new ArrayList<>(Collections.singletonList(new Double[] {20.0, DR, DR_FRONT}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {-20.0, DR, DR_FRONT}))

/*
                ,new ArrayList<>(Collections.singletonList(new Double[] {40.0, DR, DR_FRONT}))
                new ArrayList<>(Collections.singletonList(new Double[] {0.0, CL}))


                ,new ArrayList<>(Collections.singletonList(new Double[] {40.0, DR, DR_FRONT}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {40.0, DR, DR_SIDE}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {-80.0, DR, DR_SIDE}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {40.0, DR, DR_SIDE}))

                //,new ArrayList<>(Collections.singletonList(new Double[] { Constants.Elevator.NV_2, EL}))
                ,new ArrayList<>(Arrays.asList(new Double[] {360.0, DR_TURN}, new Double[]{Constants.Elevator.NV_2, EL}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {0.5, YW}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {-0.5, YW}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {0.0, YW}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {Constants.Claw.PITCH_HORIZONTAL, PT, 2000.0}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {1.0, CL}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {Constants.Claw.PITCH_UP, PT,500.0}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {0.0, CL}))

                ,new ArrayList<>(Arrays.asList(new Double[] {Constants.Elevator.NV_0, EL}, new Double[] {-40.0, DR, DR_FRONT}))*/
        ));
    }

}

