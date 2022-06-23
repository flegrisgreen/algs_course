import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private final int rowLength;
    private final int gridSize;
    private int numOpen = 0;
    private final WeightedQuickUnionUF quf;
    private int[] openTopRow;
    private int openTop = 0;
    private int[] openBottomRow;
    private int openBottom = 0;

    // Use a 2D array?

    public Percolation(int n) {

        if (n < 1) {
            throw new IllegalArgumentException("n must be larger than 0");
        }

        rowLength = n;
        gridSize = n * n;
        grid = new boolean[n][n];
        quf = new WeightedQuickUnionUF(gridSize);
        openTopRow = new int[n];
        openBottomRow = new int[n];
    }

    public void open(int row, int col) {

        if (this.isOpen(row, col)) {
            return;
        }

        int[] mappedVals = this.getMappedVals(row, col);
        int entry = mappedVals[0];
        row = mappedVals[1];
        col = mappedVals[2];

        if (entry >= gridSize) {
            return;
        }

        grid[row][col] = true;
        numOpen++;

        // Check for adjacent open columns
        // left
        if (col != 0) {
            if (grid[row][col - 1]) {
                quf.union(entry, entry - 1);
            }
        }
        // right
        if (col != (rowLength - 1)) {
            if (grid[row][col + 1]) {
                quf.union(entry, entry + 1);
            }
        }

        // Check for adjacent open rows
        // above
        if (row != 0) {
            if (grid[row - 1][col]) {
                quf.union(entry, entry - rowLength);
            }
        }
        else {
            openTopRow[openTop] = col + 1; // Non-mapped col value
            openTop++;
        }

        // below
        if (row != (rowLength - 1)) {
            if (grid[row + 1][col]) {
                quf.union(entry, entry + rowLength);
            }
        }
        else {
            openBottomRow[openBottom] = col + 1; // Non-mapped col value
            openBottom++;
        }
    }

    public boolean isOpen(int row, int col) {

        if (row < 1 || row > rowLength) {
            throw new IllegalArgumentException("row value out of bound in open()");
        }
        if (col < 1 || col > rowLength) {
            throw new IllegalArgumentException("row value out of bound in open()");
        }

        int[] mappedVals = this.getMappedVals(row, col);
        row = mappedVals[1];
        col = mappedVals[2];

        if (grid[row][col]) {
            return true;
        }
        return false;
    }

    public boolean isFull(int row, int col) {

        if (!this.isOpen(row, col)) {
            return false;
        }

        int[] mappedVals = this.getMappedVals(row, col);
        int entry = mappedVals[0];

        // only initialised array indices can be 0 since openTopRow takes col + 1 in this.open()
        for (int i = 0; this.openTopRow[i] != 0; i++) {
            if (quf.find(i) == quf.find(entry))
                return true;
        }
        return false;
    }

    public int numberOfOpenSites() {
        return numOpen;
    }

    public boolean percolates() {
        for (int j = 0; this.openBottomRow[j] != 0; j++) {
            if (this.isFull(rowLength, j + 1)) {
                return true;
            }
        }
        return false;
    }

    private int[] getMappedVals(int row, int col) {
        row = row - 1;
        col = col - 1;
        return new int[] { (rowLength * row + col), row, col };
    }

    public static void main(String[] args) {
        // Only used during development
        Percolation perc = new Percolation(10);
        perc.open(1, 2);
        perc.open(10, 2);
        for (int i = 1; i <= perc.rowLength; i++) {
            perc.open(i, 1);
            StdOut.println("isOpen: " + perc.isOpen(i, 1));
            StdOut.println("isFull: " + perc.isFull(i, 1));
            StdOut.println("isOpen: " + perc.isOpen(i, 2));
            StdOut.println("isFull: " + perc.isFull(i, 2));
            StdOut.println("Percolates: " + perc.percolates());
            for (int j = 0; j < perc.rowLength; j++) {
                for (int k = 0; k < perc.rowLength; k++) {
                    StdOut.print(perc.grid[j][k] + " ");
                }
                StdOut.print("\n");
            }
            StdOut.println("");
        }
        StdOut.println(perc.openTop);
        StdOut.println(perc.openBottom);
    }
}
