# PostCalc
This repository represents a collection of Java files used in simulating the PostCalc and Elo rating systems,
with the Tester class performing comparative analysis on the utility of the two systems. The text files represent
the output of running Tester with command-line arguments 100000 500, which corresponds to 100K players and 500
rounds simulated. The "RS" files are in the csv format, where the first two columns represent players in the Elo
system and the last two columns represent players in the PostCalc system. Both groupings of columns are sorted
by rating, which is the first column, and listed next to that respective player's underlying skill (the adjacent
column). The "Freq" files represent rating distributions, where the first number in each line is a rating, the
second number if the number of times this rating appears after the Elo simulation, and the third number
represents the number of times this rating appears after the PostCalc simulation. The "PreSimul" files are the
rating and skill of all players given to the two systems as input before any simulation has been done. The
prefixes "Mal" and "Norm" correspond to malicious and normal inputs, respectively. Malicious inputs are inputs
where half of the players are severely overrated and half of the players are severely underrated, while normal
inputs have player skills on a normal distribution centered at 1500 with a standard deviation of 300. The
"100K,500" text file is the output of the Tester class.
