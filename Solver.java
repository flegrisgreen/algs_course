/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Solver {
    // find a solution to the initial board (using the A* algorithm)
    private final Board initialBoard;
    private final boolean solvalble;
    Stack<Board> solution;

    public Solver(Board initial) {

        initialBoard = initial;
        NodeComparator comparator = new NodeComparator();
        MinPQ<Node> q = new MinPQ<>(comparator);

        if (initial == null) {
            throw new IllegalArgumentException();
        }
        if (!this.isSolvable()) {
            solvalble = false;
            return;
        }
        solvalble = true;

        Node initialNode = new Node(initial, null, 0);
        q.insert(initialNode);
        List<Board> searched = new ArrayList<>();
        solution = solve(q, searched);
    }

    private Stack<Board> solve(MinPQ<Node> q, List<Board> searched) {

        Node node = q.delMin();
        // Solution found
        if (node.thisBoard.isGoal()) {
            Stack<Board> sol = new Stack<>();
            Board board = node.thisBoard;
            while (node.prevNode != null) {
                sol.push(node.thisBoard);
                node = node.prevNode;
            }
            sol.push(node.thisBoard);
            return sol;
        }

        searched.add(node.thisBoard);

        Iterable<Board> neighbours = node.thisBoard.neighbors();
        Node parentNode = node;
        neighbours.forEach(nb -> {
            if (!searched.contains(nb)) {
                Node nn = new Node(nb, parentNode, parentNode.moves + 1);
                q.insert(nn);
            }
        });

        // Break is priority queue is empty and no solution was found
        if (!searched.isEmpty() && q.isEmpty()) {
            return new Stack<>();
        }

        return solve(q, searched);
    }

    private class Node {
        final Board thisBoard;
        final int Score;
        final Node prevNode;
        final int moves;

        Node(Board thisBoard, Node prevNode, int moves) {
            this.thisBoard = thisBoard;
            this.prevNode = prevNode;
            this.Score = this.thisBoard.manhattan() + moves;
            this.moves = moves;
        }
    }

    private class NodeComparator implements Comparator<Node> {
        public int compare(Node A, Node B) {
            return Integer.compare(A.Score, B.Score);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        // Returns false if the solution can be reached by swapping two tiles (not including 0)
        StdOut.println(initialBoard.twin());
        if (initialBoard.twin().isGoal()) {
            return false;
        }
        return true;
    }


    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {

        if (!solvalble) {
            return -1;
        }
        return (solution.size() - 1); // Do not include first board
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {

        if (!solvalble) {
            return null;
        }

        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {

        int[][] test = new int[3][3];

        int val2 = 1;
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 3; k++) {
                if (j == 2 && k == 2) {
                    test[j][k] = 0;
                    break;
                }
                test[j][k] = val2;
                val2++;
            }
        }

        test[2][1] = 7;
        test[2][0] = 8;

        Board testBoard = new Board(test);
        StdOut.println(testBoard.toString());
        Solver solver = new Solver(testBoard);
        StdOut.println(solver.isSolvable());
        StdOut.println("--------------------");

        int val = 0;
        int[][] randomTest = new int[3][3];
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

        Board randomTestBoard = new Board(randomTest);
        StdOut.println("Start board" + randomTestBoard);

        Solver solution = new Solver(randomTestBoard);
        StdOut.println("Is solvable: " + solution.isSolvable());
        StdOut.println("Min number of moves" + solution.moves());
        solution.solution().forEach(s -> StdOut.println(s.toString()));
    }

}

