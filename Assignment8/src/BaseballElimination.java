import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {

	private int[] win, loss, re;
	private int[][] games;
	private HashMap<String, Integer> teamNames;

	// create a baseball division from given filename in format specified below
	public BaseballElimination(String filename) {

		In in = new In(filename);
		int numTeams = in.readInt();
		in.readLine();

		win = new int[numTeams];
		loss = new int[numTeams];
		re = new int[numTeams];
		teamNames = new HashMap<String, Integer>();
		games = new int[numTeams][numTeams];

		for (int i = 0; i < numTeams; i++) {
			String line = in.readLine().trim();
			String[] tokens = line.split("\\s+");
			teamNames.put(tokens[0], i);
			win[i] = Integer.parseInt(tokens[1]);
			loss[i] = Integer.parseInt(tokens[2]);
			re[i] = Integer.parseInt(tokens[3]);

			for (int j = 0; j < numTeams; j++)
				games[i][j] = Integer.parseInt(tokens[j + 4]);
		}
	}

	private String getTeamFromIndex(int index) {
		for (Entry<String, Integer> entry : teamNames.entrySet())
			if (entry.getValue() == index)
				return entry.getKey();
		return null;
	}

	private void checkTeam(String team) {
		if (team == null || !teamNames.containsKey(team))
			throw new IllegalArgumentException();
	}

	// number of teams
	public int numberOfTeams() {
		return teamNames.size();
	}

	// all teams
	public Iterable<String> teams() {
		return teamNames.keySet();
	}

	// number of wins for given team
	public int wins(String team) {
		checkTeam(team);
		return win[teamNames.get(team)];
	}

	// number of losses for given team
	public int losses(String team) {
		checkTeam(team);
		return loss[teamNames.get(team)];
	}

	// number of remaining games for given team
	public int remaining(String team) {
		checkTeam(team);
		return re[teamNames.get(team)];
	}

	// number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		checkTeam(team1);
		checkTeam(team2);
		return games[teamNames.get(team1)][teamNames.get(team2)];
	}

	// is given team eliminated?
	public boolean isEliminated(String team) {
		checkTeam(team);
		EliminationProblem ep = new EliminationProblem(team);
		return ep.isEliminated;
	}

	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		checkTeam(team);
		EliminationProblem ep = new EliminationProblem(team);
		return ep.setR();
	}

	class EliminationProblem {

		int teamIndex, numGameVertices, numTeamVertices, numVertices;;
		int gameOffset, teamOffset;
		double capacity;
		String teamName;
		ArrayList<String> setR;
		FlowNetwork fn;
		FordFulkerson ff;

		boolean isEliminated;

		EliminationProblem(String team) {
			// System.out.println("Team: " + team);
			teamName = team;
			teamIndex = teamNames.get(team);
			numTeamVertices = teamNames.size() - 1;

			numGameVertices = 0;
			for (int i = 1; i < teamNames.size() - 1; i++)
				numGameVertices += i;

			numVertices = 2 + numGameVertices + numTeamVertices;
			gameOffset = 1;
			teamOffset = numGameVertices + 1;
			capacity = 0;
			setR = new ArrayList<String>();

			checkTrivialCase();

			if (setR.isEmpty()) {
				buildFlowNetwork();
				checkEliminated();
			}
		}

		private void checkTrivialCase() {
			for (int i = 0; i < numberOfTeams(); i++) {
				if (i == teamIndex)
					continue;
				if (win[i] > win[teamIndex] + re[teamIndex]) {
					isEliminated = true;
					setR.add(getTeamFromIndex(i));
					break;
				}
			}
		}

		private void addEdge(int from, int to, double capacity) {
			// /System.out.println(from + " --> " + to + " (" + capacity + ")");
			fn.addEdge(new FlowEdge(from, to, capacity));
		}

		private void buildFlowNetwork() {
			fn = new FlowNetwork(2 + numGameVertices + numTeamVertices);

			for (int i = 0; i < games.length; i++) {

				if (i == teamIndex)
					continue;

				if (i < teamIndex)
					addEdge(teamOffset + i, numVertices - 1, win[teamIndex]
							+ re[teamIndex] - win[i]);
				else
					addEdge(teamOffset + i - 1, numVertices - 1, win[teamIndex]
							+ re[teamIndex] - win[i]);

				for (int j = i + 1; j < games[0].length; j++) {

					if (j == teamIndex)
						continue;

					addEdge(0, gameOffset, games[i][j]);
					capacity += games[i][j];

					if (i > teamIndex)
						addEdge(gameOffset, teamOffset + i - 1,
								Double.POSITIVE_INFINITY);
					else
						addEdge(gameOffset, teamOffset + i,
								Double.POSITIVE_INFINITY);

					if (j > teamIndex)
						addEdge(gameOffset, teamOffset + j - 1,
								Double.POSITIVE_INFINITY);
					else
						addEdge(gameOffset, teamOffset + j,
								Double.POSITIVE_INFINITY);

					gameOffset++;
				}
			}
			gameOffset = 1;
		}

		private void checkEliminated() {
			ff = new FordFulkerson(fn, 0, numVertices - 1);

			if (capacity == ff.value())
				isEliminated = false;
			else {
				isEliminated = true;
				for (int i = 0; i < teamNames.size() - 1; i++) {
					if (i == teamIndex)
						continue;
					if (i < teamIndex) {
						if (ff.inCut(teamOffset + i))
							;
						setR.add(getTeamFromIndex(i));
					} else {
						if (ff.inCut(teamOffset + i - 1))
							;
						setR.add(getTeamFromIndex(i));
					}
				}
			}
		}

		private Iterable<String> setR() {
			if (!isEliminated)
				return null;
			return setR;
		}
	}

	public static void main(String[] args) {

		String[] filenames = { "baseball/teams5.txt", "baseball/teams4.txt",
				"baseball/teams4a.txt", "baseball/teams4b.txt",
				"baseball/teams5a.txt", "baseball/teams5b.txt",
				"baseball/teams5c.txt", "baseball/teams7.txt",
				"baseball/teams8.txt", "baseball/teams10.txt",
				"baseball/teams12.txt", "baseball/teams12-allgames.txt",
				"baseball/teams24.txt", "baseball/teams29.txt",
				"baseball/teams30.txt", "baseball/teams32.txt",
				"baseball/teams36.txt", "baseball/teams42.txt",
				"baseball/teams48.txt", "baseball/teams50.txt",
				"baseball/teams54.txt", "baseball/teams60.txt" };

		for (String file : filenames) {
			// String file = filenames[7];
			BaseballElimination division = new BaseballElimination(file);
			for (String team : division.teams()) {
				if (division.isEliminated(team)) {
					StdOut.print(team + " is eliminated by the subset R = { ");
					for (String t : division.certificateOfElimination(team)) {
						StdOut.print(t + " ");
					}
					StdOut.println("}");
				} else {
					StdOut.println(team + " is not eliminated");
				}
			}
		}
	}
}
