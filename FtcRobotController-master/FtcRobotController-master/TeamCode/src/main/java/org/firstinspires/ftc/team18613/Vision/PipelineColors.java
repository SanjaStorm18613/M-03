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

    double[][] cUp = Constants.Pipeline.COLOR_UP,
            cLw = Constants.Pipeline.COLOR_LOW;

    int tolerance = Constants.Pipeline.TOLERANCE_AREA;

    Scalar low0, up0, low1, up1, green;
    Mat input, mat, mat2;
    Size s;
    MatOfPoint objtDetc;
    ArrayList<ArrayList<MatOfPoint>> elemsArr;
    ArrayList<MatOfPoint> contourArr;
    int ObjtDetcColor = -1;


    public PipelineColors() {

        mat = new Mat();
        mat2 = new Mat();
        input = new Mat();

        green = new Scalar(0, 255, 0);

        s = new Size(3, 3);
        elemsArr = new ArrayList<>();
        contourArr = new ArrayList<>();

        low0 = new Scalar(cLw[0][0], cLw[0][1], cLw[0][2]);
        up0 = new Scalar(cUp[0][0], cUp[0][1], cUp[0][2]);//green

        low1 = new Scalar(cLw[1][0], cLw[1][1], cLw[1][2]);
        up1 = new Scalar(cUp[1][0], cUp[1][1], cUp[1][2]);//cian
    }


    @Override
    public Mat processFrame(Mat orgmInpt) {
        Imgproc.cvtColor(orgmInpt, input, Imgproc.COLOR_BGR2HLS);

        elemsArr.clear();

        elemsArr.add(colorFilter(low0, up0));
        elemsArr.add(colorFilter(low1, up1));

        orgmInpt.copyTo(input);

        ObjtDetcColor = -1;
        objtDetc = null;

        int i = 0;
        for (ArrayList<MatOfPoint> elms : elemsArr) {

            for (MatOfPoint item : elms) {

                if (objtDetc == null ||(Imgproc.contourArea(item) > Imgproc.contourArea(objtDetc))) {
                    ObjtDetcColor = i;
                    objtDetc = item;
                }
            }
            i++;
        }


        if (objtDetc != null && Imgproc.contourArea(objtDetc) > tolerance) {

            Rect rectRange = Imgproc.boundingRect(objtDetc);
            Point supDir = new Point(rectRange.x, rectRange.y);
            Point botEsc = new Point(rectRange.x + rectRange.width,
                    rectRange.y + rectRange.height);
            Point org = new Point(0,0);

            Imgproc.rectangle(input, supDir, botEsc, green, 5);
            Imgproc.putText(input, getColorDetected().toString(), org, Imgproc.FONT_HERSHEY_PLAIN, 1, green, 2);
        }


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

    public double getcontourArea() {
        if (objtDetc == null) {
            return 0;
        }

        return Imgproc.contourArea(objtDetc);
    }

    public DetectionColor getColorDetected() {

        switch (ObjtDetcColor) {
            case 0:
                return DetectionColor.GREEN;
            case 1:
                return DetectionColor.CIAN;
            default:
                return DetectionColor.BLACK;
        }
    }

    public enum DetectionColor {
        GREEN,
        CIAN,
        BLACK
    }
}


