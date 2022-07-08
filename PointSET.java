import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

public class PointSET {

    private SET<Point2D> set;

    public PointSET() {
        set = new SET<>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {

        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (!set.contains(p)) {
            set.add(p);
        }
    }

    public boolean contains(Point2D p) {

        if (p == null) {
            throw new IllegalArgumentException();
        }

        return set.contains(p);
    }

    public void draw() {
        StdDraw.setPenRadius(0.01);
        set.forEach(p -> StdDraw.point(p.x(), p.y()));
    }

    public Iterable<Point2D> range(RectHV rect) {

        if (rect == null) {
            throw new IllegalArgumentException();
        }

        List<Point2D> range = new ArrayList<>();
        set.forEach(p -> {
            if (p.x() < rect.xmax() && p.x() > rect.xmin()) {
                if (p.y() < rect.ymax() && p.y() > rect.ymin()) {
                    range.add(p);
                }
            }
        });
        return range;
    }

    public Point2D nearest(Point2D p) {

        if (p == null) {
            throw new IllegalArgumentException();
        }

        Point2D floor = set.floor(p);
        Point2D ceiling = set.ceiling(p);
        if (p.distanceTo(floor) > p.distanceTo(ceiling)) {
            return ceiling;
        }
        return floor;
    }

    public static void main(String[] args) {
        PointSET points = new PointSET();
        StdOut.println("Empty: " + points.isEmpty());
        for (int i = 0; i < 20; i++) {
            double x = StdRandom.uniform();
            double y = StdRandom.uniform();
            points.insert(new Point2D(x, y));
        }
        StdOut.println("Empty: " + points.isEmpty());
        points.draw();
    }
}
