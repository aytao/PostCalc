import edu.princeton.cs.algs4.StdRandom;

import java.util.Comparator;

// Models a player with a fixed, underlying skill level and a variable rating level
public class Player {
    private static final int DEFAULT_SKILL = 1500;
    private static final int DEFAULT_RATING = 1500;
    private static final int MIN_RATING = 100;
    private static final int DEFAULT_STDDEV = 200;
    private int skill;
    private int rating;
    private int stddev;

    public Player(int skill, int rating) {
        this.skill = skill;
        this.rating = rating;
        this.stddev = DEFAULT_STDDEV;
    }

    public Player(Player player) {
        this.skill = player.skill;
        this.rating = player.rating;
        this.stddev = player.stddev;
    }

    public Player(int skill, int rating, int stddev) {
        this.skill = skill;
        this.rating = rating;
        this.stddev = stddev;
    }

    public Player() {
        skill = DEFAULT_SKILL;
        rating = DEFAULT_RATING;
    }

    public void increaseRating(int increase) {
        rating = Math.max(MIN_RATING, rating + increase);
    }

    public double sample() {
        return StdRandom.gaussian(skill, stddev);
    }

    // makes two players play based on a gaussian distribution of their skill
    public int play(Player that) {
        return Double.compare(this.sample(), that.sample());
    }

    // returns the associated rating of this player
    public int rating() {
        return rating;
    }

    // returns the associated skill of this player
    public int skill() {
        return skill;
    }

    // returns the stddev associated with this player
    public int stddev() {
        return stddev;
    }

    // returns a new comparator that compares players by rating
    public static Comparator<Player> byRating() {
        return new ByRating();
    }

    // compares players by rating
    public static class ByRating implements Comparator<Player> {

        public int compare(Player o1, Player o2) {
            return o1.rating - o2.rating;
        }
    }

    // returns a new comparator that compares player by skill
    public static Comparator<Player> bySkill() {
        return new BySkill();
    }

    // compares players by skill
    public static class BySkill implements Comparator<Player> {

        public int compare(Player o1, Player o2) {
            return o1.skill - o2.skill;
        }
    }

    // String representation of a player
    public String toString() {
        return "Skill: " + skill + ", Rating: " + rating;
    }

    public static void main(String[] args) {
        Player player1 = new Player(1900, 1500);
        Player player2 = new Player(1500, 1500);
        int total = Integer.parseInt(args[0]);

        for (int i = 0; i < total; i++) {
            if (player1.play(player2) > 0) {
                player1.increaseRating(5);
                player2.increaseRating(-5);
            } else {
                player2.increaseRating(5);
                player1.increaseRating(-5);
            }
        }
        System.out.println("Player 1 rating: " + player1.rating());
        System.out.println("Player 2 rating: " + player2.rating());
    }


}
