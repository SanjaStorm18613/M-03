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
                                    ACCELERATION = 3000, //Tempo para atingir setpoint de velocidade
                                    PRECISION = 0.25;
    }

    public static class Braco {

        public static final double  MIN_POS = .36,
                                    MAX_POS = .77;

    }

    public static class Elevador {

        public static final double  NV_1 = 1,
                                    NV_2 = 2,
                                    NV_3 = 2.94,
                                    CONVR = 4400,
                                    UP_SPEED = .9,
                                    DOWN_SPEED = .7;

    }

    public static class Garra {

        public static final double  CLAW_CLOSE = 0.2,
                                    CLAW_OPEN = 0.7,
                                    PITCH_DROP = 0.0,
                                    HORIZONTAL = 0.4,
                                    PITCH_FALLEN = 0.15,
                                    BRACO_UP = 0.4,
                                    ROLL_UP = 0.2,
                                    ROLL_DOWN = 0.55,
                                    ROLL_SIDE_CONE = (ROLL_DOWN-ROLL_UP)/2;

    }

    public static class Yaw {

        public static final int     LIMIT = 2000,
                                    CONVR = 400;
        public static final double  AJUST = 0.06,
                                    CONTROL = .3,
                                    BRACO_UP = Braco.MIN_POS + 0.2;

    }
}
