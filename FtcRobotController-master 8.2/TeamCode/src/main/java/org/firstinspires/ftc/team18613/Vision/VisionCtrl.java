package org.firstinspires.ftc.team18613.Vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvWebcam;


public class VisionCtrl {

    HardwareMap hardwareMap;
    //OpenCvWebcam webcam;
    OpenCvCamera webcam;
    PipelineColors detectorAuto;
    TrackingJunction detectorTele;
    LinearOpMode opMode;
    Telemetry telemetry;
    boolean pipelineAuto;


    public VisionCtrl(LinearOpMode opM, HardwareMap hw, Telemetry t, boolean pipelineAuto){

        opMode = opM;
        telemetry = t;
        hardwareMap = hw;

        this.pipelineAuto = pipelineAuto;

        int cameraMonitorViewId = opMode.hardwareMap
                                .appContext .getResources()
                                .getIdentifier("cameraMonitorViewId"
                                                , "id"
                                                , hardwareMap.appContext.getPackageName());

        /*webcam = OpenCvCameraFactory.getInstance().createWebcam(
                                        hardwareMap.get
                                        (WebcamName.class, "Webcam 1")
                                        , cameraMonitorViewId);*/
 //*/
        webcam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        if (pipelineAuto) {
            detectorAuto = new PipelineColors();

        } else {
            detectorTele = new TrackingJunction();

        }
        //detectorTele = new TrackingJunction();
        //detectorAuto = new PipelineColors();

        initDetectionElement();
    }

    private void initDetectionElement() {

        //webcam.setMillisecondsPermissionTimeout(2500);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {

            @Override
            public void onOpened() {
                webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
                /*
                ExposureControl ec = webcam.getExposureControl();
                ec.setMode(ExposureControl.Mode.Manual);
                ec.setExposure(1, TimeUnit.MILLISECONDS);

                 */
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Error while opening camera: ", errorCode);
            }
        });

        if (pipelineAuto) {
            webcam.setPipeline(detectorAuto);

        } else {
            webcam.setPipeline(detectorTele);

        }

        //webcam.setPipeline(detectorTele);
        //webcam.setPipeline(detectorAuto);

    }

    public void stopDetection(){
        webcam.stopStreaming();

    }

    public PipelineColors getPipelineAuto() {
        return detectorAuto;
    }

    public TrackingJunction getPipelineTele() {
        return detectorTele;
    }
}
