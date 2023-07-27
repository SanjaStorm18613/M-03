package org.firstinspires.ftc.team18613;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team18613.Commands.Turret.DisableTracking;
import org.firstinspires.ftc.team18613.Commands.Turret.EnableTracking;
import org.firstinspires.ftc.team18613.Commands.Turret.Stop;
import org.firstinspires.ftc.team18613.Commands.Turret.TurnT;
import org.firstinspires.ftc.team18613.Subsystems.Controller;
import org.firstinspires.ftc.team18613.Subsystems.Elevator;
import org.firstinspires.ftc.team18613.Subsystems.Turret;
import org.firstinspires.ftc.team18613.Vision.TrackingJunction;
import org.firstinspires.ftc.team18613.Vision.VisionCtrl;

@TeleOp(name = "TeleTracking", group = "Linear Opmode")

public class TeleTracking extends LinearOpMode {

    VisionCtrl webcam;
    TrackingJunction detector;

    Turret turret;
    Elevator elevator;
    public static Controller pilot, copilot;
    double lastValue = 0, sumPositions = 0, cont = 0;
    ElapsedTime time;

    public void runOpMode() {

        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        detector = new TrackingJunction();
        webcam = new VisionCtrl(this, false);
        webcam.setPepiline(detector);

        elevator = new Elevator(this);
        turret = new Turret(this, elevator, detector);

        pilot = new Controller(gamepad1);
        copilot = new Controller(gamepad2);

        pilot.registerAction(Controller.left_bumper, new EnableTracking(turret),
                                                                    Controller.Actions.WHILE_PRESSED);
        pilot.registerAction(Controller.left_bumper, new DisableTracking(turret),
                                                                    Controller.Actions.ON_RELEASED);

        pilot.registerAction(Controller.right, new TurnT(turret, false),
                                                                Controller.Actions.WHILE_PRESSED);
        pilot.registerAction(Controller.right, new Stop(turret), Controller.Actions.ON_RELEASED);

        pilot.registerAction(Controller.left, new TurnT(turret, true),
                                                                Controller.Actions.WHILE_PRESSED);
        pilot.registerAction(Controller.left, new Stop(turret), Controller.Actions.ON_RELEASED);

        while (!isStarted() && !isStopRequested()) {

            update();

        }
        webcam.stopViewport();

        while (opModeIsActive()) {

            pilot.updateCommands();
            elevator.removeControl();
            turret.periodic();

            update();

        }
        webcam.stopDetection();
    }

    public void update() {

        double value = turret.getTrackingError();

        sumPositions += value;
        cont++;

        telemetry.addData("sumPositions / cont", sumPositions / cont);

        if (time.time() > 500) {
            time.reset();
            lastValue = sumPositions / cont;
            cont = 0;
            sumPositions = 0;
        }

        telemetry.addData("lastValue", lastValue);
        telemetry.addData("value", value);

        telemetry.addData("erro",detector.getDetected() ? turret.getTrackingError() : 0);
        telemetry.addData("velocidade", turret.trackingCorrection());
        telemetry.addData("integral", turret.getIntegral());
        telemetry.addData("time", turret.getTimeIntegral());
        telemetry.addLine();
        telemetry.addData("total height", detector.getSizeJunction()[0]);
        telemetry.addData("real width", detector.getSizeJunction()[1]);
        telemetry.addData("real height", detector.getSizeJunction()[2]);
        telemetry.update();

    }
}
