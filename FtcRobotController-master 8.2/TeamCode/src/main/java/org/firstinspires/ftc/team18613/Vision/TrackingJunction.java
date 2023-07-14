package org.firstinspires.ftc.team18613.Vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrackingJunction extends OpenCvPipeline {

    Mat processFrame, matTrash, box;
    Scalar lowFilter, highFilter;
    ArrayList<MatOfPoint> contours, boxCont;
    RotatedRect rect;
    MatOfPoint element;
    double lastWidth = 0, width = 0;

    public TrackingJunction() {

        processFrame = new Mat();
        lowFilter = new Scalar(100, 50, 30);
        highFilter = new Scalar(120, 360, 360);

        matTrash = new Mat();
        box = new Mat();
        contours = new ArrayList<>();
        boxCont = new ArrayList<>();
        rect = new RotatedRect();
    }

    @Override
    public Mat processFrame(Mat originalFrame) {

        Imgproc.cvtColor(originalFrame, processFrame, Imgproc.COLOR_BGR2HSV);
        Core.inRange(processFrame, lowFilter, highFilter, processFrame);

        matTrash = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
        Imgproc.morphologyEx(processFrame, processFrame, Imgproc.MORPH_OPEN, matTrash);
        Imgproc.GaussianBlur(processFrame, processFrame, new Size(3,3), 10);

        Imgproc.findContours(processFrame, contours, matTrash, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        if (contours.size() > 0) {
            for (MatOfPoint cont : contours) {
                rect = Imgproc.minAreaRect(new MatOfPoint2f(cont.toArray()));
                width = Math.min(rect.size.width, rect.size.height);

                if (width > 50 && width > lastWidth) {
                    lastWidth = width;
                    element = cont;
                }

            }

            if (element != null) {
                rect = Imgproc.minAreaRect(new MatOfPoint2f(element.toArray()));
                Imgproc.boxPoints(rect, box);
                //boxCont.add({new MatOfPoint(new Point(bo)));
               //mgproc.drawContours(processFrame, Imgproc.poibox, 0, new Scalar(0,0,255), 2);
                //boxCont.clear();

            }
        }
        return processFrame;
    }

}


