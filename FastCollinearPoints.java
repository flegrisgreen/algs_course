/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    private LineSegment[] lineSegments = new LineSegment[1];
    private int numOfSegments = 0;

    public FastCollinearPoints(Point[] points) {

        if (points == null) {
            throw new IllegalArgumentException();
        }

        if (points.length < 4) {
            throw new IllegalArgumentException();
        }

        Point[] sortedPoints = points.clone();
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            Arrays.sort(sortedPoints, p.slopeOrder());
            int startPointer = 0;
            int endPointer = 1;
            double slope = p.slopeTo(sortedPoints[startPointer]);
            for (int j = 1; j < sortedPoints.length; j++) {
                if (p.slopeTo(sortedPoints[j]) == slope) {
                    endPointer++;
                }
                else {
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
                    startPointer = j - 1;
                    slope = p.slopeTo(sortedPoints[startPointer]);
                    endPointer = j;

                }

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
        return this.lineSegments.clone();
    }

    public static void main(String[] args) {
        Point a = new Point(2, 2);
        Point b = new Point(3, 3);
        Point c = new Point(4, 4);
        Point d = new Point(5, 5);
        Point e = new Point(4, 2);
        Point f = new Point(2, 4);
        Point g = new Point(1, 5);
        Point h = new Point(2, 5);
        Point i = new Point(1, 3);

        Point[] points = new Point[] { a, b, c, d, e, f, g, h, i };

        FastCollinearPoints segmentFinder = new FastCollinearPoints(points);
        StdOut.println(segmentFinder.numberOfSegments());
        for (int j = 0; j < segmentFinder.numOfSegments; j++) {
            StdOut.println(segmentFinder.segments()[j].toString());
        }
    }
}
