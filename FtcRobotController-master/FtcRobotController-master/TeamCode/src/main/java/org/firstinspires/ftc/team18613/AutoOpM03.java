package org.firstinspires.ftc.team18613;


import static org.firstinspires.ftc.team18613.Vision.PipelineColors.DetectionColor.BLACK;
import static org.firstinspires.ftc.team18613.Vision.PipelineColors.DetectionColor.CIAN;
import static org.firstinspires.ftc.team18613.Vision.PipelineColors.DetectionColor.GREEN;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.team18613.Subsystems.Arm;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;
import org.firstinspires.ftc.team18613.Subsystems.Elevator;
import org.firstinspires.ftc.team18613.Subsystems.Claw;
import org.firstinspires.ftc.team18613.Subsystems.Turret;
import org.firstinspires.ftc.team18613.Vision.PipelineColors;
import org.firstinspires.ftc.team18613.Vision.VisionCtrl;
import org.firstinspires.ftc.team18613.utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;

@Autonomous(name = "AutoOpM03", group = "Linear Opmode")
public class AutoOpM03 extends LinearOpMode {

    DTMecanum drive;
    Elevator elevator;
    Turret turret;
    Claw claw;
    Arm arm;
    ElapsedTime time;
    VisionCtrl vision;
    PipelineColors pipeline;

    public static Telemetry tel;
    public static HardwareMap hm;

    double parkArea = 0;
    public final int dr = 0, el = 1, cl = 2, yw = 3;

    public void runOpMode() {

        elevator = new Elevator(this);
        arm = new Arm(this, elevator);
        claw = new Claw(this, elevator, arm);
        turret = new Turret(this, elevator, claw);
        drive = new DTMecanum (this, turret);

        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        time.startTime();

        vision = new VisionCtrl(this, hardwareMap, telemetry);
        pipeline = vision.getPipeline();


        while (!isStarted() && !isStopRequested()) {
            switch (pipeline.getColorDetected()) {
                case GREEN:
                    parkArea = -50;
                    break;
                case BLACK:
                    parkArea = 0;
                    break;
                case CIAN:
                    parkArea = 50;
                    break;
            }
            telemetry.addData("Color Detected", pipeline.getColorDetected());
            telemetry.update();
        }
 //*/

        ArrayList<Pair<Double, Integer>> steps = new ArrayList<>(Arrays.asList(
                //0-dr, 1-el, 2-cl, 3-yw
                new Pair<>(1., el)
                ,new Pair<>(0., cl)
                ,new Pair<>(40., dr)
                ,new Pair<>(2., el)
                ,new Pair<>(.5, yw)
                ,new Pair<>(1., cl)
                ,new Pair<>(0., yw)
                ,new Pair<>(0., el)
                ,new Pair<>(-20., dr)
                ,new Pair<>(40., dr)


        ));

        ArrayList<Boolean> forward = new ArrayList<>(Arrays.asList(
                false
                ,false
                ,true
        ));

        waitForStart();


        while (opModeIsActive() && steps.size() != 0) {

            switch (pipeline.getColorDetected()) {
                case GREEN:
                    parkArea = -50;
                    break;
                case BLACK:
                    parkArea = 0;
                    break;
                case CIAN:
                    parkArea = 50;
                    break;
            }
            telemetry.addData("Color Detected", pipeline.getColorDetected());
            telemetry.update();

            /*switch (steps.get(0).secondValue()) {

                case 0:
                    drive.move(forward.get(0), 0.5, 800, 80, steps.get(0).firstValue(), 0);
                    if (!drive.getBusy() && steps.size() > 0) {
                        steps.remove(0);
                        forward.remove(0);
                    }
                    break;

                case 1:
                    elevator.setPos(steps.get(0).firstValue(), 0.7);
                    if (!elevator.getBusy() && steps.size() > 0) {
                        steps.remove(0);
                    }
                    break;

                case 2:
                    claw.setClaw(steps.get(0).firstValue(), 1500);
                    if (!claw.getBusy() && steps.size() > 0) {
                        steps.remove(0);
                    }
                    break;

                case 3:
                    turret.setPos(steps.get(0).firstValue(), .9);
                    if (!turret.getBusy() && steps.size() > 0) {
                        steps.remove(0);
                    }
                    break;*/
            }
            //*/

            telemetry.update();
        }
    }


/*
            .3,//el
            .0,//cl
            50.0,//dr-f
            .5,//el
            -20.0,//dr-t
            1.0,//cl
            20.0,//dr-t
            .0,//el
            20.0,//dr-f
            parkArea//dr-t
 */
