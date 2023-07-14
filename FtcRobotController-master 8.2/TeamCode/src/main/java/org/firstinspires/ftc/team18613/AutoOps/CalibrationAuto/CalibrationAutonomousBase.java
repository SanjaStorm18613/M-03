package org.firstinspires.ftc.team18613.AutoOps.CalibrationAuto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.team18613.AutoOps.ConstantsAuto;
import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystems.Arm;
import org.firstinspires.ftc.team18613.Subsystems.Claw;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;
import org.firstinspires.ftc.team18613.Subsystems.Elevator;
import org.firstinspires.ftc.team18613.Subsystems.Turret;
import org.firstinspires.ftc.team18613.Vision.PipelineColors;
import org.firstinspires.ftc.team18613.Vision.VisionCtrl;

import java.util.ArrayList;

public class CalibrationAutonomousBase {

    DTMecanum drive;
    Elevator elevator;
    Turret turret;
    Claw claw;
    Arm arm;
    ConstantsAuto cAuto;
    LinearOpMode opMode;

    ArrayList<String[][]> valuesCalibration = new ArrayList<>();
    ArrayList<ArrayList<Double[]>> steps;
    PipelineColors.DetectionColor colorParkArea = PipelineColors.DetectionColor.YELLOW;

    boolean init = false;
    int idxActionSteps = 0;
    boolean A = true, X = true, Y = true;
    double drAdjust = 0.0, drTurnAdjust = 0.0, elAdjust = 0.0, ywAdjust = 0.0, ptAdjust = 0.0, sizeStep;


    public CalibrationAutonomousBase(LinearOpMode opMode, ConstantsAuto cAuto) {

        elevator = new Elevator(opMode);
        arm = new Arm(opMode, elevator);
        claw = new Claw(opMode, elevator, arm);
        turret = new Turret(opMode, elevator);
        drive = new DTMecanum(opMode, turret);

        this.cAuto = cAuto;
        this.opMode = opMode;
    }

    public void initiation() {

        VisionCtrl webcam = new VisionCtrl(opMode, opMode.hardwareMap, opMode.telemetry, true);
        PipelineColors detector = webcam.getPipelineAuto();

        while (!opMode.isStarted() && !opMode.isStopRequested()){
            colorParkArea = detector.getColorDetected();
            opMode.telemetry.addData("COR DETECTADA", colorParkArea);
            opMode.telemetry.update();
        }

        drive.setDownEncoderServo(false);

        webcam.stopDetection();

    }

    public void execution() {

        register();

        for (int action = 0; action <= idxActionSteps; action++) {

            if (steps.get(0).get(action)[1].equals(cAuto.DR)) {
                drive.setMove(false, steps.get(0).get(action)[2].equals(cAuto.DR_SIDE), 0.5, 800, 0.00005, steps.get(0).get(action)[0], 0);
                drive.setValCalibration(drAdjust);
                opMode.telemetry.addData("DRIVE +", drAdjust);

            } else if (steps.get(0).get(action)[1].equals(cAuto.DR_TURN)) {
                drive.setMove(true, false, 0.5, 800, 0.00005, 0, steps.get(0).get(action)[0] + drTurnAdjust);
                opMode.telemetry.addData("DRIVE TURN +", drTurnAdjust);

            } else if (steps.get(0).get(action)[1].equals(cAuto.EL)) {
                elevator.setPos(steps.get(0).get(action)[0] + elAdjust, Constants.Elevator.UP_SPEED);
                opMode.telemetry.addData("ELEVATOR +", elAdjust);

            } else if (steps.get(0).get(action)[1].equals(cAuto.CL)) {
                claw.setClaw(steps.get(0).get(action)[0]);

            } else if (steps.get(0).get(action)[1].equals(cAuto.PT)) {
                init = true;
                claw.setPitch(steps.get(0).get(action)[0] + ptAdjust, steps.get(0).get(action)[2]);
                opMode.telemetry.addData("PITCH +", ptAdjust);

            } else if (steps.get(0).get(action)[1].equals(cAuto.YW)) {
                turret.setPos(steps.get(0).get(action)[0] + ywAdjust, .9);
                opMode.telemetry.addData("YAW +", ywAdjust);

            }
        }

        if (opMode.gamepad1.x && X){

            if (steps.size() >= 1 && (valuesCalibration.size() < sizeStep)) {
                valuesCalibration.add(new String[][] {{"DRIVE", ""+drAdjust},
                        {"DRIVE TURN", ""+drTurnAdjust},
                        {"ELEVATOR", ""+elAdjust},
                        {"YAW", ""+ywAdjust},
                        {"PITCH", ""+ptAdjust}});
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
                ptAdjust = 0;

            }
        }

        if ((opMode.gamepad1.a && A) || (opMode.gamepad1.y && Y)){

            if (steps.get(0).get(idxActionSteps)[1].equals(cAuto.DR)) {
                drAdjust += 5.0 * (opMode.gamepad1.a ? -1 : 1);

            } else if (steps.get(0).get(idxActionSteps)[1].equals(cAuto.DR_TURN)) {
                drTurnAdjust += 2.0 * (opMode.gamepad1.a ? -1 : 1);

            } else if (steps.get(0).get(idxActionSteps)[1].equals(cAuto.EL)) {
                elAdjust += 0.25 * (opMode.gamepad1.a ? -1 : 1);

            } else if (steps.get(0).get(idxActionSteps)[1].equals(cAuto.YW)) {
                ywAdjust += 0.05 * (opMode.gamepad1.a ? -1 : 1);

            } else if (steps.get(0).get(idxActionSteps)[1].equals(cAuto.PT)) {
                ptAdjust += 0.05 * (opMode.gamepad1.a ? -1 : 1);

            }

        }

        X = !opMode.gamepad1.x;
        A = !opMode.gamepad1.a;
        Y = !opMode.gamepad1.y;

        if (init) {
            arm.periodic();
            claw.autoPeriodic();
            drive.autoPeriodic();
        }
        opMode.telemetry.update();
    }

    public void register() {
        opMode.telemetry.addLine("REGISTRO DE CALIBRAÇÂO:");

        for (String[][] v : valuesCalibration) {
            opMode.telemetry.addLine("ETAPA: " + valuesCalibration.indexOf(v));

            for (String[] i : v) {
                opMode.telemetry.addData(valuesCalibration.indexOf(v) +"-"+ i[0], i[1]);
            }
            opMode.telemetry.addLine();

        }
        opMode.telemetry.addLine("--------------------------------------------");


    }

    public void setSteps(ArrayList<ArrayList<Double[]>> steps) {
        this.steps = steps;
        sizeStep = steps.size();
    }

    public PipelineColors.DetectionColor getColorParkArea() {
        return colorParkArea;
    }

    public boolean isFinished() {
        return steps.size() == 0;
    }
}
