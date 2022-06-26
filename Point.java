/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {

        if (this.compareTo(that) == 0) {
            return Double.NEGATIVE_INFINITY;
        }
        if (this.x == that.x) {
            return Double.POSITIVE_INFINITY;
        }
        if (this.y == that.y) {
            return 0;
        }
        double y1 = that.y;
        double y0 = this.y;
        double x1 = that.x;
        double x0 = this.x;

        return (y1 - y0) / (x1 - x0);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point that) {
        if (this.y == that.y) {
            return Integer.compare(this.x, that.x);
        }
        else if (this.y > that.y) {
            return 1;
        }
        else {
            return -1;
        }
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new SortBySlope(this.x, this.y);
    }

    private class SortBySlope implements Comparator<Point> {

        private final Point thisPoint;

        private SortBySlope(int x, int y) {
            this.thisPoint = new Point(x, y);
        }

        public int compare(Point a, Point b) {
            double slope1 = this.thisPoint.slopeTo(a);
            double slope2 = this.thisPoint.slopeTo(b);

            return Double.compare(slope1, slope2);
        }
    }

    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        Point a = new Point(2, 2);
        Point b = new Point(3, 2); // Horizontal slope
        Point c = new Point(2, 1); // Vertical slope
        Point d = new Point(2, 2); // Same point
        Point e = new Point(3, 3); // Positive slope
        Point f = new Point(1, 3); // Negative slope

        Point[] points = new Point[] { a, b, c, d, e, f };
        for (int i = 0; i < points.length; i++) {
            StdOut.println(points[i]);
        }

        for (int i = 0; i < points.length; i++) {
            points[i].draw();
        }
        for (int i = 1; i < points.length; i++) {
            points[0].drawTo(points[i]);
        }

        StdOut.println("point comparison: ");
        for (int i = 1; i < points.length; i++) {
            StdOut.println(points[0].compareTo(points[i]));
        }

        StdOut.println("point slope: ");
        for (int i = 1; i < points.length; i++) {
            StdOut.println(points[0].slopeTo(points[i]));
        }

        StdOut.println("point slope order: ");
        for (int i = 1; i < points.length - 1; i = i + 2) {
            StdOut.println(points[0].slopeOrder().compare(points[i], points[i + 1]));
        }
    }
}
