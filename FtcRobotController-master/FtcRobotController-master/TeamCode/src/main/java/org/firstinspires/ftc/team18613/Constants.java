package org.firstinspires.ftc.team18613;

public class Constants {

    public static class Pipeline {

        public static final int[][] COLOR_LOW = {{40, 64, 100},  {25, 64, 127}},//HLS
                                    COLOR_UP =  {{90, 192, 255}, {35, 192, 255}};
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

        public static final double  MIN_POS = .23,
                                    MAX_POS = .69;

    }

    public static class Elevador {

        public static final double  NV_0 = 0,
                                    NV_1 = 1.5,
                                    NV_2 = 3.2,
                                    NV_3 = 4.82,

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
                                    PITCH_HORIZONTAL = 0.45,
                                    PITCH_LOWERED = 0.88,
                                    ELEVADOR_UP = .9,
                                    ROLL_UP = 0.2,
                                    ROLL_DOWN = 0.6,
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
