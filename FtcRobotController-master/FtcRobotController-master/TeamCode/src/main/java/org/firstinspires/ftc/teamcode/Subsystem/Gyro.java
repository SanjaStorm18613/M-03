package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class Gyro {

    BNO055IMU imu;

    double anguloAtual, anguloAnterior;
    int    revoluecoes;


    public Gyro(HardwareMap hwM){

        BNO055IMU.Parameters GYRO_imu_parameters;

        imu = hwM.get(BNO055IMU.class, "imu2");

        GYRO_imu_parameters                = new BNO055IMU.Parameters();
        GYRO_imu_parameters.mode           = BNO055IMU.SensorMode.GYRONLY;
        GYRO_imu_parameters.angleUnit      = BNO055IMU.AngleUnit.DEGREES;
        GYRO_imu_parameters.loggingEnabled = false;

        imu.initialize(GYRO_imu_parameters);

        anguloAnterior = 0;
        revoluecoes    = 0;
    }

    public double getContinuousAngle() {

        anguloAtual = getPeriodicAngle();

        if (180 < Math.abs(anguloAnterior - anguloAtual)) {

            if (anguloAnterior > anguloAtual) revoluecoes++;
            else revoluecoes--;

        }
        anguloAnterior = anguloAtual;

        return revoluecoes * 360 + anguloAtual;

    }

    public double getPeriodicAngle(){

        return -imu.getAngularOrientation(AxesReference
                .INTRINSIC, AxesOrder
                .ZYX, AngleUnit
                .DEGREES)
                .firstAngle;

    }

}
