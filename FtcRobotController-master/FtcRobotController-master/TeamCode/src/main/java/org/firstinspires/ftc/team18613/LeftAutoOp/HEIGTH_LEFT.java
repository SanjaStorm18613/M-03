package org.firstinspires.ftc.team18613.LeftAutoOp;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.ContantsAuto_Multitasking;
import org.firstinspires.ftc.team18613.Subsystems.Arm;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;
import org.firstinspires.ftc.team18613.Subsystems.Claw;
import org.firstinspires.ftc.team18613.Subsystems.Elevator;
import org.firstinspires.ftc.team18613.Subsystems.Turret;

import java.util.ArrayList;

@Autonomous(name = "HEIGTH_LEFT", group = "LinearOpMode")
public class HEIGTH_LEFT extends LinearOpMode {

    DTMecanum drive;
    Elevator elevator;
    Turret turret;
    Claw claw;
    Arm arm;

    boolean init = false;

    public void runOpMode() {

        elevator = new Elevator(this);
        arm = new Arm(this, elevator);
        claw = new Claw(this, elevator, arm);
        turret = new Turret(this, elevator);
        drive = new DTMecanum(this, turret);

        ContantsAuto_Multitasking cAuto = new ContantsAuto_Multitasking();
        ArrayList<ArrayList<Double[]>> steps = cAuto.getAutoDemostracao();

        waitForStart();

        while (opModeIsActive() && steps.size() != 0) {

            for (Double[] action : steps.get(0)) {

                if (action[1].equals(cAuto.DR)) {
                    drive.move(true, action[2].equals(cAuto.DR_SIDE), 0.5, 800, 0.0005, action[0], 0);

                } else if (action[1].equals(cAuto.DR_TURN)) {
                    drive.move(true, false, 0.5, 800, 0.0005, 0, action[0]);

                } else if (action[1].equals(cAuto.EL)) {
                    elevator.setPos(action[0], Constants.Elevator.UP_SPEED);

                } else if (action[1].equals(cAuto.CL)) {
                    claw.setClaw(action[0]);

                } else if (action[1].equals(cAuto.PT)) {
                    init = true;
                    claw.setPitch(action[0], action[2]);

                } else if (action[1].equals(cAuto.YW)) {
                    turret.setPos(action[0], .9);

                }
            }

            if (steps.size() > 0 && !turret.getBusy() && !claw.getBusy()
                    && !elevator.getBusy() && !drive.getBusy()) {
                steps.remove(0);
            }

            if (init) {
                arm.periodic();
                claw.autoPeriodic();
            }

            telemetry.update();
        }
    }
}