package org.firstinspires.ftc.team18613;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.team18613.Subsystems.Arm;
import org.firstinspires.ftc.team18613.Subsystems.Claw;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;
import org.firstinspires.ftc.team18613.Subsystems.Elevator;
import org.firstinspires.ftc.team18613.Subsystems.Turret;

import java.util.ArrayList;

@Autonomous(name = "CALIBRATION_AUTO", group = "LinearOpMode")
public class CALIBRATION_AUTO extends LinearOpMode {

    DTMecanum drive;
    Elevator elevator;
    Turret turret;
    Claw claw;
    Arm arm;

    public void runOpMode() {

        elevator = new Elevator(this);
        arm = new Arm(this, elevator);
        claw = new Claw(this, elevator, arm);
        turret = new Turret(this, elevator, claw);
        drive = new DTMecanum(this, turret);

        ContantsAuto_MultTasks cAuto = new ContantsAuto_MultTasks();
        ArrayList<ArrayList<Double[]>> steps = cAuto.getMultTasks();

        int idxActionSteps = 0;
        ArrayList<Double[]> actionSteps = new ArrayList<>();
        boolean A = true, UP = true, DOWN = true;
        double drAdjust = 0.0, drTurnAdjust = 0.0, elAdjust = 0.0, ywAdjust = 0.0;

        waitForStart();

        while (opModeIsActive() && steps.size() != 0) {

            for (int i = 0; i <= idxActionSteps; i++){
                actionSteps.add(steps.get(0).get(i));
            }

            for (Double[] action : actionSteps) {

                if (action[1].equals(cAuto.DR)) {
                    drive.move(true, action[2].equals(cAuto.DR_SIDE), 0.5, 800, 50, action[0] + drAdjust, 0);

                } else if (action[1].equals(cAuto.DR_TURN)) {
                    drive.move(true, false, 0.5, 800, 50, 0, action[0] + drTurnAdjust);

                } else if (action[1].equals(cAuto.EL)) {
                    elevator.setPos(action[0] + elAdjust, Constants.Elevator.UP_SPEED);

                } else if (action[1].equals(cAuto.CL)) {
                    claw.setClaw(action[0]);

                } else if (action[1].equals(cAuto.PT)) {
                    claw.setPitch(action[0], action[2]);

                } else if (action[1].equals(cAuto.YW)) {
                    turret.setPos(action[0] + ywAdjust, .9);

                }
            }

            if (gamepad1.a && A){
                if (idxActionSteps < steps.get(0).size()-1) {
                    idxActionSteps++;
                } else if (steps.size() > 1 && !turret.getBusy() && !claw.getBusy()
                        && !elevator.getBusy() && !drive.getBusy()) {
                    steps.remove(0);
                    idxActionSteps = 0;
                }

            } A = !gamepad1.a;

            if ((gamepad1.dpad_up && UP) || (gamepad1.dpad_down && DOWN)){
                if (steps.get(0).get(idxActionSteps)[1].equals(cAuto.DR)) {
                    drAdjust += 5 * (gamepad1.dpad_down ? -1 : 1);

                } else if (steps.get(0).get(idxActionSteps)[1].equals(cAuto.DR_TURN)) {
                    drTurnAdjust += 2 * (gamepad1.dpad_down ? -1 : 1);

                } else if (steps.get(0).get(idxActionSteps)[1].equals(cAuto.EL)) {
                    elAdjust += 0.25 * (gamepad1.dpad_down ? -1 : 1);

                } else if (steps.get(0).get(idxActionSteps)[1].equals(cAuto.YW)) {
                    ywAdjust += 0.05 * (gamepad1.dpad_down ? -1 : 1);
                }

            }
            UP = !gamepad1.dpad_up;
            DOWN = !gamepad1.dpad_down;

            /*if (steps.size() > 0 && !turret.getBusy() && !claw.getBusy()
                    && !elevator.getBusy() && !drive.getBusy()) {
                steps.remove(0);
            }*/

            arm.periodic();
            claw.autoPeriodic();
            telemetry.update();
        }
    }
}