package org.firstinspires.ftc.team18613;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team18613.Commands.Claw.AngulationDrop;
import org.firstinspires.ftc.team18613.Commands.Claw.Drop;
import org.firstinspires.ftc.team18613.Commands.Claw.HorizontalColect;
import org.firstinspires.ftc.team18613.Commands.Claw.InvertCone;
import org.firstinspires.ftc.team18613.Commands.Claw.LoweredFrontColect;
import org.firstinspires.ftc.team18613.Commands.Claw.LoweredSideColect;
import org.firstinspires.ftc.team18613.Commands.Claw.Retract;
import org.firstinspires.ftc.team18613.Commands.Drive.Move;
import org.firstinspires.ftc.team18613.Commands.Drive.Slow;
import org.firstinspires.ftc.team18613.Commands.Drive.TurnD;
import org.firstinspires.ftc.team18613.Commands.Elevator.AdjustStage;
import org.firstinspires.ftc.team18613.Commands.Elevator.ShiftStage;
import org.firstinspires.ftc.team18613.Commands.Turret.DisableTracking;
import org.firstinspires.ftc.team18613.Commands.Turret.EnableTracking;
import org.firstinspires.ftc.team18613.Commands.Turret.Stop;
import org.firstinspires.ftc.team18613.Commands.Turret.TurnT;
import org.firstinspires.ftc.team18613.Subsystems.Arm;
import org.firstinspires.ftc.team18613.Subsystems.Claw;
import org.firstinspires.ftc.team18613.Subsystems.Controller;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;
import org.firstinspires.ftc.team18613.Subsystems.Elevator;
import org.firstinspires.ftc.team18613.Subsystems.Turret;
import org.firstinspires.ftc.team18613.Utils.Supplier;
import org.firstinspires.ftc.team18613.Utils.SupplierPair;
import org.firstinspires.ftc.team18613.Vision.TrackingJunction;
import org.firstinspires.ftc.team18613.Vision.VisionCtrl;

@TeleOp(name = "TeleOpM03", group = "Linear Opmode")

public class TeleOpM03 extends LinearOpMode {

    Elevator elevator;
    DTMecanum drive;
    Turret turret;
    Claw claw;
    Arm arm;

    VisionCtrl webcam;
    TrackingJunction detector;

    public static Controller pilot, copilot;

    public void runOpMode() {

        pilot = new Controller(gamepad1);
        copilot = new Controller(gamepad2);

        detector = new TrackingJunction();
        webcam = new VisionCtrl(this, false);
        webcam.setPepiline(detector);

        elevator = new Elevator(this);
        arm = new Arm(this, elevator);
        claw = new Claw(this, elevator, arm);
        turret = new Turret(this, elevator, detector);
        drive = new DTMecanum (this, turret);

        while (!isStarted() && !isStopRequested()) {

            telemetry.addData("erro", detector.getDetected() ? turret.getTrackingError() : 0);
            telemetry.addData("velocidade", turret.trackingCorrection());
            telemetry.addData("integral", turret.getIntegral());
            telemetry.addData("time", turret.getTimeIntegral());
            telemetry.update();
        }
        webcam.stopViewport();

        //TURRET
        copilot.registerAction(Controller.right, new TurnT(turret, false), Controller.Actions.WHILE_PRESSED);
        copilot.registerAction(Controller.right, new Stop(turret), Controller.Actions.ON_RELEASED);

        copilot.registerAction(Controller.left, new TurnT(turret, true), Controller.Actions.WHILE_PRESSED);
        copilot.registerAction(Controller.left, new Stop(turret), Controller.Actions.ON_RELEASED);

        copilot.registerAction(Controller.left_bumper, new EnableTracking(turret), Controller.Actions.WHILE_PRESSED);
        copilot.registerAction(Controller.left_bumper, new DisableTracking(turret),Controller.Actions.ON_RELEASED);

        //DRIVE MECANUM
        pilot.registerAction(Controller.right_stick_x, (Supplier<Float> val) -> new TurnD(drive, val));
        pilot.registerAction(new int[]{Controller.left_stick_x, Controller.left_stick_y}, (SupplierPair<Float> val) -> new Move(drive, val));
        pilot.registerAction(Controller.right_trigger, (Supplier<Float> val) -> new Slow(drive, val));

        //CLAW
        pilot.registerAction(Controller.a, new HorizontalColect(claw), Controller.Actions.ON_PRESSED);
        pilot.registerAction(Controller.left_bumper, new HorizontalColect(claw), Controller.Actions.ON_PRESSED);
        pilot.registerAction(Controller.b, new LoweredFrontColect(claw), Controller.Actions.ON_PRESSED);
        pilot.registerAction(Controller.x, new LoweredSideColect(claw), Controller.Actions.ON_PRESSED);
        copilot.registerAction(Controller.b, new Retract(claw), Controller.Actions.ON_PRESSED);
        copilot.registerAction(Controller.right_trigger_not_zero, new Drop(claw), Controller.Actions.ON_PRESSED);
        copilot.registerAction(Controller.left_trigger, (Supplier<Float> val) -> new AngulationDrop(claw, val));
        copilot.registerAction(Controller.a, new InvertCone(claw), Controller.Actions.ON_PRESSED);

        //ELEVATOR
        copilot.registerAction(Controller.y, new ShiftStage(elevator, true), Controller.Actions.ON_PRESSED);
        copilot.registerAction(Controller.x, new ShiftStage(elevator, false), Controller.Actions.ON_PRESSED);

        copilot.registerAction(Controller.up, new AdjustStage(elevator, true), Controller.Actions.ON_PRESSED);
        copilot.registerAction(Controller.down, new AdjustStage(elevator, false), Controller.Actions.ON_PRESSED);


        while (opModeIsActive()) {

            copilot.updateCommands();
            pilot.updateCommands();

            elevator.removeControl();
            arm.removeControl();

            drive.periodic();
            claw.periodic();
            turret.periodic();
            arm.periodic();
            elevator.periodic();

            telemetry.update();

        }
        webcam.stopDetection();
    }
}
