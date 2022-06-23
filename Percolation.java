import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int[][] grid;
    private final int rowLength;
    private final int gridSize;
    private int numOpen = 0;
    private final WeightedQuickUnionUF quf;

    // Use a 2D array?

    public Percolation(int n) {

        if (n <= 0) {
            throw new IllegalArgumentException("n must be larger than 0");
        }

        rowLength = n;
        gridSize = n * n;
        grid = new int[n][n];
        quf = new WeightedQuickUnionUF(gridSize);
    }

    public void open(int row, int col) {

        if (this.isOpen(row, col)) {
            return;
        }

        row = row - 1;
        col = col - 1;
        int entry = rowLength * row + col;

        if (entry >= gridSize) {
            return;
        }

        grid[row][col] = 1;
        numOpen++;

        // Check for adjacent open columns
        // left
        if (col != 0) {
            if (grid[row][col - 1] == 1) {
                quf.union(entry, entry - 1);
            }
        }
        // right
        if (col != (rowLength - 1)) {
            if (grid[row][col + 1] == 1) {
                quf.union(entry, entry + 1);
            }
        }

        // Check for adjacent open rows
        // above
        if (row != 0) {
            if (grid[row - 1][col] == 1) {
                quf.union(entry, entry - rowLength);
            }
        }
        // below
        if (row != (rowLength - 1)) {
            if (grid[row + 1][col] == 1) {
                quf.union(entry, entry + rowLength);
            }
        }
    }

    public boolean isOpen(int row, int col) {

        if (row < 1 || row > rowLength) {
            throw new IllegalArgumentException("row value out of bound in open()");
        }
        if (col < 1 || col > rowLength) {
            throw new IllegalArgumentException("row value out of bound in open()");
        }

        row = row - 1;
        col = col - 1;

        if (grid[row][col] == 1) {
            return true;
        }
        return false;
    }

    public boolean isFull(int row, int col) {

        if (!this.isOpen(row, col)) {
            return false;
        }

        row = row - 1;
        col = col - 1;
        int entry = rowLength * row + col;
        if (entry >= gridSize) {
            return false;
        }

        for (int i = 0; i < rowLength; i++) {
            if (grid[0][i] == 1) {
                if (quf.find(i) == quf.find(entry))
                    return true;
            }
        }
        return false;
    }

    public int numberOfOpenSites() {
        return numOpen;
    }

    public boolean percolates() {
        for (int j = 1; j <= rowLength; j++) {
            if (this.isFull(rowLength, j)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // Only used during development
        Percolation perc = new Percolation(10);
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
    }
}
