package org.firstinspires.ftc.team18613;

import org.firstinspires.ftc.team18613.utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {

    public static class Pipeline {

        public static final double[][] COLOR_LOW = {{50, 50, 30},  {90, 70, 70}},//HLS
                                    COLOR_UP =  {{80, 180, 360}, {100, 180, 360}};//teste antes
        public static final int TOLERANCE_AREA = 1000;

    }

    public static class DTMecanum {

        //Tele
        public static final double  SPEED = .7,
                                    YAW_SPEED = .7,
                                    ACCELERATION = 1500, //Tempo para atingir setpoint de velocidade
                                    PRECISION = 0.25,
                                    TOLERANCE_DISTANCE = 500,
                                    TOLERANCE_ANGLE = 5,
                                    TOLERANCE_ENCODER_DIFERENCE = 100,
                                    CONVERTION = 725;
    }

    public static class Braco {

        public static final double  MIN_POS = .2,
                                    MAX_POS = .72;

    }

    public static class Elevator {

        public static final double  NV_0 = 0,
                                    NV_1 = 1.5,
                                    NV_2 = 3.4,
                                    NV_3 = 4,

                                    UP_SPEED = 1,
                                    DOWN_SPEED = .9,
                                    AJUSTE = 0.25;
        public static final int TOLERANCE = 80,
                                CONVR = 1830;

    }

    public static class Claw {

        public static final double  CLAW_CLOSE = 0.19,
                                    CLAW_OPEN = 0.45,
                                    PITCH_UP = 0.05,
                                    PITCH_HORIZONTAL = .52,
                                    PITCH_LOWERED = .87,
                                    ELEVADOR_UP = 1.2,
                                    ELEVATOR_UP_RETRAIN = .7,
                                    ROLL_UP = 0.12,
                                    ROLL_DOWN = 0.65,
                                    ROLL_SIDE_CONE = ROLL_UP + Math.abs(ROLL_DOWN-ROLL_UP)/2,
                                    TRANSITION_TIME = 1000;

    }

    public static class Turret {

        public static final int     LIMIT = 1760,
                                    CONVR = 400,
                                    COUNTS_PER_REVOLUTION = 1600;
        public static final double  SPEED = 1,
                                    CHASSIS_OPENING = .15;
    }
}
