import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private Node root;
    private List<Node> treeList;

    public KdTree() {
        root = null;
        treeList = new ArrayList<>();
    }

    private class Node {
        Point2D thisPoint;
        boolean key; // false = x, true = y
        Node smallChild;
        Node largeChild;
        Node parent;
        int rank;

        public Node(Point2D thisPoint, Node parent) {
            this.thisPoint = thisPoint;
            this.parent = parent;
            if (parent != null) {
                this.key = !parent.key;
            }
            else {
                this.key = false; // First node start with x comparison
            }
            this.smallChild = null;
            this.largeChild = null;
            rank = 1; // only this point
        }
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        if (root == null) {
            return 0;
        }
        return root.rank;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        this.insertNode(p, root);
    }

    private boolean insertNode(Point2D p, Node currentNode) {
        if (root == null) {
            root = new Node(p, null);
            root.rank = 1;
            treeList.add(root);
            return true; // Needs to return a value for recursion to work
        }
        // Increase current node rank
        currentNode.rank++;
        // Compare y-coordinates
        if (currentNode.key) {
            // Insert to the right
            if (p.y() > currentNode.thisPoint.y()) {
                if (currentNode.largeChild == null) {
                    currentNode.largeChild = new Node(p, currentNode);
                    treeList.add(currentNode.largeChild);
                    return true;
                }
                else return insertNode(p, currentNode.largeChild);
            }
            // Insert to the left
            else {
                if (currentNode.smallChild == null) {
                    currentNode.smallChild = new Node(p, currentNode);
                    treeList.add(currentNode.smallChild);
                    return true;
                }
                else return insertNode(p, currentNode.smallChild);
            }
        }
        // Compare x-coordinates
        else {
            // Insert to the right
            if (p.x() > currentNode.thisPoint.x()) {
                if (currentNode.largeChild == null) {
                    currentNode.largeChild = new Node(p, currentNode);
                    treeList.add(currentNode.largeChild);
                    return true;
                }
                else return insertNode(p, currentNode.largeChild);
            }
            // Insert to the left
            else {
                if (currentNode.smallChild == null) {
                    currentNode.smallChild = new Node(p, currentNode);
                    treeList.add(currentNode.smallChild);
                    return true;
                }
                else return insertNode(p, currentNode.smallChild);
            }
        }
    }

    public boolean contains(Point2D p) {
        // Make use of kdtree search to find p (not the built-in contains method)
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Node currentNode = root;
        boolean nodeFound = false;
        while (true) {

            if (currentNode.thisPoint.equals(p)) {
                nodeFound = true;
                break;
            }

            // Compare y
            if (currentNode.key) {
                // Search right
                if (p.y() > currentNode.thisPoint.y()) {
                    if (currentNode.largeChild == null) {
                        break;
                    }
                    else {
                        currentNode = currentNode.largeChild;
                    }
                    // Search left
                }
                else {
                    if (currentNode.smallChild == null) {
                        break;
                    }
                    else {
                        currentNode = currentNode.smallChild;
                    }
                }
            }
            // Compare x
            else {
                // Search right
                if (p.x() > currentNode.thisPoint.x()) {
                    if (currentNode.largeChild == null) {
                        break;
                    }
                    else {
                        currentNode = currentNode.largeChild;
                    }
                    // Search left
                }
                else {
                    if (currentNode.smallChild == null) {
                        break;
                    }
                    else {
                        currentNode = currentNode.smallChild;
                    }
                }
            }
        }
        return nodeFound;
    }

    public void draw() {
        // Todo: The lines are not correct yet. They do not stop at parent x or y vertices
        treeList.forEach(n -> {
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(0, 0, 0);
            StdDraw.point(n.thisPoint.x(), n.thisPoint.y());
            if (n.key) { // y partition - blue line
                StdDraw.setPenRadius(0.001);
                StdDraw.setPenColor(4, 118, 208);
                if (n.thisPoint.x() > n.parent.thisPoint.x()) { // This point is above
                    StdDraw.line(n.parent.thisPoint.x(), n.thisPoint.y(), 1, n.thisPoint.y());
                }
                else {
                    StdDraw.line(0, n.thisPoint.y(), n.parent.thisPoint.x(), n.thisPoint.y());
                }
            }
            else { // x partition - red line
                StdDraw.setPenRadius(0.001);
                StdDraw.setPenColor(255, 36, 0);
                if (n.parent == null) { // Root
                    StdDraw.line(n.thisPoint.x(), 0, n.thisPoint.x(), 1);
                }
                else if (n.thisPoint.y() > n.parent.thisPoint.y()) { // This point is above
                    StdDraw.line(n.thisPoint.x(), n.parent.thisPoint.y(), n.thisPoint.x(), 1);
                }
                else {
                    StdDraw.line(n.thisPoint.x(), 0, n.thisPoint.x(), n.parent.thisPoint.y());
                }
            }
        });
    }

    public Iterable<Point2D> range(RectHV rect) {

        if (rect == null) {
            throw new IllegalArgumentException();
        }

        List<Point2D> inRange = new ArrayList<>();
        Stack<Node> toExploreRight = new Stack<>();
        Node currentNode = root;
        while (true) {

            boolean exploreleftBranch = false;
            boolean explorerightBranch = false;

            // Point is in range and both subtrees must be searched
            if (checkInRange(currentNode, rect)) {
                inRange.add(currentNode.thisPoint);
                exploreleftBranch = true;
                explorerightBranch = true;
            }
            else if (currentNode.key) {
                if (yInRange(currentNode, rect)) {
                    exploreleftBranch = true;
                    explorerightBranch = true;
                }
                else if (rect.ymin() > currentNode.thisPoint.y()) {
                    explorerightBranch = true;
                }
                else {
                    exploreleftBranch = true;
                }

            }
            else {
                if (xInRange(currentNode, rect)) {
                    exploreleftBranch = true;
                    explorerightBranch = true;
                }
                else if (rect.xmin() > currentNode.thisPoint.x()) {
                    explorerightBranch = true;
                }
                else {
                    exploreleftBranch = true;
                }
            }


            // Go left and push to stack
            if (exploreleftBranch && currentNode.smallChild != null) {
                if (explorerightBranch && currentNode.largeChild != null) {
                    toExploreRight.push(currentNode.largeChild);
                }
                currentNode = currentNode.smallChild;
            }
            // Left = null, go right (do not push to stack)
            else if (explorerightBranch && currentNode.largeChild != null) {
                currentNode = currentNode.largeChild;
            }
            // Left and right null -> pop from stack
            else {
                if (!toExploreRight.isEmpty()) {
                    currentNode = toExploreRight.pop();
                }
                else {
                    break;
                }
            }
        }
        return inRange;
    }

    private boolean checkInRange(Node currentNode, RectHV rect) {
        if (xInRange(currentNode, rect)) {
            if (yInRange(currentNode, rect)) {
                return true;
            }
        }
        return false;
    }

    private boolean xInRange(Node currentNode, RectHV rect) {
        if (currentNode.thisPoint.x() > rect.xmin() && currentNode.thisPoint.x() < rect.xmax()) {
            return true;
        }
        return false;
    }

    private boolean yInRange(Node currentNode, RectHV rect) {
        if (currentNode.thisPoint.y() > rect.ymin() && currentNode.thisPoint.y() < rect.ymax()) {
            return true;
        }
        return false;
    }

    public Point2D nearest(Point2D p) {

        if (this.root == null) {
            return null;
        }
        if (p == null) {
            throw new IllegalArgumentException();
        }

        Node currentNode = root;
        Node closest = root;
        double closestDistance = p.distanceTo(closest.thisPoint);
        while (true) {
            boolean exploreleftBranch = false;
            boolean explorerightBranch = false;
            if (p.equals(currentNode.thisPoint)) {
                return currentNode.thisPoint;
            }

            // Check if current is closer than closest
            if (p.distanceTo(currentNode.thisPoint) < closestDistance) {
                // continue
            }
            // Check left and right, but first check right if p>current , else check left first
            // If new closest point current branch, do not explore other branch
            // If reached null nodes -> check what is still in queue (if any)
            // return closest
        }

    }

    public static void main(String[] args) {
        KdTree points = new KdTree();
        StdOut.println("Empty: " + points.isEmpty());
        StdOut.println("Size: " + points.size());
        for (int i = 0; i < 30; i++) {
            double x = StdRandom.uniform();
            double y = StdRandom.uniform();
            points.insert(new Point2D(x, y));
        }
        StdOut.println("Empty: " + points.isEmpty());
        StdOut.println("Size: " + points.size());
        RectHV range = new RectHV(0.4, 0.4, 0.7, 0.5);
        Iterable<Point2D> inRange = points.range(range);
        StdDraw.rectangle(0.55, 0.45, 0.15, 0.05);
        StdOut.println("All points");
        points.treeList.forEach(p -> StdOut.println(p.thisPoint));
        StdOut.println("Points in range");
        inRange.forEach(p -> StdOut.println(p));
        points.draw();
    }
}
