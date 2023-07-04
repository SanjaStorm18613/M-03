package org.firstinspires.ftc.team18613.AutoOps;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Vision.PipelineColors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ContantsAuto {

    public final Double DR = 0.0, DR_TURN = 1.0, EL = 2.0, CL = 3.0, PT = 4.0, YW = 5.0,
                        DR_FRONT = 0.0, DR_SIDE = 0.1;


    public ContantsAuto(){}

    public ArrayList<ArrayList<Double[]>> getAutoHeigth(boolean leftInitialPos, PipelineColors.DetectionColor colorParkArea){

        double parkArea = 55.0, yawTurn = 0.43;

        if (leftInitialPos) yawTurn = -0.45;

        if (colorParkArea == PipelineColors.DetectionColor.YELLOW) parkArea = 0;
        else if (colorParkArea == PipelineColors.DetectionColor.GREEN) parkArea *= -1;

        return new ArrayList<>(Arrays.asList(

                new ArrayList<>(Arrays.asList(new Double[] {0.0, CL}
                                                ,new Double[] {Constants.Elevator.NV_1, EL}
                                                ))

                ,new ArrayList<>(Arrays.asList(new Double[] {Constants.Claw.PITCH_UP, PT, 500.0}
                                                ,new Double[] {Constants.Elevator.NV_3, EL}
                                                ,new Double[] {130.0, DR, DR_FRONT}
                                                ))

                ,new ArrayList<>(Collections.singletonList(new Double[] {yawTurn, YW}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {Constants.Claw.PITCH_UP + 0.12, PT, 2000.0}))

                ,new ArrayList<>(Arrays.asList(new Double[] {1.0, CL}
                                                ,new Double[] {Constants.Claw.PITCH_UP, PT, 1000.0}
                                                ))

                ,new ArrayList<>(Arrays.asList(new Double[] {0.0, YW}
                                                ,new Double[] {Constants.Claw.PITCH_LOWERED, PT, 1000.0}
                                                ,new Double[] {Constants.Claw.ELEVATOR_UP_RETRAIN, EL}
                                                ,new Double[] {-65., DR, DR_FRONT}
                                                ))

                ,new ArrayList<>(Collections.singletonList(new Double[] {parkArea, DR, DR_SIDE}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {Constants.Claw.PITCH_HORIZONTAL, PT, 1000.0}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {Constants.Elevator.NV_0, EL}))

        ));
    }

    public ArrayList<ArrayList<Double[]>> getAutoMid(boolean leftInitialPos, PipelineColors.DetectionColor colorParkArea){

        double parkArea = 55.0, yawTurn = 0.43;

        if (leftInitialPos) yawTurn = -0.45;

        if (colorParkArea == PipelineColors.DetectionColor.YELLOW) parkArea = 0;
        else if (colorParkArea == PipelineColors.DetectionColor.GREEN) parkArea *= -1;

        return new ArrayList<>(Arrays.asList(

                new ArrayList<>(Arrays.asList(new Double[] {0.0, CL}
                                                ,new Double[] {Constants.Elevator.NV_2, EL}
                                                ))

                ,new ArrayList<>(Arrays.asList(new Double[] {Constants.Claw.PITCH_UP, PT, 500.0}
                                                ,new Double[] {75.0, DR, DR_FRONT}
                                                ))

                ,new ArrayList<>(Collections.singletonList(new Double[] {yawTurn, YW}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {Constants.Claw.PITCH_UP + 0.12, PT, 2000.0}))

                ,new ArrayList<>(Arrays.asList(new Double[] {1.0, CL}
                                                ,new Double[] {Constants.Claw.PITCH_UP, PT, 1000.0}
                                                ))

                ,new ArrayList<>(Arrays.asList(new Double[] {0.0, YW}
                                                ,new Double[] {Constants.Claw.PITCH_LOWERED, PT, 1000.0}
                                                ,new Double[] {Constants.Claw.ELEVATOR_UP_RETRAIN, EL}
                                                ,new Double[] {-10., DR, DR_FRONT}
                                                ))

                ,new ArrayList<>(Collections.singletonList(new Double[] {parkArea, DR, DR_SIDE}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {Constants.Claw.PITCH_HORIZONTAL, PT, 1000.0}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {Constants.Elevator.NV_0, EL}))

        ));
    }

    public ArrayList<ArrayList<Double[]>> getAutoParkOnly(PipelineColors.DetectionColor colorParkArea){

        double parkArea = 55.0;

        if (colorParkArea == PipelineColors.DetectionColor.YELLOW) parkArea = 0;
        else if (colorParkArea == PipelineColors.DetectionColor.GREEN) parkArea *= -1;

        return new ArrayList<>(Arrays.asList(

                /*new ArrayList<>(Collections.singletonList(new Double[] {0.0, CL}))
                ,new ArrayList<>(Collections.singletonList(new Double[] {0.0, CL}))*/


                new ArrayList<>(Arrays.asList(new Double[] {0.0, CL}
                        ,new Double[] {Constants.Elevator.NV_1, EL}
                ))

                ,new ArrayList<>(Arrays.asList(new Double[] {Constants.Claw.PITCH_LOWERED, PT, 500.0}
                        ,new Double[] {65.0, DR, DR_FRONT}
                        ,new Double[] {Constants.Claw.ELEVATOR_UP_RETRAIN, EL}
                ))

                ,new ArrayList<>(Collections.singletonList(new Double[] {parkArea, DR, DR_SIDE}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {Constants.Claw.PITCH_HORIZONTAL, PT, 1000.0}))

                ,new ArrayList<>(Collections.singletonList(new Double[] {Constants.Elevator.NV_0, EL}))

        ));
    }

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

