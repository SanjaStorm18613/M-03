package org.firstinspires.ftc.team18613;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ContantsAuto_MultTasks {

    public final Double DR = 0.0, DR_TURN = 1.0, EL = 2.0, CL = 3.0, PT = 4.0, YW = 5.0,
                        DR_FRONT = 0.0, DR_SIDE = 0.1;


    public ContantsAuto_MultTasks(){}

    public ArrayList<ArrayList<Double[]>> getMultTasks(){

        return new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(new Double[] {30.0, DR, DR_FRONT}, new Double[] {1.0, CL}))
                ,new ArrayList<>(Collections.singletonList(new Double[]{0.0, CL}))
                ,new ArrayList<>(Arrays.asList(new Double[] {-30.0, DR, DR_FRONT}, new Double[] {1.0, CL}))

        ));
    }

}

