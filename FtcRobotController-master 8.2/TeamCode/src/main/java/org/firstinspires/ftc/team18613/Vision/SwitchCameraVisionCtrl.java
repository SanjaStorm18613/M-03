package org.firstinspires.ftc.team18613.Vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.concurrent.TimeUnit;


public class SwitchCameraVisionCtrl {

    HardwareMap hardwareMap;
    OpenCvWebcam webcamTele, webcamAuto;
    //OpenCvCamera webcam;
    LinearOpMode opMode;
    Telemetry telemetry;
    PipelineColors detectorAuto;
    TrackingJunction detectorTele;

    public SwitchCameraVisionCtrl(LinearOpMode opM, HardwareMap hw, Telemetry t){

        opMode = opM;
        telemetry = t;
        hardwareMap = hw;

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        int[] viewportContainerIds = OpenCvCameraFactory.getInstance()
                .splitLayoutForMultipleViewports(
                        cameraMonitorViewId, //The container we're splitting
                        2, //The number of sub-containers to create
                        OpenCvCameraFactory.ViewportSplitMethod.VERTICALLY);

        webcamAuto = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class,
                                                    "Webcam 1"), viewportContainerIds[0]);
        webcamTele = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class,
                                                    "Webcam 2"), viewportContainerIds[1]);

        //webcam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        initDetectionElement();
    }

    private void initDetectionElement() {

        detectorAuto = new PipelineColors();
        detectorTele = new TrackingJunction();

        //webcamAuto.setMillisecondsPermissionTimeout(2500);

        webcamAuto.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener(){
            @Override
            public void onOpened()
            {
                webcamAuto.setPipeline(detectorAuto);
                webcamAuto.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);

                /*ExposureControl ec = webcamAuto.getExposureControl();
                ec.setMode(ExposureControl.Mode.Manual);
                ec.setExposure(1, TimeUnit.MILLISECONDS);*/


            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Error while opening camera: ", errorCode);
            }
        });

        //webcamTele.setMillisecondsPermissionTimeout(2500);

        webcamTele.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener(){
            @Override
            public void onOpened()
            {
                webcamTele.setPipeline(detectorTele);
                webcamTele.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);

                /*ExposureControl ec = webcamTele.getExposureControl();
                ec.setMode(ExposureControl.Mode.Manual);
                ec.setExposure(1, TimeUnit.MILLISECONDS);*/


            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Error while opening camera: ", errorCode);
            }
        });

        /*webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {

            @Override
            public void onOpened() {
                webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
                *//*
                ExposureControl ec = webcam.getExposureControl();
                ec.setMode(ExposureControl.Mode.Manual);
                ec.setExposure(1, TimeUnit.MILLISECONDS);

                 *//*
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Error while opening camera: ", errorCode);
            }
        });

        detectorTele = new TrackingJunction();
        webcam.setPipeline(detectorTele);*/

    }

    public void stopDetection(){
        webcamAuto.stopStreaming();
        webcamTele.stopStreaming();
        //webcam.stopStreaming();

    }

    public PipelineColors getPipelineAuto(){
        return detectorAuto;
    }

    public TrackingJunction getPipelineTele(){
        return detectorTele;
    }
}
