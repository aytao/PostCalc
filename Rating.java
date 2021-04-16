import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public abstract class Rating {
    protected static final Comparator<Player> BY_RATING = Player.byRating();
    protected static final Comparator<Player> BY_SKILL = Player.bySkill();
    protected static final int K = 10;    // K value used for calculating ELO changes
    protected final Player[] players;     // array of all the players
    protected final int n;                // number of players

    // create a new Elo simulation object
    public Rating(Player[] playersArr) {
        n = playersArr.length;
        if (n % 2 != 0) {
            throw new IllegalArgumentException("Must be an even number of players");
        }
        players = new Player[n];
        for (int i = 0; i < n; i++) {
            players[i] = new Player(playersArr[i]);
        }
        Arrays.sort(players, BY_RATING);
    }

    // simulate the number of games provided; input parameter is number of rounds
    public void simulate(int rounds) {
        for (int i = 0; i < rounds; i++) {
            for (int j = 0; j + 1 < n; j += 2) {
                this.play(players[j], players[j + 1]);
            }
            Arrays.sort(players, BY_RATING);
        }
    }

    // count the number of skill inversions when players are ordered by rating
    public long inversions() {
        Multiset multiset = new Multiset();
        long inversions = 0;
        for (int i = 0; i < n; i++) {
            multiset.add(players[i].skill());
            inversions += i - multiset.rank(players[i].skill());
        }
        return inversions;
    }

    // count the number of "weighted" inversions, where weight is skill difference
    public long weightedInversions() {
        Multiset multiset = new Multiset();
        long weightedInversions = 0;
        for (int i = 0; i < n; i++) {
            multiset.add(players[i].skill());
            Iterable<Integer> q = multiset.keys(players[i].skill() + 1, highestRating());
            for (int skill : q) {
                weightedInversions += (skill - players[i].skill());
            }
        }
        return weightedInversions;
    }

    // calculate the average skill difference between paired games
    public double avgSkillDiff() {
        double avgDiff = 0;
        for (int i = 0; i + 1 < n; i += 2) {
            int diff = Math.abs(players[i].skill() - players[i + 1].skill());
            avgDiff += (double) diff / (n / 2);
        }
        return avgDiff;
    }

    // predict the outcome of a game (out of 1)
    public static double predict(Player p1, Player p2) {
        double exp = (double) - (p1.rating() - p2.rating()) / 400;
        double denominator = 1 + Math.pow(10, exp);
        return (1 / denominator);
    }

    // average rating over all players
    public int avgRating() {
        int total = 0;
        for (Player player : players) {
            total += player.rating();
        }
        return total / n;
    }

    // returns the rating of the lowest rated player
    public int lowestRating() {
        return players[0].rating();
    }

    // returns the rating of the highest rated player
    public int highestRating() {
        return players[n - 1].rating();
    }

    // plot rating distribution
    public void plotRatings(int group) {
        int highest = highestRating();
        double[] ratings = new double[(int) (highest/group * 1.2)];
        int j = 0;
        for (int i = 1; i < highest/group + 1; i += 1) {
            while (j < n && players[j].rating() < i * group) {
                ratings[i]++;
                j++;
            }
        }
        double max = StdStats.max(ratings);
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] /= max;
        }
        StdStats.plotLines(ratings);
    }

    // save rating distributions to a file with specified name
    public void saveRatings(String fileName) {
        int group = 1;
        int highest = highestRating();
        int[] ratings = new int[(int) (highest/group * 1.2)];
        int j = 0;
        for (int i = 1; i < highest/group + 1; i += 1) {
            while (j < n && players[j].rating() < i * group) {
                ratings[i]++;
                j++;
            }
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < ratings.length; i++) {
            str.append(i).append(",").append(ratings[i]);
            str.append("\n");
        }

        try {
            File file = new File(fileName);
            if (!file.createNewFile()) {
                StdOut.println("File already exists");
                return;
            }
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(str.toString());
            fileWriter.close();
        } catch (IOException e) {
            StdOut.println("An error occurred.");
            e.printStackTrace();;
        }

    }

    // return rating distribution as an int array
    public int[] ratings() {
        int group = 1;
        int highest = highestRating();
        int[] ratings = new int[(int) (highest/group * 1.2)];
        int j = 0;
        for (int i = 1; i < highest/group + 1; i += 1) {
            while (j < n && players[j].rating() < i * group) {
                ratings[i]++;
                j++;
            }
        }
        return ratings;
    }


    // simulate a game between two players
    public abstract void play(Player player1, Player player2);
}
