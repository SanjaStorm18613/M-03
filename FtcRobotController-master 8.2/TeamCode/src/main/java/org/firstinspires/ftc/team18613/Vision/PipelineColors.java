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

    Scalar low0, up0, low1, up1, green, red, black;
    Mat input, mat, mat2, cropOrg;
    Size s;
    MatOfPoint objectDetection;
    ArrayList<ArrayList<MatOfPoint>> elementsArr;
    ArrayList<MatOfPoint> contourArr, contourObjectDetect;
    int ObjectDetectionColor = -1;


    public PipelineColors() {

        mat = new Mat();
        mat2 = new Mat();
        input = new Mat();
        cropOrg = new Mat();

        green = new Scalar(0, 255, 0);
        red = new Scalar(255, 0, 0);
        black = new Scalar(0,0,0);

        s = new Size(3, 3);
        elementsArr = new ArrayList<>();
        contourArr = new ArrayList<>();

        low0 = new Scalar(Constants.Pipeline.AUTO_COLOR_LOW[0]);
        up0 = new Scalar(Constants.Pipeline.AUTO_COLOR_UP[0]);//green

        low1 = new Scalar(Constants.Pipeline.AUTO_COLOR_LOW[1]);
        up1 = new Scalar(Constants.Pipeline.AUTO_COLOR_UP[1]);//cian
    }


    @Override
    public Mat processFrame(Mat originalInput) {
        Point center = new Point(100,160);

        cropOrg = new Mat(originalInput, new Rect(140,200,360,280));

        Imgproc.cvtColor(cropOrg, input, Imgproc.COLOR_BGR2HLS);

        elementsArr.clear();

        elementsArr.add(colorFilter(low0, up0));
        elementsArr.add(colorFilter(low1, up1));

        cropOrg.copyTo(input);

        ObjectDetectionColor = -1;
        objectDetection = null;
        contourObjectDetect = null;

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
            if (contourObjectDetect != null) {
                Imgproc.drawContours(input, contourObjectDetect,
                                    contourObjectDetect.indexOf(objectDetection), red, 2);
            }
            Imgproc.putText(input, getColorDetected().toString(), supDir, Imgproc.FONT_HERSHEY_PLAIN,
                                                                    1, green, 2);
        } else {
            Imgproc.putText(input, "NOT FOUND/BLACK", center, Imgproc.FONT_HERSHEY_PLAIN,
                                                                    1, green, 2);
        }

        Imgproc.resize(input, input, originalInput.size());

        return input;
    }

    public ArrayList<MatOfPoint> colorFilter(Scalar low, Scalar up) {

        Core.inRange(input, low, up, mat);

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, s);

        Imgproc.morphologyEx(mat, mat, Imgproc.MORPH_OPEN, kernel);
        Imgproc.GaussianBlur(mat, mat, s, 10);

        contourArr.clear();
        Imgproc.findContours(mat, contourArr, mat2, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);

        mat.release();
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

    public enum DetectionColor {
        GREEN,
        YELLOW,
        BLACK
    }
}


