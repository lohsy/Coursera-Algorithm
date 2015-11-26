public class Outcast {
	// constructor takes a WordNet object

	private WordNet w;
	private int[][] dist;

	public Outcast(WordNet wordnet) {
		w = wordnet;
	}

	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		dist = new int[nouns.length][nouns.length];

		int maxDistance = Integer.MIN_VALUE;
		String outcastNoun = "";

		for (int i = 0; i < nouns.length; i++) {
			int distSum = 0;
			for (int j = i + 1; j < nouns.length; j++) {
				dist[i][j] = w.distance(nouns[i], nouns[j]);
			}

			for (int j = 0; j < nouns.length; j++) {
				if (i == j)
					continue;
				if (j < i)
					distSum += dist[j][i];
				else
					distSum += dist[i][j];
			}

			if (distSum > maxDistance) {
				maxDistance = distSum;
				outcastNoun = nouns[i];
			}
		}

		return outcastNoun;
	}

	// see test client below
	// public static void main(String[] args) {
	// WordNet wordnet = new WordNet(args[0], args[1]);
	// Outcast outcast = new Outcast(wordnet);
	// for (int t = 2; t < args.length; t++) {
	// In in = new In(args[t]);
	// String[] nouns = in.readAllStrings();
	// StdOut.println(args[t] + ": " + outcast.outcast(nouns));
	// }
	// }

	public static void main(String[] args) {
		WordNet wordnet = new WordNet("wordnet/synsets.txt",
				"wordnet/hypernyms.txt");
		// wordnet.distance("orange_juice", "apple_juice");
		Outcast outcast = new Outcast(wordnet);
		String[] filenames = new String[] { "wordnet/outcast5.txt",
				"wordnet/outcast8.txt", "wordnet/outcast11.txt" };
		for (int t = 0; t < filenames.length; t++) {
			In in = new In(filenames[t]);
			String[] nouns = in.readAllStrings();
			StdOut.println(filenames[t] + ": " + outcast.outcast(nouns));
		}
	}
}
