/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {

    private LineSegment[] lineSegments = new LineSegment[1];

    public BruteCollinearPoints(Point[] points) {
        int pointer = 0;

        if (points == null) {
            throw new IllegalArgumentException();
        }

        // Minimum of 4 points required for a collinear line segment
        if (points.length < 4) {
            throw new IllegalArgumentException();
        }

        checkPoints(points);

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int p = k + 1; p < points.length; p++) {
                        Point a = points[i];
                        Point b = points[j];
                        Point c = points[k];
                        Point d = points[p];

                        if (pointer == this.lineSegments.length - 1) {
                            doubleSegementsArray();
                        }

                        if (a.slopeTo(b) == a.slopeTo(c) && a.slopeTo(b) == a.slopeTo(d)) {
                            Point[] segment = new Point[] { a, b, c, d };
                            Arrays.sort(segment);
                            LineSegment adLine = new LineSegment(segment[0], segment[3]);
                            this.lineSegments[pointer] = adLine;
                            pointer++;

                        }

                    }
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

    private void checkPoints(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i] == points[j]) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    public int numberOfSegments() {
        int numSegs = 0;
        for (int i = 0; i < this.lineSegments.length; i++) {
            if (this.lineSegments[i] != null) {
                numSegs++;
            }
        }
        return numSegs;
    }

    public LineSegment[] segments() {
        LineSegment[] copy = new LineSegment[numberOfSegments()];
        for (int i = 0; i < numberOfSegments(); i++) {
            copy[i] = this.lineSegments[i];
        }
        return copy;
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

        BruteCollinearPoints segmentFinder = new BruteCollinearPoints(points);
        StdOut.println(segmentFinder.numberOfSegments());
        for (int j = 0; j < segmentFinder.segments().length; j++) {
            StdOut.println(segmentFinder.segments()[j].toString());
        }
    }
}
