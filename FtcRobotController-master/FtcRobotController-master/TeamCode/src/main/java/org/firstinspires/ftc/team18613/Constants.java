package org.firstinspires.ftc.team18613;

public class Constants {

    public static class Pipeline {

        public static final double[][] COLOR_LOW = {{30, 70, 20},  {0, 70, 70}},//HLS
                                    COLOR_UP =  {{50, 130, 80}, {20, 130, 100}};//teste antes
        public static final int TOLERANCE_AREA = 50;

    }

    public static class DTMecanum {

        //Tele
        public static final double  SPEED = .7,
                                    YAW_SPEED = .7,
                                    ACCELERATION = 1500, //Tempo para atingir setpoint de velocidade
                                    PRECISION = 0.25,
                                    TOLERANCE_DISTANCE = 1000,
                                    TOLERANCE_ANGLE = 5,
                                    TOLERANCE_ENCODER_DIFERENCE = 100,
                                    CONVERTION = 725;
    }

    public static class Braco {

        public static final double  MIN_POS = .2,
                                    MAX_POS = .7;

    }

    public static class Elevador {

        public static final double  NV_0 = 0,
                                    NV_1 = 1.5,
                                    NV_2 = 3.6,
                                    NV_3 = 4.63,

                                    UP_SPEED = .9,
                                    DOWN_SPEED = .7,
                                    AJUSTE = 0.25;
        public static final int TOLERANCE = 80,
                                CONVR = 1500;

    }

    public static class Claw {

        public static final double  CLAW_CLOSE = 0.19,
                                    CLAW_OPEN = 0.45,
                                    PITCH_UP = 0.05,
                                    PITCH_HORIZONTAL = .50,
                                    PITCH_LOWERED = .85,
                                    ELEVADOR_UP = .9,
                                    ROLL_UP = 0.2,
                                    ROLL_DOWN = 0.55,
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
