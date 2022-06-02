/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

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
        String boardAsString = "";

        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                boardAsString = boardAsString + " " + Integer.toString(this.board[i][j]);
            }
            boardAsString = boardAsString + "\n";
        }
        return size + "\n" + boardAsString;
    }

    // board dimension n
    public int dimension() {
        return this.boardSize;
    }

    // number of tiles out of place
    public int hamming() {
        return 0;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return 0;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.checkBoard(0, 0, 1);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        // Going to have to check strings against each other.
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> list = new ArrayList<>();

        // Loop to check which tiles can switch with 0
        // New (neighbour) boards are added to list
        // list.add(neighbour)
        // Return list

        return list;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        return null;
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

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] test = new int[3][3];
        int[][] test2 = new int[4][4];

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

        Board testBoard = new Board(test);
        StdOut.println(testBoard.toString());
        StdOut.println(testBoard.isGoal());

        Board testBoard2 = new Board(test2);
        StdOut.println(testBoard2.toString());
        StdOut.println(testBoard2.isGoal());

    }
}
