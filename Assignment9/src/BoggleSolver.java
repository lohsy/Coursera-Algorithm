import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {

	private TrieSETAZ trie;
	private HashSet<String> words;

	// Initializes the data structure using the given array of strings as the
	// dictionary.
	// (You can assume each word in the dictionary contains only the uppercase
	// letters A through Z.)
	public BoggleSolver(String[] dictionary) {
		trie = new TrieSETAZ();

		for (String s : dictionary) {
			trie.add(s);
		}
	}

	// Returns the set of all valid words in the given Boggle board, as an
	// Iterable.
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		words = new HashSet<String>();

		int length = board.rows() * board.cols();
		for (int i = 0; i < length; i++) {
			char c = board.getLetter(getRow(i, board), getCol(i, board));
			dfs2(board, new boolean[length], i, ""+c, trie.getRoot().next[c - 'A']);
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

	private void dfs2(BoggleBoard board, boolean[] marked, int v, String prefix, Node n) {
		marked[v] = true;

		if (n == null)
			return;
		
		if(prefix.charAt(prefix.length() - 1) == 'Q') {
			dfs2(board,marked, v, prefix + 'U', n.next['U' - 'A']);
			return;
		}
		
		if(n.isString && prefix.length() >= 3 && !words.contains(prefix)){
			words.add(prefix);
		}
		
		for (int i : neighbours(v, board)) {
			if (!marked[i]) {
				char c = board.getLetter(getRow(i, board), getCol(i, board));
				dfs2(board,marked, i, prefix + c, n.next[c - 'A']);
				marked[i] = false;
			}
		}
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
		
		String [] b = {"boggle/board4x4.txt", "boggle/board-q.txt", "boggle/board-16q.txt"};
		String [] d = {"boggle/dictionary-algs4.txt", "boggle/dictionary-16q.txt"};
		
		BoggleBoard board = new BoggleBoard(b[2]);
		
		long time = System.currentTimeMillis();
		
		In in = new In(d[1]);
		String[] dictionary = in.readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		System.out.println("Constructor: " + (System.currentTimeMillis() - time)/1000.0 + " seconds.");
		
		int score = 0;
		for (String word : solver.getAllValidWords(board)) {
			StdOut.println(word);
			score += solver.scoreOf(word);
		}
		StdOut.println("Score = " + score);
		
		System.out.println("Solver: " + (System.currentTimeMillis() - time)/1000.0 + " seconds.");
	}
	
    // R-way trie node
	 private static class Node {
		private Node[] next = new Node[TrieSETAZ.R];
		private boolean isString;
	 }

	 
	private class TrieSETAZ implements Iterable<String> {
	    private static final int R = 26;//A-Z
	    private Node root;      // root of trie
	    private int N;          // number of keys in trie

	    public TrieSETAZ() {
	    }

	    public Node getRoot() {
	    	return root;
	    }
	    
	    public boolean contains(String key) {
	        Node x = get(root, key, 0);
	        if (x == null) return false;
	        return x.isString;
	    }

	    private Node get(Node x, String key, int d) {
	        if (x == null) return null;
	        if (d == key.length()) return x;
	        char c = key.charAt(d);
	        return get(x.next[c  - 'A'], key, d+1);
	    }

	    public void add(String key) {
	        root = add(root, key, 0);
	    }

	    private Node add(Node x, String key, int d) {
	        if (x == null) x = new Node();
	        if (d == key.length()) {
	            if (!x.isString) N++;
	            x.isString = true;
	        }
	        else {
	            char c = key.charAt(d);
	            x.next[c - 'A'] = add(x.next[c - 'A'], key, d+1);
	        }
	        return x;
	    }

	    public int size() {
	        return N;
	    }

	    public boolean isEmpty() {
	        return size() == 0;
	    }

	    public Iterator<String> iterator() {
	        return keysWithPrefix("").iterator();
	    }

	    public Iterable<String> keysWithPrefix(String prefix) {
	        Queue<String> results = new Queue<String>();
	        Node x = get(root, prefix, 0);
	        collect(x, new StringBuilder(prefix), results);
	        return results;
	    }

	    private void collect(Node x, StringBuilder prefix, Queue<String> results) {
	        if (x == null) return;
	        if (x.isString) results.enqueue(prefix.toString());
	        for (char c = 0; c < R; c++) {
	            prefix.append(c);
	            collect(x.next[c], prefix, results);
	            prefix.deleteCharAt(prefix.length() - 1);
	        }
	    }
  
	    public Iterable<String> keysThatMatch(String pattern) {
	        Queue<String> results = new Queue<String>();
	        StringBuilder prefix = new StringBuilder();
	        collect(root, prefix, pattern, results);
	        return results;
	    }
	        
	    private void collect(Node x, StringBuilder prefix, String pattern, Queue<String> results) {
	        if (x == null) return;
	        int d = prefix.length();
	        if (d == pattern.length() && x.isString)
	            results.enqueue(prefix.toString());
	        if (d == pattern.length())
	            return;
	        char c = pattern.charAt(d);
	        if (c == '.') {
	            for (char ch = 0; ch < R; ch++) {
	                prefix.append(ch);
	                collect(x.next[ch], prefix, pattern, results);
	                prefix.deleteCharAt(prefix.length() - 1);
	            }
	        }
	        else {
	            prefix.append(c);
	            collect(x.next[c], prefix, pattern, results);
	            prefix.deleteCharAt(prefix.length() - 1);
	        }
	    }

	    public String longestPrefixOf(String query) {
	        int length = longestPrefixOf(root, query, 0, -1);
	        if (length == -1) return null;
	        return query.substring(0, length);
	    }

	    private int longestPrefixOf(Node x, String query, int d, int length) {
	        if (x == null) return length;
	        if (x.isString) length = d;
	        if (d == query.length()) return length;
	        char c = query.charAt(d);
	        return longestPrefixOf(x.next[c], query, d+1, length);
	    }

	    public void delete(String key) {
	        root = delete(root, key, 0);
	    }

	    private Node delete(Node x, String key, int d) {
	        if (x == null) return null;
	        if (d == key.length()) {
	            if (x.isString) N--;
	            x.isString = false;
	        }
	        else {
	            char c = key.charAt(d);
	            x.next[c] = delete(x.next[c], key, d+1);
	        }

	        // remove subtrie rooted at x if it is completely empty
	        if (x.isString) return x;
	        for (int c = 0; c < R; c++)
	            if (x.next[c] != null)
	                return x;
	        return null;
	    }
	}
}
