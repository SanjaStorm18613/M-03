package org.firstinspires.ftc.team18613;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.team18613.Subsystems.Arm;
import org.firstinspires.ftc.team18613.Subsystems.Controller;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;
import org.firstinspires.ftc.team18613.Subsystems.Elevator;
import org.firstinspires.ftc.team18613.Subsystems.Claw;
import org.firstinspires.ftc.team18613.Subsystems.Turret;
import org.firstinspires.ftc.team18613.commands.Claw.AngulationDrop;
import org.firstinspires.ftc.team18613.commands.Claw.Drop;
import org.firstinspires.ftc.team18613.commands.Claw.HorizontalColect;
import org.firstinspires.ftc.team18613.commands.Claw.LoweredFrontColect;
import org.firstinspires.ftc.team18613.commands.Claw.LoweredSideColect;
import org.firstinspires.ftc.team18613.commands.Claw.Retract;
import org.firstinspires.ftc.team18613.commands.Drive.Move;
import org.firstinspires.ftc.team18613.commands.Drive.Slow;
import org.firstinspires.ftc.team18613.commands.Elevator.AdjustStage;
import org.firstinspires.ftc.team18613.commands.Elevator.ShiftStage;
import org.firstinspires.ftc.team18613.commands.Turret.Stop;
import org.firstinspires.ftc.team18613.commands.Turret.Turn;
import org.firstinspires.ftc.team18613.utils.FloatPair;
import org.firstinspires.ftc.team18613.utils.Supplier;

@TeleOp(name = "TeleOpM03", group = "Linear Opmode")

public class TeleOpM03 extends LinearOpMode {

    Elevator elevator;
    DTMecanum drive;
    Turret turret;
    Claw claw;
    Arm arm;

    public static Telemetry tel;
    public static HardwareMap hm;

    public static Controller pilot, copilot;

    public void runOpMode() {
        tel = telemetry;
        hm = hardwareMap;
        pilot = new Controller(gamepad1);
        copilot = new Controller(gamepad2);

        elevator = new Elevator();
        arm = new Arm(elevator);
        claw = new Claw(elevator, arm);
        turret = new Turret(elevator, claw);
        drive = new DTMecanum (turret);


        waitForStart();
        while (opModeIsActive()) {

            copilot.whilePressed(Controller.left, new Turn(turret, false));
            copilot.onReleased(Controller.left, new Stop(turret));

            copilot.whilePressed(Controller.right, new Turn(turret, true));
            copilot.onReleased(Controller.right, new Stop(turret));

            pilot.sticks(Controller.left_stick_x, Controller.left_stick_y, (FloatPair val) -> new Move(drive, val.firstValue(), val.secondValue()));
            pilot.stick(Controller.right_stick_x, (Float val) -> new org.firstinspires.ftc.team18613.commands.Drive.Turn(drive, val));
            pilot.trigger(Controller.right_trigger, (Float val) -> new Slow(drive, val));

            pilot.onPressed(Controller.a, new HorizontalColect(claw));
            pilot.onPressed(Controller.b, new LoweredFrontColect(claw));
            pilot.onPressed(Controller.x, new LoweredSideColect(claw));
            copilot.trigger(Controller.left_trigger, (Float val) -> new AngulationDrop(claw, val));
            copilot.onPressed(Controller.right_trigger_not_zero, new Drop(claw));
            copilot.onPressed(Controller.b, new Retract(claw));

            //*/
            copilot.onPressed(Controller.y, new ShiftStage(elevator, true));
            copilot.onPressed(Controller.x, new ShiftStage(elevator, false));

            copilot.onPressed(Controller.up, new AdjustStage(elevator, true));
            copilot.onPressed(Controller.down, new AdjustStage(elevator, false));


            elevator.removeControl();
            arm.removeControl();

            drive.periodic();
            claw.periodic();
            turret.periodic();
            arm.periodic();
            elevator.periodic();
            //claw.getTelemetry();
            telemetry.update();

/*
            elevator.Control(
                    gamepad2.y,
                    gamepad2.x,
                    gamepad2.dpad_up,
                    gamepad2.dpad_down
            );*/
/*
            claw.Control(
                    gamepad2.a,
                    gamepad1.a,
                    gamepad1.b,
                    gamepad1.x,
                    gamepad2.b,
                    gamepad2.right_trigger > 0,
                    gamepad2.left_trigger);


 */
            //arm.control();

            //drive.getTelemetry();


        }
    }
}
