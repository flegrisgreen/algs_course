import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int[] grid;
    private final int rowLength;
    private final int gridSize;
    private int numOpen = 0;
    private final WeightedQuickUnionUF quf;

    public Percolation(int n) {
        rowLength = n;
        gridSize = n * n;
        grid = new int[gridSize];
        quf = new WeightedQuickUnionUF(gridSize);
    }

    public void open(int row, int col) {

        if (row < 1 || row > rowLength) {
            throw new IllegalArgumentException("row value out of bound in open()");
        }
        if (col < 1 || col > rowLength) {
            throw new IllegalArgumentException("row value out of bound in open()");
        }

        row = row - 1;
        col = col - 1;
        int entry = rowLength * row + col;
        if (entry >= gridSize) {
            return;
        }
        else if (grid[entry] == 1) {
            return;
        }
        else {
            grid[entry] = 1;
            numOpen++;
            if (entry >= 0 && entry < gridSize) {
                if (col != 0) {
                    if (grid[entry - 1] == 1) {
                        quf.union(entry, entry - 1);
                    }
                }
                if (col != (rowLength - 1)) {
                    if (grid[entry + 1] == 1) {
                        quf.union(entry, entry + 1);
                    }
                }
            }
            if (entry - rowLength >= 0) {
                if (grid[entry - rowLength] == 1) {
                    quf.union(entry, entry - rowLength);
                }
            }
            if (entry + rowLength < gridSize) {
                if (grid[entry + rowLength] == 1) {
                    quf.union(entry, entry + rowLength);
                }
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
        int entry = rowLength * row + col;
        if (entry >= gridSize) {
            return false;
        }

        if (grid[entry] == 1) {
            return true;
        }
        return false;

    }

    public boolean isFull(int row, int col) {

        if (row < 1 || row > rowLength) {
            throw new IllegalArgumentException("row value out of bound in open()");
        }
        if (col < 1 || col > rowLength) {
            throw new IllegalArgumentException("row value out of bound in open()");
        }

        row = row - 1;
        col = col - 1;
        int entry = rowLength * row + col;
        if (entry >= gridSize) {
            return false;
        }
        if (grid[entry] == 1) {
            for (int i = 0; i < rowLength; i++) {
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
        Percolation perc = new Percolation(5);
        for (int i = 1; i <= perc.rowLength; i++) {
            perc.open(i, 1);
            StdOut.println("isOpen: " + perc.isOpen(i, 1));
            StdOut.println("isFull: " + perc.isFull(i, 1));
            StdOut.println("Percolates: " + perc.percolates());
            for (int j = 0; j < perc.gridSize; j++) {
                StdOut.print(perc.grid[j]);
            }
            StdOut.println("");
        }
    }
}
