package org.firstinspires.ftc.team18613.Vision;

import android.graphics.Camera;
import android.graphics.ImageFormat;

import org.firstinspires.ftc.team18613.Constants;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import java.util.ArrayList;

public class TrackingJunction extends OpenCvPipeline {

    private final Mat processFrame;
    private Mat mat;

    private Scalar lowFilter, highFilter;
    private RotatedRect rotatedRect;

    private final Point[] box;
    private Point midPoint;

    private final ArrayList<MatOfPoint> contours, boxCont;
    private double centerJunction = 0, lastCenterJunction = 0;
    private boolean streamingFilter = false, detected = false;

    public TrackingJunction() {

        processFrame = new Mat();
        lowFilter = new Scalar(Constants.Pipeline.TELE_COLOR_LOW);
        highFilter = new Scalar(Constants.Pipeline.TELE_COLOR_UP);

        mat = new Mat();
        box = new Point[4];
        midPoint = new Point(0,0);
        contours = new ArrayList<>();
        boxCont = new ArrayList<>();
        rotatedRect = new RotatedRect();
    }

    @Override
    public Mat processFrame(Mat originalFrame) {

        Imgproc.cvtColor(originalFrame, processFrame, Imgproc.COLOR_BGR2HLS);
        Core.inRange(processFrame, lowFilter, highFilter, processFrame);

        mat = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(20,20));
        Imgproc.morphologyEx(processFrame, processFrame, Imgproc.MORPH_OPEN, mat);

        mat = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10,10));
        Imgproc.erode(processFrame, processFrame, mat);

        mat = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(15,15));
        Imgproc.morphologyEx(processFrame, processFrame, Imgproc.MORPH_OPEN, mat);

        mat = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5));
        Imgproc.morphologyEx(processFrame, processFrame, Imgproc.MORPH_CLOSE, mat);

        mat = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(20,20));
        Imgproc.dilate(processFrame, processFrame, mat);

        contours.clear();
        Imgproc.findContours(processFrame, contours, mat, Imgproc.RETR_LIST,
                                                                    Imgproc.CHAIN_APPROX_SIMPLE);
        mat.release();

        if (!streamingFilter) {

            originalFrame.copyTo(processFrame);

            double lastWidth = 0, height;
            midPoint = new Point(0, 0);
            MatOfPoint element = null;

            if (contours.size() > 0) {
                for (MatOfPoint cont : contours) {
                    if (Imgproc.contourArea(cont) > 1000) {
                        height = Imgproc.boundingRect(cont).height;
                        rotatedRect = Imgproc.minAreaRect(new MatOfPoint2f(cont.toArray()));
                        double realWidth = Math.min(rotatedRect.size.width, rotatedRect.size.height);
                        double realHeight = Math.max(rotatedRect.size.width, rotatedRect.size.height);

                        if (realWidth > 40 && realWidth > lastWidth && height > 300 && realWidth * 2 <= realHeight) {
                            lastWidth = realWidth;
                            element = cont;
                        }
                    }

                }

                if (element != null) {
                    detected = true;

                    rotatedRect = Imgproc.minAreaRect(new MatOfPoint2f(element.toArray()));
                    rotatedRect.points(box);
                    boxCont.clear();
                    boxCont.add(new MatOfPoint(box));
                    Imgproc.drawContours(processFrame, boxCont, 0, new Scalar(0, 0, 255), 2);

                    if (rotatedRect.size.width > rotatedRect.size.height) {
                        midPoint = new Point((box[0].x + box[1].x) / 2, (box[0].y + box[1].y) / 2);
                    } else {
                        midPoint = new Point((box[1].x + box[2].x) / 2, (box[1].y + box[2].y) / 2);
                    }

                    Imgproc.circle(processFrame, midPoint, 5, new Scalar(0, 255, 0), -1);
                    centerJunction = Math.signum(midPoint.x) * (Math.abs(midPoint.x) + Math.abs(lastCenterJunction)) / 2.;
                    lastCenterJunction = centerJunction;

                } else {
                    detected = false;
                    centerJunction = 0;
                }
            }
        }
        return processFrame;
    }

    public double getCenterJunction() {
        return centerJunction;
    }

    public boolean getDetected() {
        return detected;
    }

    /*public double getArea(){
        return 0;
    }*/

    public void setFilter(double[][] newFilter) {
        lowFilter = new Scalar(newFilter[0]);
        highFilter = new Scalar(newFilter[1]);

    }

    public void setStreaming(boolean streamingFilter) {
        this.streamingFilter = streamingFilter;
    }

}


