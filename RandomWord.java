/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        double count = 1;
        double prob;
        String out = "";
        while (!StdIn.isEmpty()) {
            String in = StdIn.readString();
            prob = 1 / count;
            if (StdRandom.bernoulli(prob)) {
                out = in;
            }
            count++;
        }
        StdOut.println(out);
    }
}
