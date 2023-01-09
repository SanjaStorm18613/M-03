package org.firstinspires.ftc.teamcode.Subsystem;

public class Constantis {

    public static class DTMecanum {
        //Autonomo
        public static final double  P = 0,
                                    I = 0,
                                    D = 0;
        //Tele
        public static final double  SPEED = .7,
                                    YAW_SPEED = .7,
                                    ACCELERATION = 1500, //Tempo para atingir setpoint de velocidade
                                    PRECISION = 0.25;
    }

    public static class Braco {

        public static final double  MIN_POS = .23,
                                    MAX_POS = .65;

    }

    public static class Elevador {

        public static final double  NV_1 = 1.5,
                                    NV_2 = 3.2,
                                    NV_3 = 4.65,
                                    CONVR = 1500,
                                    UP_SPEED = .9,
                                    DOWN_SPEED = .7;

    }

    public static class Garra {

        public static final double  CLAW_CLOSE = 0.2,
                                    CLAW_OPEN = 0.7,
                                    PITCH_DROP = 0.0,
                                    HORIZONTAL = 0.47,
                                    PITCH_FALLEN = 0.9,
                                    ELEVADOR_UP = 1,
                                    ROLL_UP = 0.2,
                                    ROLL_DOWN = 0.6,
                                    ROLL_SIDE_CONE = Math.abs(ROLL_DOWN-ROLL_UP)/2;

    }

    public static class Yaw {

        public static final int     LIMIT = 2000,
                                    CONVR = 400;
        public static final double  AJUST = 0.06,
                                    CONTROL = .3,
                                    BRACO_UP = Braco.MIN_POS + 0.2;

    }
}
