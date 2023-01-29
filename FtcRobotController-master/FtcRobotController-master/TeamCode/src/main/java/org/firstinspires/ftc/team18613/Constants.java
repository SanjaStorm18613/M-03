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
                                    TOLERANCE_DISTANCE = 100,
                                    TOLERANCE_ANGLE = 100,
                                    CONVERTION = 50;
    }

    public static class Braco {

        public static final double  MIN_POS = .23,
                                    MAX_POS = .65;

    }

    public static class Elevador {

        public static final double  NV_0 = 0,
                                    NV_1 = 1.5,
                                    NV_2 = 3.2,
                                    NV_3 = 4.62,
                                    CONVR = 1500,
                                    UP_SPEED = .9,
                                    DOWN_SPEED = .7,
                                    AJUSTE = 0.25;

        public static final int TOLERANCE = 100;

    }

    public static class Garra {

        public static final double  CLAW_CLOSE = 0.2,
                                    CLAW_OPEN = 0.45,
                                    PITCH_DROP = 0.05,
                                    HORIZONTAL = 0.53,
                                    PITCH_FALLEN = 0.9,
                                    ELEVADOR_UP = 1,
                                    ROLL_UP = 0.2,
                                    ROLL_DOWN = 0.6,
                                    ROLL_SIDE_CONE = ROLL_UP + Math.abs(ROLL_DOWN-ROLL_UP)/2,
                                    TRANSITION_TIME = 2000;

    }

    public static class Yaw {

        public static final int     LIMIT = 5000,
                                    CONVR = 400,
                                    LAP_COUNTS = 1000;
        public static final double  AJUST = 0.06,
                                    CONTROL = .3,
                                    BRACO_UP = Braco.MIN_POS + 0.2,
                                    SPEED = 1;
    }
}