package org.firstinspires.ftc.team18613.Vision;

import org.firstinspires.ftc.team18613.Constants;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;

public class PipelineColors extends OpenCvPipeline {

    private final Mat input;
    private final Mat mat2;
    private Mat cropOrg;

    private final Size s;
    private MatOfPoint objectDetection;
    private Scalar low0, up0, low1, up1;
    private final Scalar green, red;

    private final ArrayList<ArrayList<MatOfPoint>> elementsArr;
    private final ArrayList<MatOfPoint> contourArr;

    private int ObjectDetectionColor = -1;
    private boolean streamingFilter = false;


    public PipelineColors() {

        mat2 = new Mat();
        input = new Mat();
        cropOrg = new Mat();

        green = new Scalar(0, 255, 0);
        red = new Scalar(255, 0, 0);

        s = new Size(3, 3);
        elementsArr = new ArrayList<>();
        contourArr = new ArrayList<>();

        low0 = new Scalar(Constants.Pipeline.AUTO_COLOR_LOW[0]);
        up0 = new Scalar(Constants.Pipeline.AUTO_COLOR_UP[0]);//green

        low1 = new Scalar(Constants.Pipeline.AUTO_COLOR_LOW[1]);
        up1 = new Scalar(Constants.Pipeline.AUTO_COLOR_UP[1]);//yellow
    }


    @Override
    public Mat processFrame(Mat originalInput) {
        Point center = new Point(100,160);

        cropOrg = new Mat(originalInput, new Rect(70,100,160,120));

        Imgproc.cvtColor(cropOrg, input, Imgproc.COLOR_BGR2HLS);

        elementsArr.clear();

        elementsArr.add(colorFilter(low0, up0));
        elementsArr.add(colorFilter(low1, up1));

        if (!streamingFilter) {
            cropOrg.copyTo(input);

            ObjectDetectionColor = -1;
            objectDetection = null;
            ArrayList<MatOfPoint> contourObjectDetect = null;

            int i = 0;
            for (ArrayList<MatOfPoint> elms : elementsArr) {

                for (MatOfPoint item : elms) {

                    if (objectDetection == null || (Imgproc.contourArea(item)
                            > Imgproc.contourArea(objectDetection))) {
                        ObjectDetectionColor = i;
                        objectDetection = item;
                        contourObjectDetect = elms;
                    }
                }
                i++;
            }

            if (objectDetection != null && Imgproc.contourArea(objectDetection)
                    > Constants.Pipeline.TOLERANCE_AREA) {

                Rect rectRange = Imgproc.boundingRect(objectDetection);
                Point supDir = new Point(rectRange.x, rectRange.y);
                Point botEsc = new Point(rectRange.x + rectRange.width,
                        rectRange.y + rectRange.height);


                Imgproc.rectangle(input, supDir, botEsc, green, 1);
                Imgproc.drawContours(input, contourObjectDetect, contourObjectDetect.indexOf(objectDetection), red, 2);
                Imgproc.putText(input, getColorDetected().toString(), supDir, Imgproc.FONT_HERSHEY_PLAIN,1, green, 2);

            } else {
                Imgproc.putText(input, "NOT FOUND/BLACK", center, Imgproc.FONT_HERSHEY_PLAIN,
                        1, green, 2);
            }

        }

        Imgproc.resize(input, input, originalInput.size());
        return input;
    }

    public ArrayList<MatOfPoint> colorFilter(Scalar low, Scalar up) {

        Core.inRange(input, low, up, input);

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, s);

        Imgproc.morphologyEx(input, input, Imgproc.MORPH_OPEN, kernel);
        Imgproc.GaussianBlur(input, input, s, 10);

        contourArr.clear();
        Imgproc.findContours(input, contourArr, mat2, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);

        mat2.release();
        return(ArrayList<MatOfPoint>) contourArr.clone();
    }

    public DetectionColor getColorDetected() {

        if (objectDetection != null && Imgproc.contourArea(objectDetection)
                                                            > Constants.Pipeline.TOLERANCE_AREA) {
            switch (ObjectDetectionColor) {
                case 0:
                    return DetectionColor.GREEN;
                case 1:
                    return DetectionColor.YELLOW;
                default:
                    return DetectionColor.BLACK;
            }
        } else {
            return DetectionColor.BLACK;
        }
    }

    public void setFilter(double[][][] newFilter) {
        low0 = new Scalar(newFilter[0][0]);
        up0 = new Scalar(newFilter[1][0]);

        low1 = new Scalar(newFilter[0][1]);
        up1 = new Scalar(newFilter[1][1]);

    }

    public void setStreaming(boolean streamingFilter) {
        this.streamingFilter = streamingFilter;
    }

    public enum DetectionColor {
        GREEN,
        YELLOW,
        BLACK
    }
}


