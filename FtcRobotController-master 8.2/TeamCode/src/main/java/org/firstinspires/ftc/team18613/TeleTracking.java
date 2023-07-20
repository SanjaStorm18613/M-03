package org.firstinspires.ftc.team18613;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

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

    public void runOpMode() {

        detector = new TrackingJunction();
        webcam = new VisionCtrl(this, hardwareMap, telemetry, "Webcam 2");
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

        while (!isStarted()) {

            telemetry.addData("erro", detector.getDetected() ? turret.getTrackingError() : 0);
            telemetry.addData("velocidade", turret.trackingCorrection());
            telemetry.addData("integral", turret.getIntegral());
            telemetry.addData("time", turret.getTime());
            telemetry.update();

            if (isStopRequested()) {
                webcam.stopDetection();
                break;
            }

        }

        while (opModeIsActive()) {

            pilot.updateCommands();
            elevator.removeControl();
            turret.periodic();

            telemetry.addData("erro",detector.getDetected() ? turret.getTrackingError() : 0);
            telemetry.addData("velocidade", turret.trackingCorrection());
            telemetry.addData("integral", turret.getIntegral());
            telemetry.addData("time", turret.getTime());
            telemetry.update();

        }
        webcam.stopDetection();
    }
}
