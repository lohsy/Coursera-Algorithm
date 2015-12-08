import java.util.ArrayList;
import java.util.HashSet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

public class BoggleSolver {

	private TST<Integer> trie;
	private HashSet<String> words;

	// Initializes the data structure using the given array of strings as the
	// dictionary.
	// (You can assume each word in the dictionary contains only the uppercase
	// letters A through Z.)
	public BoggleSolver(String[] dictionary) {
		trie = new TST<Integer>();

		for (String s : dictionary) {
			trie.put(s, s.length());
		}
	}

	// Returns the set of all valid words in the given Boggle board, as an
	// Iterable.
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		words = new HashSet<String>();

		int length = board.rows() * board.cols();
		for (int i = 0; i < length; i++) {
			// System.out.println("Iteration " + i);
			if (isQ(i, board))
				dfs(board, new boolean[length], i, "QU");
			else
				dfs(board, new boolean[length], i,
						""+ board.getLetter(getRow(i, board), getCol(i, board)));
		}

		return words;
	}

	private int getRow(int v, BoggleBoard board) {
		return v / board.cols();
	}

	private int getCol(int v, BoggleBoard board) {
		return v % board.cols();
	}

	private int getV(int row, int col, BoggleBoard board) {
		if (row >= board.rows() || row < 0 || col >= board.cols() || col < 0)
			return -1;
		return board.cols() * row + col;
	}

	private boolean isQ(int v, BoggleBoard board) {
		return board.getLetter(getRow(v, board), getCol(v, board)) == 'Q';
	}

	private void dfs(BoggleBoard board, boolean[] marked, int v, String prefix) {
		marked[v] = true;

		// System.out.print(prefix +"\t[" + getRow(v, board) + "][" + getCol(v,
		// board) + "]");

		// if prefix exists continue, else just terminate
		if (trie.keysWithPrefix(prefix).iterator().hasNext()) {
			// System.out.print("\t prefix exists");
			// if word exists, add to set
			if (prefix.length() >= 3 && trie.contains(prefix)
					&& !words.contains(prefix)) {
				words.add(prefix);
				// System.out.print("\t word exists");
			}
			// System.out.println();
			// iterate over non marked neighbours
			for (int i : neighbours(v, board)) {
				if (!marked[i]) {
					if (isQ(i, board))
						dfs(board, marked, i, prefix + "QU");
					else
						dfs(board,marked, i, prefix + 
								board.getLetter(getRow(i, board), getCol(i, board)));
					marked[i] = false;
				}
			}
		}
		// System.out.println();
	}

	private Iterable<Integer> neighbours(int v, BoggleBoard board) {
		ArrayList<Integer> n = new ArrayList<Integer>();
		int row = getRow(v, board);
		int col = getCol(v, board);

		if (getV(row, col, board) < 0)
			return n;

		if (!(getV(row - 1, col - 1, board) < 0))
			n.add(getV(row - 1, col - 1, board));
		if (!(getV(row - 1, col, board) < 0))
			n.add(getV(row - 1, col, board));
		if (!(getV(row - 1, col + 1, board) < 0))
			n.add(getV(row - 1, col + 1, board));
		if (!(getV(row, col - 1, board) < 0))
			n.add(getV(row, col - 1, board));
		if (!(getV(row, col + 1, board) < 0))
			n.add(getV(row, col + 1, board));
		if (!(getV(row + 1, col - 1, board) < 0))
			n.add(getV(row + 1, col - 1, board));
		if (!(getV(row + 1, col, board) < 0))
			n.add(getV(row + 1, col, board));
		if (!(getV(row + 1, col + 1, board) < 0))
			n.add(getV(row + 1, col + 1, board));
		return n;
	}

	// Returns the score of the given word if it is in the dictionary, zero
	// otherwise.
	// (You can assume the word contains only the uppercase letters A through
	// Z.)
	public int scoreOf(String word) {
		if (!trie.contains(word))
			return 0;
		if (word.length() <= 2)
			return 0;
		else if (word.length() <= 4)
			return 1;
		else if (word.length() <= 5)
			return 2;
		else if (word.length() <= 6)
			return 3;
		else if (word.length() <= 7)
			return 5;
		else
			return 11;
	}

	public static void main(String[] args) {
		In in = new In("boggle/dictionary-algs4.txt");
		String[] dictionary = in.readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard("boggle/board-q.txt");
		// BoggleBoard board = new BoggleBoard("boggle/board4x4.txt");
		int score = 0;
		for (String word : solver.getAllValidWords(board)) {
			StdOut.println(word);
			score += solver.scoreOf(word);
		}
		StdOut.println("Score = " + score);
	}
}
