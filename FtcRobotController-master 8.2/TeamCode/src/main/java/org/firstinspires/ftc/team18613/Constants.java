package org.firstinspires.ftc.team18613;

public class Constants {

    public static class Pipeline {

        public static final double[][] AUTO_COLOR_LOW = {{50, 50, 30},  {90, 70, 70}}
                                        ,AUTO_COLOR_UP =  {{80, 180, 360}, {100, 180, 360}};

        public static final double[] TELE_COLOR_LOW = {75, 90, 40}
                                    ,TELE_COLOR_UP = {130, 290, 360};

        public static final int TOLERANCE_AREA = 1000;

    }

    public static class DTMecanum {

        //Tele
        public static final double SPEED = .7
                                    ,ACCELERATION = 1500 //Tempo para atingir setpoint de velocidade
                                    ,PRECISION = 0.25
                                    ,TOLERANCE_DISTANCE = 500
                                    ,TOLERANCE_ANGLE = 3
                                    ,CONVERSION_2_EXTERNAL = 725
                                    ,CONVERSION_2_INTERNAL = 30;
    }

    public static class Braco {

        public static final double MIN_POS = .2
                                    ,MAX_POS = .71;

    }

    public static class Elevator {

        public static final double NV_0 = 0
                                    ,NV_1 = 1.8
                                    ,NV_2 = 3.1
                                    ,NV_3 = 3.75

                                    ,UP_SPEED = 1
                                    ,DOWN_SPEED = .9
                                    ,ADJUST = 0.25;

        public static final int TOLERANCE = 80
                                ,CONVERSION = 1830;

    }

    public static class Claw {

        public static final double CLAW_CLOSE = 0.19
                                    ,CLAW_OPEN = 0.45
                                    ,PITCH_UP = 0.05
                                    ,PITCH_HORIZONTAL = .52
                                    ,PITCH_LOWERED = .87
                                    ,ELEVATOR_UP = 1.2
                                    ,ELEVATOR_UP_RETRAIN = .7
                                    ,ROLL_UP = 0.12
                                    ,ROLL_DOWN = 0.67
                                    ,ROLL_SIDE_CONE = ROLL_UP + Math.abs(ROLL_DOWN-ROLL_UP)/2
                                    ,TRANSITION_TIME = 1000;

    }

    public static class Turret {

        public static final int LIMIT = 1760
                                ,CONVERSION = 400
                                ,COUNTS_PER_REVOLUTION = 1600
                                ,TRACKING_CENTER_OFFSET = 25;

        public static final double SPEED = 1
                                    ,CHASSIS_OPENING = .15
                                    ,TRACKING_CORRECTION_I = 0.000004
                                    ,RSL_POWER = 0.2
                                    ,TRACKING_ERROR_TOLERANCE = 20;

    }
}
