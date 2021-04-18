import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Tester {

    public static void simulateInput(Elo elo, PostCalc postCalc, int rounds) {
        StdOut.println("Before simulations");
        StdOut.printf("Number of inversions: %d\n", elo.inversions());
        StdOut.printf("Total weight of inversions: %d\n", elo.weightedInversions());
        StdOut.printf("Average skill difference: %.3f\n", elo.avgSkillDiff());

        elo.simulate(rounds);
        StdOut.println("\nAfter ELO simulation");
        StdOut.printf("Number of inversions: %d\n", elo.inversions());
        StdOut.printf("Total weight of inversions: %d\n", elo.weightedInversions());
        StdOut.printf("Average skill difference: %.3f\n", elo.avgSkillDiff());

        postCalc.simulate(rounds);
        StdOut.println("\nAfter PostCalc simulation");
        StdOut.printf("Number of inversions: %d\n", postCalc.inversions());
        StdOut.printf("Total weight of inversions: %d\n", postCalc.weightedInversions());
        StdOut.printf("Average skill difference: %.3f\n", postCalc.avgSkillDiff());

        StdOut.println("\nAverage Ratings");
        StdOut.printf("Elo: %.3f\n", elo.avgRating());
        StdOut.printf("PostCalc: %.3f\n", postCalc.avgRating());
    }

    private static Player[] maliciousInput(int num) {
        Player[] players = new Player[num];
        for (int i = 0; i < num / 2; i++) {
            players[i] = new Player((int) StdRandom.gaussian(2000, 100), 1000);
        }
        for (int i = num / 2; i < num; i++) {
            players[i] = new Player((int) StdRandom.gaussian(1000, 100), 2000);
        }
        return players;
    }

    private static Player[] normalInput(int num) {
        Player[] players = new Player[num];
        for (int i = 0; i < num; i++) {
            int skill = (int) StdRandom.gaussian(1500, 300);
            if (skill < 0) {
                throw new RuntimeException("Damn :/");
            }
            players[i] = new Player(skill, 1500);
        }
        return players;
    }

    // saves rating distributions to a file of specified name
    public static void saveFreq(String fileName, int[] eloRatings, int[] PostCalcRatings) {
        StringBuilder str = new StringBuilder();
        int length = eloRatings.length;
        if (length < PostCalcRatings.length) {
            length = PostCalcRatings.length;
        }
        for (int i = 0; i < length; i++) {
            str.append(i);

            str.append(",");
            if (i < eloRatings.length) {
                str.append(eloRatings[i]);
            } else {
                str.append(0);
            }
            str.append(",");
            if (i < PostCalcRatings.length) {
                str.append(PostCalcRatings[i]);
            } else {
                str.append(0);
            }
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
            e.printStackTrace();
        }
    }

    // saves ratings vs skill to a file
    public static void saveRS(String fileName, Elo elo, PostCalc postCalc) {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < elo.players.length; i++) {
            str.append(elo.players[i].rating()).append(",");
            str.append(elo.players[i].skill()).append(",");
            str.append(postCalc.players[i].rating()).append(",");
            str.append(postCalc.players[i].skill()).append("\n");
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
            e.printStackTrace();
        }
    }

    // saves player states to a file
    public static void saveRS(String fileName, Player[] players) {
        StringBuilder str = new StringBuilder();

        for (Player player : players) {
            str.append(player.rating()).append(",");
            str.append(player.skill()).append("\n");
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
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int rounds = Integer.parseInt(args[1]);
        Player[] players1 = normalInput(n);

        Elo elo = new Elo(players1);
        PostCalc postCalc = new PostCalc(players1);

        saveRS("NormPreSimul" + args[0] + "," + args[1] + ".txt", players1);

        StdOut.println("Normal Input");
        StdOut.println("____________________");
        simulateInput(elo, postCalc, rounds);

        saveFreq("NormFreq" + args[0] + "," + args[1] + ".txt", elo.ratings(), postCalc.ratings());
        saveRS("NormRS" + args[0] + "," + args[1] + ".txt", elo, postCalc);

        Player[] players2 = maliciousInput(n);
        Elo elo2 = new Elo(players2);
        PostCalc postCalc2 = new PostCalc(players2);

        saveRS("MalPreSimul" + args[0] + "," + args[1] + ".txt", elo2, postCalc2);

        StdOut.println("\n\nMalicious Input");
        StdOut.println("____________________");
        simulateInput(elo2, postCalc2, rounds);

        saveFreq("MalFreq" + args[0] + "," + args[1] + ".txt", elo2.ratings(), postCalc2.ratings());
        saveRS("MalRS" + args[0] + "," + args[1] + ".txt", elo2, postCalc2);
    }
}
