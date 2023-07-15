package org.firstinspires.ftc.team18613;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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

@TeleOp(name = "TeleTracking", group = "Linear Opmode")

public class TeleTracking extends LinearOpMode {

    VisionCtrl webcam;
    //TrackingJunction detector = webcam.getPipelineTele();

    public void runOpMode() {

        webcam = new VisionCtrl(this, hardwareMap, telemetry, false);

        waitForStart();

        while (opModeIsActive()) {
            webcam.stopDetection();

        }
    }
}
