package org.firstinspires.ftc.teamcode.Subsystem;

public class Constantis {

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
                                    COVERTION = 50;
    }

    public static class Braco {

        public static final double  MIN_POS = .21,
                                    MAX_POS = .6;

    }

    public static class Elevador {

        public static final double  NV_1 = 1.5,
                                    NV_2 = 3.2,
                                    NV_3 = 4.8,
                                    CONVR = 1500,
                                    UP_SPEED = .9,
                                    DOWN_SPEED = .7,
                                    AJUSTE = 0.25;

        public static final int TOLERANCE = 100;

    }

    public static class Garra {

        public static final double  CLAW_CLOSE = 0.2,
                                    CLAW_OPEN = 0.45,
                                    PITCH_DROP = 0.1,
                                    HORIZONTAL = 0.56,
                                    PITCH_FALLEN = 0.95,
                                    ELEVADOR_UP = 1,
                                    ROLL_UP = 0.2,
                                    ROLL_DOWN = 0.6,
                                    ROLL_SIDE_CONE = Math.abs(ROLL_DOWN-ROLL_UP)/2,
                                    TRANSITION_TIME = 1500;

    }

    public static class Yaw {

        public static final int     LIMIT = 5000,
                                    CONVR = 400;
        public static final double  AJUST = 0.06,
                                    CONTROL = .3,
                                    BRACO_UP = Braco.MIN_POS + 0.2;

    }
}
