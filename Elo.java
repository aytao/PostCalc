import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

public class Elo extends Rating {

    // create a new Elo simulation object
    public Elo(Player[] playersArr) {
        super(playersArr);
    }

    // simulate a game
    public void play(Player p1, Player p2) {
        int diff = (int) (K * (0.5 * (p1.play(p2) + 1) - predict(p1, p2)));
        p1.increaseRating(diff);
        p2.increaseRating(-diff);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int rounds = Integer.parseInt(args[1]);
        Player[] players = new Player[n];
        for (int i = 0; i < n; i++) {
            players[i] = new Player((int) StdRandom.gaussian(1500, 300), 1500);
        }
        Elo elo = new Elo(players);
        StdOut.println("Number of inversions: " + elo.inversions());
        StdOut.println("Average pair difference: " + elo.avgSkillDiff());
        Stopwatch stopwatch = new Stopwatch();
        elo.simulate(rounds);
        StdOut.println("Time elapsed: " + stopwatch.elapsedTime());
        StdOut.println("Number of inversions after simulation: " + elo.inversions());
        StdOut.println("Average pair difference after simulation: " + elo.avgSkillDiff());
    }


}
