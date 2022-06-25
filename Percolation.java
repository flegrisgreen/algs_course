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
    private boolean percolating = false;
    private boolean checkSets = true;

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
                this.updatePercolating();
            }
        }
        // right
        if (col != (rowLength - 1)) {
            if (grid[row][col + 1]) {
                quf.union(entry, entry + 1);
                this.updatePercolating();
            }
        }

        // Check for adjacent open rows
        // above
        if (row != 0) {
            if (row == 1 && grid[row - 1][col]) {
                quf.union(entry, entry - rowLength);
                this.updateTop(col);
                this.updatePercolating();
            }
            else if (grid[row - 1][col]) {
                quf.union(entry, entry - rowLength);
                this.updatePercolating();
            }
        }
        else {
            if (grid[row + 1][col]) {
                this.updateTop(col);
                this.updatePercolating();
            }
        }

        // below
        if (row != (rowLength - 1)) {
            if (row == (rowLength - 2) && grid[row + 1][col]) {
                quf.union(entry, entry + rowLength);
                this.updateBottom(col);
                this.updatePercolating();
            }
            else if (grid[row + 1][col]) {
                quf.union(entry, entry + rowLength);
                this.updatePercolating();
            }
        }
        else {
            if (grid[row - 1][col]) {
                this.updateBottom(col);
                this.updatePercolating();
            }
        }
    }

    public boolean isOpen(int row, int col) {

        if (row < 1 || row > rowLength) {
            throw new IllegalArgumentException("row value out of bound in open()");
        }
        if (col < 1 || col > rowLength) {
            throw new IllegalArgumentException("col value out of bound in open()");
        }

        int[] mappedVals = this.getMappedVals(row, col);
        row = mappedVals[1];
        col = mappedVals[2];

        return grid[row][col];
    }

    public boolean isFull(int row, int col) {

        if (!this.isOpen(row, col)) {
            return false;
        }

        int[] mappedVals = this.getMappedVals(row, col);
        int entry = mappedVals[0];

        if (row == 1 && grid[row][col]) {
            return true;
        }

        for (int i = 0; i < openTop; i++) {
            if (quf.find(this.openTopRow[i] - 1) == quf.find(entry)) {
                return true;
            }
        }
        return false;
    }

    public int numberOfOpenSites() {
        return numOpen;
    }

    public boolean percolates() {
        return percolating;
    }

    private int[] getMappedVals(int row, int col) {
        row = row - 1;
        col = col - 1;
        return new int[] { (rowLength * row + col), row, col };
    }

    private void updatePercolating() {
        if (this.numberOfOpenSites() < rowLength) {
            return;
        }

        // Only check open top and bottom site that have adjacent sites open -> implement in open()

        // Update and check set count < n^2 -(n-1) (this only requires one quf call)
        if (openBottom > 0 && openTop > 0 && checkSets) {
            if (quf.count() > (gridSize - rowLength + 1)) {
                return;
            }
            else checkSets = false;
        }

        for (int i = 0; i < openBottom; i++) {
            if (this.isFull(rowLength, openBottomRow[i])) {
                percolating = true;
            }
        }
    }

    private void updateTop(int col) {
        openTopRow[openTop] = col + 1; // Non-mapped col value
        openTop++;
    }

    private void updateBottom(int col) {
        openBottomRow[openBottom] = col + 1; // Non-mapped col value
        openBottom++;
    }

    public static void main(String[] args) {
        // Only used during development
        Percolation perc = new Percolation(5);
        StdOut.println("isOpen: " + perc.isOpen(1, 1));
        StdOut.println("isFull: " + perc.isFull(1, 1));
        perc.open(1, 2);
        boolean toggle = false;
        int start;
        for (int i = 1; i <= perc.rowLength; i++) {
            start = toggle ? 2 : 1;
            toggle = !toggle;
            for (int j = start; j <= perc.rowLength; j = j + 2) {
                perc.open(i, j);
            }
            perc.open(2, 3);
            perc.open(2, 4);
            perc.open(4, 3);
            StdOut.println("isOpen: " + i + perc.isOpen(i, 1));
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
            StdOut.println(perc.quf.count());
        }
        for (int k = 0; k < perc.rowLength; k++) {
            StdOut.print(perc.openTopRow[k] + " ");
        }
        StdOut.print("\n");
        for (int k = 0; k < perc.rowLength; k++) {
            StdOut.print(perc.openBottomRow[k] + " ");
        }
    }
}
