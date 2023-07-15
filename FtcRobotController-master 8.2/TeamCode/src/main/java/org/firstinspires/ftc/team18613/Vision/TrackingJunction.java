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

public class TrackingJunction extends OpenCvPipeline {

    Mat processFrame, matTrash;
    Scalar lowFilter, highFilter;
    ArrayList<MatOfPoint> contours, boxCont;
    RotatedRect rect;
    MatOfPoint element;
    Point[] box;
    Point midPoint;
    double lastWidth, width = 0;

    public TrackingJunction() {

        processFrame = new Mat();
        lowFilter = new Scalar(40, 40, 50);
        highFilter = new Scalar(90, 180, 360);

        matTrash = new Mat();
        box = new Point[4];
        midPoint = new Point();
        contours = new ArrayList<>();
        boxCont = new ArrayList<>();
        rect = new RotatedRect();
    }

    @Override
    public Mat processFrame(Mat originalFrame) {

        Imgproc.cvtColor(originalFrame, processFrame, Imgproc.COLOR_BGR2HLS);
        Core.inRange(processFrame, lowFilter, highFilter, processFrame);

        matTrash = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
        Imgproc.morphologyEx(processFrame, processFrame, Imgproc.MORPH_OPEN, matTrash);
        Imgproc.GaussianBlur(processFrame, processFrame, new Size(3,3), 10);

        contours.clear();
        Imgproc.findContours(processFrame, contours, matTrash, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        matTrash.release();

        originalFrame.copyTo(processFrame);

        element = null;
        lastWidth = 0;
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
                rect.points(box);
                boxCont.clear();
                boxCont.add(new MatOfPoint(box));
                Imgproc.drawContours(processFrame, boxCont, 0, new Scalar(0,0,255), 2);

                if (rect.size.width > rect.size.height) {
                    midPoint = new Point((box[0].x + box[1].x)/2, (box[0].y + box[1].y)/2);
                } else {
                    midPoint = new Point((box[1].x + box[2].x)/2, (box[1].y + box[2].y)/2);
                }

                Imgproc.circle(processFrame, midPoint, 5, new Scalar(0, 255, 0), -1);

            }
        }
        return processFrame;
    }

}


