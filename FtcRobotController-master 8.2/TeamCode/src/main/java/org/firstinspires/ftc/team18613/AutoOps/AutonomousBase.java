package org.firstinspires.ftc.team18613.AutoOps;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystems.Arm;
import org.firstinspires.ftc.team18613.Subsystems.Claw;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;
import org.firstinspires.ftc.team18613.Subsystems.Elevator;
import org.firstinspires.ftc.team18613.Subsystems.Turret;
import org.firstinspires.ftc.team18613.Vision.PipelineColors;
import org.firstinspires.ftc.team18613.Vision.TrackingJunction;
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

        this.cAuto = cAuto;
        this.opMode = opMode;
    }

    public void initiation() {

        PipelineColors detectorAuto = new PipelineColors();
        VisionCtrl webcam = new VisionCtrl(opMode, true);
        webcam.setPepiline(detectorAuto);

        while (!opMode.isStarted() && !opMode.isStopRequested()){
            colorParkArea = detectorAuto.getColorDetected();
            opMode.telemetry.addData("COR DETECTADA", colorParkArea);
            opMode.telemetry.update();
        }


        webcam.stopDetection();

        TrackingJunction detectorTele = new TrackingJunction();
        webcam = new VisionCtrl(opMode, false);
        webcam.setPepiline(detectorTele);

        turret = new Turret(opMode, elevator, detectorTele);
        drive = new DTMecanum(opMode, turret);

        drive.setDownEncoderServo(false);

        webcam.stopViewport();

    }

    public void execution() {

        if (steps.size() != 0) {

            for (Double[] action : steps.get(0)) {

                if (action[1].equals(cAuto.DR)) {
                    drive.setMove(false, action[2].equals(cAuto.DR_SIDE), 0.5,
                                                    800, 0.00004, action[0],0.02, 0);

                } else if (action[1].equals(cAuto.DR_TURN)) {
                    drive.setMove(true, false, 0.5, 800,0.00004, 0,0.02,  action[0]);

                } else if (action[1].equals(cAuto.EL)) {
                    elevator.setPos(action[0], Constants.Elevator.UP_SPEED);

                } else if (action[1].equals(cAuto.CL)) {
                    claw.setClaw(action[0]);

                } else if (action[1].equals(cAuto.PT)) {
                    claw.setPitch(action[0], action[2]);
                    init = true;

                } else if (action[1].equals(cAuto.YW)) {
                    turret.setPos(action[0], .9);

                } else if (action[1].equals(cAuto.YW_DT)) {
                    turret.setInitTracker(action[0]);
                }
            }
        }

        opMode.telemetry.addData("detec", turret.getTrackerBusy());
        opMode.telemetry.addData("drive", drive.getBusy());
        opMode.telemetry.addData("elevator", elevator.getBusy());
        opMode.telemetry.addData("claw", claw.getBusy());
        opMode.telemetry.addData("turret", turret.getBusy());
        opMode.telemetry.update();

        if (init) {
            arm.periodic();
            claw.autoPeriodic();
            turret.autoPeriodic();
            drive.autoPeriodic();

        }


        if (steps.size() != 0 && !turret.getBusy() && !claw.getBusy() && !elevator.getBusy()
                && !drive.getBusy() && !turret.getTrackerBusy()) {
            steps.remove(0);
        }
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
