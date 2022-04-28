/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        String input = "start";
        RandomizedQueue<String> inputs = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            input = StdIn.readString();
            inputs.enqueue(input);
        }

        while (k > 0) {
            StdOut.println(inputs.dequeue());
            k--;
        }
    }
}
