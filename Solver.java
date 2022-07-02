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
    // private Board initialBoard;
    private boolean solvable;
    private Stack<Board> solution;

    public Solver(Board initial) {

        NodeComparator comparator = new NodeComparator();
        MinPQ<Node> q = new MinPQ<>(comparator);
        MinPQ<Node> qTwin = new MinPQ<>(comparator);

        if (initial == null) {
            throw new IllegalArgumentException();
        }
        // initialBoard = initial;
        Node initialNode = new Node(initial, null, 0);
        Node twin = new Node(initial.twin(), null, 0);

        q.insert(initialNode);
        qTwin.insert(twin);

        List<Board> searched = new ArrayList<>();
        List<Board> searchedTwin = new ArrayList<>();

        solution = solve(q, searched, qTwin, searchedTwin);
        if (solution.isEmpty()) {
            solvable = false;
        }
        else {
            solvable = true;
        }
    }

    private Stack<Board> solve(MinPQ<Node> q, List<Board> searched, MinPQ<Node> qTwin,
                               List<Board> searchedTwin) {

        Stack<Board> sol = new Stack<>();
        while (true) {
            Node node = q.delMin();
            Node twinNode = qTwin.delMin();

            // Solution found
            if (node.thisBoard.isGoal()) {
                while (node.prevNode != null) {
                    sol.push(node.thisBoard);
                    node = node.prevNode;
                }
                sol.push(node.thisBoard);
                break;
            }
            // Solution found for twin -> unsolvable board
            else if (twinNode.thisBoard.isGoal()) {
                break;
            }

            // Push to searched
            if (node.prevNode != null) {
                searched.clear();
                searched.add(node.prevNode.thisBoard);
            }

            if (twinNode.prevNode != null) {
                searchedTwin.clear();
                searchedTwin.add(twinNode.prevNode.thisBoard);
            }

            // Push to board solution queue
            Iterable<Board> neighbours = node.thisBoard.neighbors();
            Node parentNode = node;
            neighbours.forEach(nb -> {
                if (!searched.contains(nb)) {
                    Node nn = new Node(nb, parentNode, parentNode.moves + 1);
                    q.insert(nn);
                }
            });

            // Push to twin board solution queue
            Iterable<Board> twinNeighbours = twinNode.thisBoard.neighbors();
            twinNeighbours.forEach(nb -> {
                if (!searchedTwin.contains(nb)) {
                    Node nn = new Node(nb, twinNode, twinNode.moves + 1);
                    qTwin.insert(nn);
                }
            });
        }
        return sol;
    }

    private class Node {
        final Board thisBoard;
        final int score;
        final Node prevNode;
        final int moves;

        Node(Board thisBoard, Node prevNode, int moves) {
            this.thisBoard = thisBoard;
            this.prevNode = prevNode;
            this.score = this.thisBoard.manhattan() + moves;
            this.moves = moves;
        }
    }

    private class NodeComparator implements Comparator<Node> {
        public int compare(Node a, Node b) {
            return Integer.compare(a.score, b.score);
        }
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }


    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {

        if (!solvable) {
            return -1;
        }
        return (solution.size() - 1); // Do not include first board
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {

        if (!solvable) {
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
        if (solver.isSolvable()) {
            solver.solution().forEach(s -> StdOut.println(s.toString()));
        }
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
        StdOut.println("Start board \n" + randomTestBoard.toString());

        Solver solution = new Solver(randomTestBoard);
        StdOut.println("Is solvable: " + solution.isSolvable());
        StdOut.println("Min number of moves: " + solution.moves());
        if (solution.isSolvable()) {
            solution.solution().forEach(s -> StdOut.println(s.toString()));
        }

        int[][] exceptionTest = new int[3][3];
        exceptionTest[0][0] = 5;
        exceptionTest[0][1] = 8;
        exceptionTest[0][2] = 6;
        exceptionTest[1][0] = 4;
        exceptionTest[1][1] = 2;
        exceptionTest[1][2] = 3;
        exceptionTest[2][0] = 1;
        exceptionTest[2][1] = 7;
        exceptionTest[2][2] = 0;

        Board exceptionBoard = new Board(exceptionTest);
        StdOut.println("Start board \n" + exceptionBoard.toString());

        Solver exception = new Solver(exceptionBoard);
        StdOut.println("Is solvable: " + exception.isSolvable());
        StdOut.println("Min number of moves: " + exception.moves());
        if (exception.isSolvable()) {
            exception.solution().forEach(s -> StdOut.println(s.toString()));
        }
    }

}

