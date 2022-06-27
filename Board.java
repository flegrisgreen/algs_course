/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int[][] board;
    private final int boardSize;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.board = new int[tiles.length][tiles.length];
        this.boardSize = tiles.length;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                this.board[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        String size = Integer.toString(this.boardSize);
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                s.append(" ");
                s.append(Integer.toString(this.board[i][j]));
            }
            s.append("\n");
        }
        return size + "\n" + s.toString();
    }

    // board dimension n
    public int dimension() {
        return this.boardSize;
    }

    // number of tiles out of place
    public int hamming() {
        int goalVal = 1;
        int hammingCount = 0;
        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                if (board[i][j] == 0) {
                    goalVal++;
                    continue;
                }
                else if (board[i][j] != goalVal) {
                    hammingCount++;
                }
                goalVal++;
            }
        }
        return hammingCount;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattanCount = 0;
        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                int val = board[i][j];
                if (val == 0) continue;
                int row = 0;
                while (val > ((row + 1) * boardSize)) {
                    row++;
                }
                int col = Math.abs(val - (row * boardSize) - 1);
                manhattanCount = manhattanCount + Math.abs(row - i) + Math.abs(col - j);
            }
        }
        return manhattanCount;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.checkBoard(0, 0, 1);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        // Going to have to check strings against each other.
        if (this.dimension() != ((Board) y).dimension()) {
            return false;
        }
        // This is not pretty. Find better solution. Maybe using neighbours?
        if (this.toString().equals(((Board) y).toString())) {
            return true;
        }
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> list = new ArrayList<>();

        // Find 0
        int[] zeroPos = this.findZero(0, 0); // zeroPos[0] = row
        int zRow = zeroPos[0];
        int zCol = zeroPos[1];

        // Copy this board
        int[][] neighbour = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                neighbour[i][j] = board[i][j];
            }
        }

        // Swap above
        if (zRow != 0) {
            neighbour[zRow - 1][zCol] = board[zRow][zCol];
            neighbour[zRow][zCol] = board[zRow - 1][zCol];
            list.add(new Board(neighbour));
            neighbour[zRow - 1][zCol] = board[zRow - 1][zCol];
            neighbour[zRow][zCol] = board[zRow][zCol];
        }

        // Swap below
        if (zRow != (boardSize - 1)) {
            neighbour[zRow + 1][zCol] = board[zRow][zCol];
            neighbour[zRow][zCol] = board[zRow + 1][zCol];
            list.add(new Board(neighbour));
            neighbour[zRow + 1][zCol] = board[zRow + 1][zCol];
            neighbour[zRow][zCol] = board[zRow][zCol];
        }

        // Swap left
        if (zCol != 0) {
            neighbour[zRow][zCol - 1] = board[zRow][zCol];
            neighbour[zRow][zCol] = board[zRow][zCol - 1];
            list.add(new Board(neighbour));
            neighbour[zRow][zCol - 1] = board[zRow][zCol - 1];
            neighbour[zRow][zCol] = board[zRow][zCol];
        }

        // Swap right
        if (zCol != (boardSize - 1)) {
            neighbour[zRow][zCol + 1] = board[zRow][zCol];
            neighbour[zRow][zCol] = board[zRow][zCol + 1];
            list.add(new Board(neighbour));
        }
        return list;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        // Copy board
        int[][] neighbour = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                neighbour[i][j] = board[i][j];
            }
        }
        // Get random positions
        int a = StdRandom.uniform(boardSize);
        int b = StdRandom.uniform(boardSize);
        int c = StdRandom.uniform(boardSize);
        int d = StdRandom.uniform(boardSize);

        while (d == b) {
            d = StdRandom.uniform(boardSize);
        }
        // Swap tiles
        neighbour[a][b] = board[c][d];
        neighbour[c][d] = board[a][b];

        return new Board(neighbour);
    }

    private boolean checkBoard(int row, int col, int val) {
        if (row == this.boardSize - 1 && col == this.boardSize - 1) {
            return true;
        }
        else if (this.board[row][col] != val) {
            return false;
        }
        else {
            if (col == this.boardSize - 1) {
                return checkBoard(row + 1, 0, val + 1);
            }
            else {
                return checkBoard(row, col + 1, val + 1);
            }
        }
    }

    private int[] findZero(int row, int col) {
        if (board[row][col] == 0) {
            int[] rowCol = new int[2];
            rowCol[0] = row;
            rowCol[1] = col;
            return rowCol;
        }
        else {
            if (col == this.boardSize - 1) {
                return findZero(row + 1, 0);
            }
            else {
                return findZero(row, col + 1);
            }
        }

    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] test = new int[3][3];
        int[][] test2 = new int[4][4];
        int[][] randomTest = new int[3][3];

        int val = 8;
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 3; k++) {
                test[j][k] = val;
                val--;
            }
        }

        int val2 = 1;
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 4; k++) {
                if (j == 3 && k == 3) {
                    test2[j][k] = 0;
                    break;
                }
                test2[j][k] = val2;
                val2++;
            }
        }
        List<Integer> taken = new ArrayList<>();
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 3; k++) {
                val = StdRandom.uniform(9);
                while (taken.contains(val)) {
                    val = StdRandom.uniform(9);
                }
                randomTest[j][k] = val;
                taken.add(val);
            }
        }

        Board testBoard = new Board(test);
        StdOut.println(testBoard.toString());
        StdOut.println(testBoard.isGoal());
        StdOut.println(testBoard.hamming());
        StdOut.println(testBoard.manhattan());
        Board testBoardSame = new Board(test);
        StdOut.println(testBoard.equals(testBoardSame));

        StdOut.println("--------------------");

        Board testBoard2 = new Board(test2);
        StdOut.println(testBoard2.toString());
        StdOut.println(testBoard2.isGoal());
        StdOut.println(testBoard2.hamming());
        StdOut.println(testBoard2.manhattan());
        StdOut.println(testBoard2.equals(testBoard));

        StdOut.println("--------------------");

        Board randomTestBoard = new Board(randomTest);
        StdOut.println(randomTestBoard.toString());
        StdOut.println("Is goal" + randomTestBoard.isGoal());
        StdOut.println("Hamming" + randomTestBoard.hamming());
        StdOut.println("Manhattan" + randomTestBoard.manhattan());
        StdOut.println("Equal test board" + randomTestBoard.equals(testBoard));
        randomTestBoard.neighbors().forEach((neighbour) -> StdOut.println(neighbour.toString()));
        StdOut.println("Twin" + "\n" + randomTestBoard.twin().toString());
    }
}
