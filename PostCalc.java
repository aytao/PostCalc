import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;


public class PostCalc extends Rating {

    // create a new PostCalc simulation object
    public PostCalc(Player[] playersArr) {
        super(playersArr);
    }

    // simulate a game
    public void play(Player p1, Player p2) {
        double perf1 = p1.sample();
        double perf2 = p2.sample();
        double result = perf1 - perf2;

        double factor1 = Gaussian.cdf(perf1, p1.rating(), p1.stddev());
        double factor2 = Gaussian.cdf(perf2, p2.rating(), p2.stddev());

        if (result > 0) {
            factor2 -= 1;
        } else if (result < 0) {
            factor1 -= 1;
        } else {
            factor1 -= 0.5;
            factor2 -= 0.5;
        }
        p1.increaseRating((int) (factor1 * K));
        p2.increaseRating((int) (factor2 * K));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int rounds = Integer.parseInt(args[1]);
        int plotGroups = Integer.parseInt(args[2]);
        Player[] players = new Player[n];
        for (int i = 0; i < n; i++) {
            players[i] = new Player((int) StdRandom.gaussian(1500, 300), 1500);
        }
        PostCalc postCalc = new PostCalc(players);
        StdOut.println("Number of inversions: " + postCalc.inversions());
        StdOut.println("Average pair difference: " + postCalc.avgSkillDiff());
        Stopwatch stopwatch = new Stopwatch();
        postCalc.simulate(rounds);
        StdOut.println("Time elapsed: " + stopwatch.elapsedTime());
        StdOut.println("Number of inversions after simulation: " + postCalc.inversions());
        StdOut.println("Average pair difference after simulation: " + postCalc.avgSkillDiff());
        postCalc.plotRatings(10);
        postCalc.saveRatings("PostCalc1M");
        postCalc.plotRatings(plotGroups);
    }


}
