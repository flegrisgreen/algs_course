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

    //Todo:
    // Try to take advantage of sparcity?
    // Try to use recursive function when checking for percolation?

    public Percolation(int n) {

        if (n < 1) {
            throw new IllegalArgumentException("n must be larger than 0");
        }

        rowLength = n;
        gridSize = n * n;
        grid = new boolean[n][n];
        quf = new WeightedQuickUnionUF(gridSize);
        openTopRow = new int[n]; // this can be half this size
        openBottomRow = new int[n]; // this can be half this size
    }

    public void open(int row, int col) {

        if (this.isOpen(row, col)) {
            return;
        }

        boolean shouldUpdate = false;
        int[] mappedVals = this.getMappedVals(row, col);
        int entry = mappedVals[0];
        row = mappedVals[1];
        col = mappedVals[2];

        if (entry >= gridSize) {
            return;
        }

        grid[row][col] = true;
        numOpen++;

        if (row == 0) {
            if (row == rowLength - 1) { // n = 1
                percolating = true;
                return;
            }
            if (grid[row + 1][col]) {
                quf.union(entry, entry + rowLength);
                shouldUpdate = true;
            }
            this.updateTop(col);
        }
        else if (row == rowLength - 1) {
            if (grid[row - 1][col]) {
                quf.union(entry, entry - rowLength);
                shouldUpdate = true;
            }
            this.updateBottom(col);
        }
        else {
            if (grid[row - 1][col]) {
                quf.union(entry, entry - rowLength);
                shouldUpdate = true;
            }
            if (grid[row + 1][col]) {
                quf.union(entry, entry + rowLength);
                shouldUpdate = true;
            }
        }

        if (col == 0) {
            if (grid[row][col + 1]) {
                quf.union(entry, entry + 1);
                shouldUpdate = true;
            }
        }
        else if (col == rowLength - 1) {
            if (grid[row][col - 1]) {
                quf.union(entry, entry - 1);
                shouldUpdate = true;
            }
        }
        else {
            if (grid[row][col - 1]) {
                quf.union(entry, entry - 1);
                shouldUpdate = true;
            }
            if (grid[row][col + 1]) {
                quf.union(entry, entry + 1);
                shouldUpdate = true;
            }
        }
        if (shouldUpdate) {
            this.updatePercolating();
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

        return grid[mappedVals[1]][mappedVals[2]];
    }

    public boolean isFull(int row, int col) {

        if (!this.isOpen(row, col)) {
            return false;
        }

        if (row == 1 && grid[row - 1][col - 1]) {
            return true;
        }
        int entrySetVal = quf.find(this.getMappedVals(row, col)[0]);
        for (int i = 0; i < openTop; i++) {
            if (quf.find(this.openTopRow[i] - 1) == entrySetVal) {
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
        if (numOpen < rowLength) {
            return;
        }

        if (!(openTop > 0) || !(openBottom > 0)) {
            return;
        }

        // Update and check set count < n^2 -(n-1) (this only requires one quf call)
        if (checkSets) {
            if (quf.count() > ((gridSize - rowLength) + 1)) {
                return;
            }
            else checkSets = false;
        }

        for (int i = 0; i < openBottom; i++) {
            if (this.isFull(rowLength, openBottomRow[i])) {
                percolating = true;
                return;
            }
        }
    }

    private void updateTop(int col) {
        if (col == 0) {
            if (!grid[0][col + 1]) {
                openTopRow[openTop] = col + 1; // Non-mapped col value
                openTop++;
            }
        }
        else if (col == rowLength - 1) {
            if (!grid[0][col - 1]) {
                openTopRow[openTop] = col + 1; // Non-mapped col value
                openTop++;
            }
        }
        else if (!grid[0][col - 1] && !grid[0][col + 1]) { // Only need to check unique sets
            openTopRow[openTop] = col + 1; // Non-mapped col value
            openTop++;
        }
        else if (grid[0][col - 1] && grid[0][col + 1]) {
            this.rebuildOpenTop(col); // Non-mapped col value
        }
    }


    private void rebuildOpenTop(int col) {
        int setVal = quf.find(this.getMappedVals(1, col)[0]);
        boolean readyPopped = false; // Pop the first one and shift entries left
        for (int i = 0; i < openTop; i++) {
            if (readyPopped) {
                openTopRow[i - 1] = openTopRow[i];
                continue;
            }

            if (quf.find(this.getMappedVals(1, openTopRow[i])[0]) == setVal) {
                openTopRow[i] = 0;
                readyPopped = true;
            }
        }
        openTop--;
    }

    private void updateBottom(int col) {
        int bottomRow = rowLength - 1;
        if (col == 0) {
            if (!grid[bottomRow][col + 1]) {
                openBottomRow[openBottom] = col + 1; // Non-mapped col value
                openBottom++;
            }
        }
        else if (col == bottomRow) {
            if (!grid[bottomRow][col - 1]) {
                openBottomRow[openBottom] = col + 1; // Non-mapped col value
                openBottom++;
            }
        }
        else if (!grid[bottomRow][col - 1] && !grid[bottomRow][col
                + 1]) { // Only need to check unique sets
            openBottomRow[openBottom] = col + 1; // Non-mapped col value
            openBottom++;
        }
        else if (grid[bottomRow][col - 1] && grid[bottomRow][col
                + 1]) { // Only need to check unique sets
            this.rebuildOpenBottom(col);  // Non-mapped col value
        }
    }

    private void rebuildOpenBottom(int col) {
        int setVal = quf.find(this.getMappedVals(rowLength - 1, col)[0]);
        boolean readyPopped = false; // Pop the first one and shift entries left
        for (int i = 0; i < openBottom; i++) {
            if (readyPopped) {
                openBottomRow[i - 1] = openBottomRow[i];
                continue;
            }

            if (quf.find(this.getMappedVals(1, openBottomRow[i])[0]) == setVal) {
                openBottomRow[i] = 0;
                readyPopped = true;
            }
        }
        openBottom--;
    }

    public static void main(String[] args) {
        // Only used during development
        Percolation perc = new Percolation(10);
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
