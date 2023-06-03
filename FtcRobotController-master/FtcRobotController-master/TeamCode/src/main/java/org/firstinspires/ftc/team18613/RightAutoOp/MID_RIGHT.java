package org.firstinspires.ftc.team18613.RightAutoOp;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.ContantsAuto;
import org.firstinspires.ftc.team18613.Subsystems.Arm;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;
import org.firstinspires.ftc.team18613.Subsystems.Claw;
import org.firstinspires.ftc.team18613.Subsystems.Elevator;
import org.firstinspires.ftc.team18613.Subsystems.Turret;
import org.firstinspires.ftc.team18613.Vision.PipelineColors;
import org.firstinspires.ftc.team18613.Vision.VisionCtrl;
import org.firstinspires.ftc.team18613.utils.Pair;

import java.util.ArrayList;

@Autonomous(name = "MID_RIGHT", group = "LinearOpMode")
public class MID_RIGHT extends LinearOpMode {

    DTMecanum drive;
    Elevator elevator;
    Turret turret;
    Claw claw;
    Arm arm;
    ElapsedTime time;
    VisionCtrl webcam;
    PipelineColors pipeline;

    boolean init = false;
    Double parkArea = 0.0;

    public void runOpMode() {

        elevator = new Elevator(this);
        arm = new Arm(this, elevator);
        claw = new Claw(this, elevator, arm);
        turret = new Turret(this, elevator, claw);
        drive = new DTMecanum(this, turret);

        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        time.startTime();

        webcam = new VisionCtrl(this, hardwareMap, telemetry);
        pipeline = webcam.getPipeline();

        ContantsAuto cAuto = new ContantsAuto();

        while (!isStarted() && !isStopRequested()) {
            switch (pipeline.getColorDetected()) {
                case GREEN:
                    parkArea = 0.0;
                    break;
                case BLACK:
                    parkArea = -60.0;
                    break;
                case YELLOW:
                    parkArea = 60.0;
                    break;
            }
            telemetry.addData("Color Detected", pipeline.getColorDetected());
            telemetry.update();
        }

        webcam.stopDetection();

        ArrayList<Pair<Double, Integer>> steps = cAuto.getMidSteps(parkArea, 1.0);
        ArrayList<Boolean> drSide = cAuto.getMidDRSide();

        while (opModeIsActive() && steps.size() != 0) {

            switch (steps.get(0).secondValue()) {

                case 0:
                    drive.move(true, drSide.get(0), 0.5, 800, 50, steps.get(0).firstValue(), 0);
                    if (!drive.getBusy() && steps.size() > 0) {
                        steps.remove(0);
                        drSide.remove(0);
                    }
                    break;

                case 1:
                    elevator.setPos(steps.get(0).firstValue(), Constants.Elevator.UP_SPEED);
                    if (!elevator.getBusy() && steps.size() > 0) {
                        steps.remove(0);
                    }
                    break;

                case 2:
                    claw.setClaw(steps.get(0).firstValue());
                    if (!claw.getBusy() && steps.size() > 0) {
                        steps.remove(0);
                    }
                    break;

                case 3:
                    init = true;
                    claw.setPitch(steps.get(0).firstValue(), steps.get(1).firstValue());

                    if (!claw.getBusy() && steps.size() > 0) {
                        steps.remove(1);
                        steps.remove(0);
                    }
                    break;

                case 4:
                    turret.setPos(steps.get(0).firstValue(), .9);
                    if (!turret.getBusy() && steps.size() > 0) {
                        steps.remove(0);
                    }
                    break;

                case 7:
                    elevator.setPos(steps.get(0).firstValue(), 0.7);
                    turret.setPos(steps.get(1).firstValue(), .9);

                    if (!elevator.getBusy() && !turret.getBusy() && steps.size() > 0) {
                        steps.remove(1);
                        steps.remove(0);
                    }
                    break;
            }

            if (init) {
                arm.periodic();
                claw.autoPeriodic();
            }

            telemetry.update();
        }
    }
}