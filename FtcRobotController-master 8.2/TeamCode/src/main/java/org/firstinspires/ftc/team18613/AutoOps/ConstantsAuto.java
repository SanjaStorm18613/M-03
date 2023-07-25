package org.firstinspires.ftc.team18613.AutoOps;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Vision.PipelineColors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ConstantsAuto {

    public final Double DR = 0.0, DR_TURN = 1.0, EL = 2.0, CL = 3.0, PT = 4.0, YW = 5.0, YW_DT = 6.0,
                        DR_FRONT = 0.1, DR_SIDE = 0.2;


    public ConstantsAuto(){}

    //------------------------HEIGHT
    public ArrayList<ArrayList<Double[]>> getAutoHeight(boolean leftInitialPos,
                                                    PipelineColors.DetectionColor colorParkArea){

        double parkArea = 65.0, yawTurn = 0.41, drStep1 = 140.0;

        if (leftInitialPos) {
            yawTurn = -0.42;
            drStep1 = 132.0;

        }

        if (colorParkArea == PipelineColors.DetectionColor.GREEN) parkArea = 0;
        else if (colorParkArea == PipelineColors.DetectionColor.BLACK) parkArea *= -1;

        return new ArrayList<>(Arrays.asList(

                new ArrayList<>(Arrays.asList(new Double[] {0.0, CL}
                                                ,new Double[] {Constants.Elevator.NV_1, EL}
                                                ))

                ,new ArrayList<>(Arrays.asList(new Double[] {Constants.Claw.PITCH_UP, PT, 500.0}
                                                ,new Double[] {drStep1, DR, DR_FRONT}
                                                ,new Double[] {Constants.Elevator.NV_3, EL}
                                                ))

                ,new ArrayList<>(Collections.singletonList(new Double[] {yawTurn, YW}))
                //------------------------HEIGHT

                /*,new ArrayList<>(Collections.singletonList(new Double[] {Constants.Claw.PITCH_UP
                                                                                + 0.3, PT, 2000.0}))

                ,new ArrayList<>(Arrays.asList(new Double[] {1.0, CL}
                                                ,new Double[] {Constants.Claw.PITCH_UP, PT, 1000.0}
                                                ))*/

                ,new ArrayList<>(Arrays.asList(new Double[]{4000.0, YW_DT}
                                                ,new Double[] {Constants.Claw.PITCH_UP + 0.3, PT, 4000.0}))

                ,new ArrayList<>(Arrays.asList(new Double[] {1.0, CL}
                                                ,new Double[] {Constants.Claw.PITCH_UP, PT, 1000.0}
                ))

                ,new ArrayList<>(Arrays.asList(new Double[] {0.0, YW}
                                                ,new Double[] {Constants.Claw.PITCH_LOWERED, PT, 1000.0}
                                                ,new Double[] {Constants.Claw.ELEVATOR_UP_RETRAIN, EL}
                                                ,new Double[] {65. - drStep1, DR, DR_FRONT}
                                                ))

                ,new ArrayList<>(Collections.singletonList(new Double[] {parkArea, DR, DR_SIDE}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {Constants.Claw.PITCH_HORIZONTAL, PT, parkArea == 0. ? 0. : 3000.0}))

                ,new ArrayList<>(Arrays.asList(new Double[] {Constants.Elevator.NV_0, EL}
                                                ,new Double[] {0.0, CL}))
                //------------------------HEIGHT

        ));
    }

    //------------------------MID
    public ArrayList<ArrayList<Double[]>> getAutoMid(boolean leftInitialPos,
                                                    PipelineColors.DetectionColor colorParkArea){

        double parkArea = 65.0, yawTurn = 0.40, drStep1 = 63.0;

        if (leftInitialPos) {
            yawTurn = -0.40;
            drStep1 = 64.0;

        }

        if (colorParkArea == PipelineColors.DetectionColor.GREEN) parkArea = 0;
        else if (colorParkArea == PipelineColors.DetectionColor.BLACK) parkArea *= -1;

        return new ArrayList<>(Arrays.asList(

                new ArrayList<>(Arrays.asList(new Double[] {0.0, CL}
                                                ,new Double[] {Constants.Elevator.NV_2, EL}
                                                ))

                ,new ArrayList<>(Arrays.asList(new Double[] {Constants.Claw.PITCH_UP, PT, 500.0}
                                                ,new Double[] {drStep1, DR, DR_FRONT}
                                                ))

                ,new ArrayList<>(Collections.singletonList(new Double[] {yawTurn, YW}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {Constants.Claw.PITCH_UP + 0.3, PT, 2000.0}))
                //------------------------MID

                ,new ArrayList<>(Arrays.asList(new Double[] {1.0, CL}
                                                ,new Double[] {Constants.Claw.PITCH_UP, PT, 1000.0}
                                                ))

                ,new ArrayList<>(Arrays.asList(new Double[] {0.0, YW}
                                                ,new Double[] {Constants.Claw.PITCH_LOWERED, PT, 1000.0}
                                                ,new Double[] {Constants.Claw.ELEVATOR_UP_RETRAIN, EL}
                                                //,new Double[] {5.0, DR, DR_FRONT}
                                                ))

                ,new ArrayList<>(Collections.singletonList(new Double[] {parkArea, DR, DR_SIDE}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {Constants.Claw.PITCH_HORIZONTAL, PT, parkArea == 0. ? 0. : 3000.0}))

                ,new ArrayList<>(Arrays.asList(new Double[] {Constants.Elevator.NV_0, EL}
                                                ,new Double[] {0.0, CL}))
                //------------------------MID

        ));
    }

    //------------------------PARK ONLY
    public ArrayList<ArrayList<Double[]>> getAutoParkOnly(
                                                    PipelineColors.DetectionColor colorParkArea){

        double parkArea = 60.0;

        if (colorParkArea == PipelineColors.DetectionColor.GREEN) parkArea = 0;
        else if (colorParkArea == PipelineColors.DetectionColor.BLACK) parkArea *= -1;

        return new ArrayList<>(Arrays.asList(

                /*new ArrayList<>(Collections.singletonList(new Double[] {0.0, CL}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {0.0, CL}))*/


                new ArrayList<>(Arrays.asList(new Double[] {0.0, CL}
                        ,new Double[] {Constants.Elevator.NV_1, EL}
                ))

                ,new ArrayList<>(Arrays.asList(new Double[] {Constants.Claw.PITCH_LOWERED, PT,
                                                                                            500.0}
                        ,new Double[] {65.0, DR, DR_FRONT}
                        ,new Double[] {Constants.Claw.ELEVATOR_UP_RETRAIN, EL}
                ))

                ,new ArrayList<>(Collections.singletonList(new Double[] {parkArea, DR, DR_SIDE}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {
                                                    Constants.Claw.PITCH_HORIZONTAL, PT, 1000.0}))
                //------------------------PARK ONLY

                ,new ArrayList<>(Collections.singletonList(new Double[] {Constants.Elevator.NV_0,
                                                                                            EL}))

        ));
    }

    public ArrayList<ArrayList<Double[]>> getAutoDemonstrator(){

        return new ArrayList<>(Arrays.asList(

                new ArrayList<>(Collections.singletonList(new Double[] {0.0, CL}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {40.0, DR, DR_FRONT}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {40.0, DR, DR_SIDE}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {-80.0, DR, DR_SIDE}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {40.0, DR, DR_SIDE}))

                ,new ArrayList<>(Arrays.asList(new Double[] {360.0, DR_TURN}, new Double[]{
                                                                    Constants.Elevator.NV_2, EL}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {0.5, YW}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {-0.5, YW}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {0.0, YW}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {
                                                    Constants.Claw.PITCH_HORIZONTAL, PT, 2000.0}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {1.0, CL}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {
                                                                Constants.Claw.PITCH_UP, PT,500.0}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {0.0, CL}))

                ,new ArrayList<>(Arrays.asList(new Double[] {Constants.Elevator.NV_0, EL},
                                                                new Double[] {-40.0, DR, DR_FRONT}))
        ));
    }

}

