package org.firstinspires.ftc.teamcode.Subsystem;

public class Constantis {

    public class DTMecanum {
        //Autonomo
        public static final double P = 0;
        public static final double I = 0;
        public static final double D = 0;
        //Tele
        public static final double SPEED = .7;
        public static final double YAW_SPEED = .7;

    }

    public class Braco {

        public static final double MIN_POS = .2;
        public static final double MAX_POS = .7;

    }

    public class Elevador {

        public static final double NV_1 = 1.6;
        public static final double NV_2 = 2.7;
        public static final double NV_3 = 4.2;
        public static final int CONVR = 1480;

        public static final double UP_S = .7;
        public static final double DOWN_S = .5;

    }

    public class Garra {

        public static final double CLAW_CLOSE = 0.2;
        public static final double CLAW_OPEN = 0.7;

        public static final double PITCH_DROP = 0.3;
        public static final double HORIZONTAL = 0.6;
        public static final double PITCH_FALLEN = 0.6;
        public static final double ELEV_UP = 0.35;

        public static final double ROLL_UP = 0.2;
        public static final double ROLL_DOWN = 0.55;
        public static final double ROLL_SIDE_CONE = (ROLL_DOWN-ROLL_UP)/2;


    }

    public class Yaw {

        public static final int LIMIT = 2000;
        public static final double AJUST = 0.06;
        public static final int CONVR = 400;


    }
}
