package org.firstinspires.ftc.team18613;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team18613.Subsystems.Arm;
import org.firstinspires.ftc.team18613.Subsystems.Claw;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;
import org.firstinspires.ftc.team18613.Subsystems.Elevator;
import org.firstinspires.ftc.team18613.Subsystems.Turret;

import java.util.ArrayList;
import java.util.Arrays;

@TeleOp(name = "CALIBRATION_AUTO", group = "LinearOpMode")
public class CALIBRATION_AUTO extends LinearOpMode {

    DTMecanum drive;
    Elevator elevator;
    Turret turret;
    Claw claw;
    Arm arm;

    ArrayList<String[][]> valuesCalibration = new ArrayList<>();

    public void runOpMode() {

        elevator = new Elevator(this);
        arm = new Arm(this, elevator);
        claw = new Claw(this, elevator, arm);
        turret = new Turret(this, elevator);
        drive = new DTMecanum(this, turret);

        ContantsAuto cAuto = new ContantsAuto();
        ArrayList<ArrayList<Double[]>> steps = cAuto.getAutoDemostracao();

        int idxActionSteps = 0;
        boolean A = true, X = true, Y = true;
        double drAdjust = 0.0, drTurnAdjust = 0.0, elAdjust = 0.0, ywAdjust = 0.0, sizeStep = steps.size();

        waitForStart();

        while (opModeIsActive() && steps.size() != 0) {

            regiter();

            for (int action = 0; action <= idxActionSteps; action++) {

                if (steps.get(0).get(action)[1].equals(cAuto.DR)) {
                    drive.setMove(true, steps.get(0).get(action)[2].equals(cAuto.DR_SIDE), 0.5, 800, 0.0005, steps.get(0).get(action)[0], 0);
                    drive.setValCalibration(drAdjust);
                    telemetry.addData("drAdjust", drAdjust);

                } else if (steps.get(0).get(action)[1].equals(cAuto.DR_TURN)) {
                    drive.setMove(true, false, 0.5, 800, 0.005, 0, steps.get(0).get(action)[0] + drTurnAdjust);
                    telemetry.addData("drTurnAdjust", drTurnAdjust);

                } else if (steps.get(0).get(action)[1].equals(cAuto.EL)) {
                    elevator.setPos(steps.get(0).get(action)[0] + elAdjust, Constants.Elevator.UP_SPEED);
                    telemetry.addData("elAdjust", elAdjust);

                } else if (steps.get(0).get(action)[1].equals(cAuto.CL)) {
                    claw.setClaw(steps.get(0).get(action)[0]);

                } else if (steps.get(0).get(action)[1].equals(cAuto.PT)) {
                    claw.setPitch(steps.get(0).get(action)[0], steps.get(0).get(action)[2]);

                } else if (steps.get(0).get(action)[1].equals(cAuto.YW)) {
                    turret.setPos(steps.get(0).get(action)[0] + ywAdjust, .9);
                    telemetry.addData("ywAdjust", ywAdjust);

                }
            }

            if (gamepad1.x && X){

                if (steps.size() >= 1 && (valuesCalibration.size() < sizeStep)) {
                    valuesCalibration.add(new String[][] {{"DRIVE", ""+drAdjust},
                            {"DRIVE TURN", ""+drTurnAdjust},
                            {"ELEVATOR", ""+elAdjust},
                            {"YAW", ""+ywAdjust}});
                }

                if (idxActionSteps < steps.get(0).size()-1) {
                    idxActionSteps++;
                } else if (steps.size() > 1 && !turret.getBusy() && !claw.getBusy()
                        && !elevator.getBusy() && !drive.getBusy()) {
                    steps.remove(0);
                    idxActionSteps = 0;

                    drAdjust = 0;
                    drTurnAdjust = 0;
                    elAdjust = 0;
                    ywAdjust = 0;

                }
            }

            if ((gamepad1.a && A) || (gamepad1.y && Y)){
                if (steps.get(0).get(idxActionSteps)[1].equals(cAuto.DR)) {
                    drAdjust += 5.0 * (gamepad1.a ? -1 : 1);

                } else if (steps.get(0).get(idxActionSteps)[1].equals(cAuto.DR_TURN)) {
                    drTurnAdjust += 2.0 * (gamepad1.a ? -1 : 1);

                } else if (steps.get(0).get(idxActionSteps)[1].equals(cAuto.EL)) {
                    elAdjust += 0.25 * (gamepad1.a ? -1 : 1);

                } else if (steps.get(0).get(idxActionSteps)[1].equals(cAuto.YW)) {
                    ywAdjust += 0.05 * (gamepad1.a ? -1 : 1);
                }

            }

            X = !gamepad1.x;
            A = !gamepad1.a;
            Y = !gamepad1.y;



            arm.periodic();
            claw.autoPeriodic();
            drive.autoPeriodic();
            telemetry.update();
        }
        drive.tankDrive(0);
    }

    public void regiter() {
        telemetry.addLine("REGISTRO DE CALIBRAÇÂO:");

        for (String[][] v : valuesCalibration) {
            telemetry.addLine("ETAPA: " + valuesCalibration.indexOf(v));

            for (String[] i : v) {
                telemetry.addData(valuesCalibration.indexOf(v) +"-"+ i[0], i[1]);
            }
            telemetry.addLine();

        }
        telemetry.addLine("--------------------------------------------");


    }
}