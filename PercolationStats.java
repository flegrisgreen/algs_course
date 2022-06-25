/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONF = 1.96;
    private final double[] finalResults;

    public PercolationStats(int n, int trials) {

        if (n < 1) {
            throw new IllegalArgumentException("n must be larger than 0");
        }

        if (trials < 1) {
            throw new IllegalArgumentException("t must be larger than 0");
        }

        double size = n * n;
        double[] results = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                perc.open(row, col);
            }
            double open = perc.numberOfOpenSites();
            double fraction = open / size;
            results[i] = fraction;
        }
        finalResults = results;
    }

    public double mean() {
        double mean = StdStats.mean(finalResults);
        return mean;
    }

    public double stddev() {
        double stddev = StdStats.stddev(finalResults);
        return stddev;
    }

    public double confidenceLo() {
        double loConf = (this.mean() - ((PercolationStats.CONF * this.stddev()) / Math.sqrt(
                finalResults.length)));
        return loConf;
    }

    public double confidenceHi() {
        double hiConf = (this.mean() + ((PercolationStats.CONF * this.stddev()) / Math.sqrt(
                finalResults.length)));
        return hiConf;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Two arguments required: n and t");
        }
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        // Stopwatch time = new Stopwatch();
        PercolationStats percStats = new PercolationStats(n, t);
        StdOut.println("mean                    = " + percStats.mean());
        StdOut.println("stddev                  = " + percStats.stddev());
        StdOut.println("95% confidence interval = [" + percStats.confidenceLo() + ", "
                               + percStats.confidenceHi() + "]");
        // StdOut.println(time.elapsedTime());
    }
}
