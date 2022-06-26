/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Arrays;

public class FastCollinearPoints {

    private LineSegment[] lineSegments = new LineSegment[1];
    private int numOfSegments = 0;

    public FastCollinearPoints(Point[] points) {

        if (points == null) {
            throw new IllegalArgumentException();
        }

        // Check for null points
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Null points not allowed");
            }
        }

        // Check for equal points
        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        for (int i = 0; i < points.length - 1; i++) {
            if (sortedPoints[i].compareTo(sortedPoints[i + 1]) == 0) {
                throw new IllegalArgumentException("Duplicate points not allowed");
            }
        }

        if (points.length < 4) {
            return;
        }

        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            if (i != 0) Arrays.sort(sortedPoints, p.slopeOrder());
            int startPointer = 0;
            int endPointer = 0;
            double slope = p.slopeTo(sortedPoints[startPointer]);
            for (int j = 1; j < sortedPoints.length; j++) {
                if (Double.compare(p.slopeTo(sortedPoints[j]), slope) == 0) {
                    endPointer++;
                    if (j == sortedPoints.length - 1) {
                        this.addSegment(sortedPoints, startPointer, endPointer,
                                        p); // add segment in last iteration
                    }
                }
                else {
                    this.addSegment(sortedPoints, startPointer, endPointer, p);
                    startPointer = j;
                    slope = p.slopeTo(sortedPoints[startPointer]);
                    endPointer = j;
                }

            }
        }
    }

    private void addSegment(Point[] sortedPoints, int startPointer, int endPointer, Point p) {

        if (this.numOfSegments == this.lineSegments.length) {
            doubleSegementsArray();
        }

        if (endPointer - startPointer > 1) {

            Point[] segmentPoints = slicedArray(sortedPoints, startPointer, endPointer,
                                                p);
            Arrays.sort(segmentPoints);
            LineSegment segment = new LineSegment(segmentPoints[0],
                                                  segmentPoints[segmentPoints.length
                                                          - 1]);
            if (!isDuplicate(segment)) {
                this.lineSegments[this.numOfSegments] = segment;
                numOfSegments++;
            }
        }
    }

    private void doubleSegementsArray() {
        LineSegment[] copy = new LineSegment[this.lineSegments.length * 2];
        for (int i = 0; i < this.lineSegments.length; i++) {
            copy[i] = this.lineSegments[i];
        }
        this.lineSegments = copy;
    }

    private Point[] slicedArray(Point[] points, int start, int end, Point p) {
        Point[] slice = new Point[(end - start) + 2];
        int val = 0;
        for (int i = start; i < end + 1; i++) {
            slice[val] = points[i];
            val++;
        }
        slice[slice.length - 1] = p;
        return slice;
    }

    private boolean isDuplicate(LineSegment segment) {
        for (int i = 0; i < this.numOfSegments; i++) {
            if (this.lineSegments[i].toString().equals(segment.toString())) {
                return true;
            }
        }
        return false;
    }

    public int numberOfSegments() {
        return this.numOfSegments;
    }

    public LineSegment[] segments() {
        LineSegment[] copy = new LineSegment[numberOfSegments()];
        for (int i = 0; i < numberOfSegments(); i++) {
            copy[i] = this.lineSegments[i];
        }
        return copy;
    }

    public static void main(String[] args) {

        int xval = 20;
        int yval = 20;
        Point[] points = new Point[xval * yval];

        int entry = 0;
        for (int k = 0; k < xval; k++) {
            for (int j = 0; j < yval; j++) {
                points[entry] = new Point(k, j);
                // StdOut.print(points[entry] + " ");
                entry++;
            }
            // StdOut.println("\n");
        }
        Stopwatch time = new Stopwatch();
        FastCollinearPoints segmentFinder = new FastCollinearPoints(points);
        StdOut.println(time.elapsedTime());
        StdOut.println(segmentFinder.numberOfSegments());
        // for (int j = 0; j < segmentFinder.numOfSegments; j++) {
        //     StdOut.println(segmentFinder.segments()[j].toString());
        // }
    }
}
