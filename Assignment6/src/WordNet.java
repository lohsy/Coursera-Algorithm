public class WordNet {

	private Digraph digraph;
	private SAP sap;

	private SeparateChainingHashST<String, Queue<Integer>> allNouns;
	private String[] allSynsets;

	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null)
			throw new NullPointerException();

		allNouns = new SeparateChainingHashST<String, Queue<Integer>>();
		readSynsets(synsets);
		readHypernyms(hypernyms);
		sap = new SAP(digraph);
	}

	private void readSynsets(String filename) {
		In in = new In(filename);
		String[] raw = in.readAllLines();
		digraph = new Digraph(raw.length);
		allSynsets = new String[raw.length];

		// we assume id = row num in file
		for (int i = 0; i < raw.length; i++) {
			String[] tokens = raw[i].split(",");
			allSynsets[i] = tokens[1];
			for (String t : tokens[1].split(" ")) {
				Queue<Integer> q;
				if (allNouns.contains(t))
					q = allNouns.get(t);
				else
					q = new Queue<Integer>();
				q.enqueue(i);
				allNouns.put(t, q);
			}
		}
	}

	private void readHypernyms(String filename) {
		In in = new In(filename);
		for (String s : in.readAllLines()) {
			String[] tokens = s.split(",");
			int id1 = Integer.parseInt(tokens[0]);
			for (int i = 1; i < tokens.length; i++) {
				int id2 = Integer.parseInt(tokens[i]);
				digraph.addEdge(id1, id2);
			}
		}
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return allNouns.keys();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		if (word == null)
			throw new NullPointerException();
		return allNouns.contains(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		if (nounA == null || nounB == null)
			throw new NullPointerException();
		if (!isNoun(nounA) || !isNoun(nounB))
			throw new IllegalArgumentException();
		return sap.length(allNouns.get(nounA), allNouns.get(nounB));
	}

	// a synset (second field of synsets.txt) that is the common ancestor of
	// nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if (nounA == null || nounB == null)
			throw new NullPointerException();
		if (!isNoun(nounA) || !isNoun(nounB))
			throw new IllegalArgumentException();
		return allSynsets[sap
				.ancestor(allNouns.get(nounA), allNouns.get(nounB))];
	}

	// do unit testing of this class
	public static void main(String[] args) {
		new WordNet("wordnet/synsets.txt", "wordnet/hypernyms.txt");
	}
}
