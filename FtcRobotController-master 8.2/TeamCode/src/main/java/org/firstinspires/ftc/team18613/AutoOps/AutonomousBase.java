package org.firstinspires.ftc.team18613.AutoOps;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystems.Arm;
import org.firstinspires.ftc.team18613.Subsystems.Claw;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;
import org.firstinspires.ftc.team18613.Subsystems.Elevator;
import org.firstinspires.ftc.team18613.Subsystems.Turret;
import org.firstinspires.ftc.team18613.Vision.PipelineColors;
import org.firstinspires.ftc.team18613.Vision.VisionCtrl;

import java.util.ArrayList;

public class AutonomousBase {

    DTMecanum drive;
    Elevator elevator;
    Turret turret;
    Claw claw;
    Arm arm;
    ConstantsAuto cAuto;
    LinearOpMode opMode;

    ArrayList<ArrayList<Double[]>> steps;
    PipelineColors.DetectionColor colorParkArea = PipelineColors.DetectionColor.YELLOW;
    boolean init = false;

    public AutonomousBase(LinearOpMode opMode, ConstantsAuto cAuto) {

        elevator = new Elevator(opMode);
        arm = new Arm(opMode, elevator);
        claw = new Claw(opMode, elevator, arm);
        turret = new Turret(opMode, elevator);
        drive = new DTMecanum(opMode, turret);

        this.cAuto = cAuto;
        this.opMode = opMode;
    }

    public void initiation() {

        VisionCtrl webcam = new VisionCtrl(opMode, opMode.hardwareMap, opMode.telemetry);
        PipelineColors detector = webcam.getPipeline();

        while (!opMode.isStarted() && !opMode.isStopRequested()){
            colorParkArea = detector.getColorDetected();
            opMode.telemetry.addData("COR DETECTADA", colorParkArea);
            opMode.telemetry.update();
        }

        drive.setDownEncoderServo(false);

        webcam.stopDetection();

    }

    public void execution() {

        if (steps.size() != 0) {

            for (Double[] action : steps.get(0)) {

                if (action[1].equals(cAuto.DR)) {
                    drive.setMove(false, action[2].equals(cAuto.DR_SIDE), 0.5, 800, 0.00005, action[0], 0);

                } else if (action[1].equals(cAuto.DR_TURN)) {
                    drive.setMove(true, false, 0.5, 800, 0.00005, 0, action[0]);

                } else if (action[1].equals(cAuto.EL)) {
                    elevator.setPos(action[0], Constants.Elevator.UP_SPEED);

                } else if (action[1].equals(cAuto.CL)) {
                    claw.setClaw(action[0]);

                } else if (action[1].equals(cAuto.PT)) {
                    claw.setPitch(action[0], action[2]);
                    init = true;

                } else if (action[1].equals(cAuto.YW)) {
                    turret.setPos(action[0], .9);

                }
            }
        }

        if (steps.size() != 0 && !turret.getBusy() && !claw.getBusy()
                && !elevator.getBusy() && !drive.getBusy()) {
            steps.remove(0);
        }

        if (init) {
            arm.periodic();
            claw.autoPeriodic();
            drive.autoPeriodic();
        }

        opMode.telemetry.update();
    }

    public void setSteps(ArrayList<ArrayList<Double[]>> steps) {
        this.steps = steps;
    }

    public PipelineColors.DetectionColor getColorParkArea() {
        return colorParkArea;
    }

    public boolean isFinished() {
        return steps.size() == 0;
    }
}
