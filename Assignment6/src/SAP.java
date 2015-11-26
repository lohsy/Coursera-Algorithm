import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

	private final Digraph digraph;

	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		if (G == null)
			throw new NullPointerException();
		this.digraph = new Digraph(G);
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		checkRange(v, w);
		return new DBFS(v, w).getAncestorLength();
	}

	// a common ancestor of v and w that participates in a shortest ancestral
	// path; -1 if no such path
	public int ancestor(int v, int w) {
		checkRange(v, w);
		return new DBFS(v, w).getAncestor();
	}

	private void checkRange(int v, int w) {
		if (v < 0 || v >= digraph.V() || w < 0 || w >= digraph.V())
			throw new IndexOutOfBoundsException();
	}

	// length of shortest ancestral path between any vertex in v and any vertex
	// in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		checkRange(v, w);
		return new DBFS(v, w).getAncestorLength();
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no
	// such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		checkRange(v, w);
		return new DBFS(v, w).getAncestor();
	}

	private void checkRange(Iterable<Integer> v, Iterable<Integer> w) {
		for (int i : v)
			if (i < 0 || i >= digraph.V())
				throw new IndexOutOfBoundsException();
		for (int i : w)
			if (i < 0 || i >= digraph.V())
				throw new IndexOutOfBoundsException();
	}

	private class DBFS {
		private int length;
		private int ancestor;

		BreadthFirstDirectedPaths vPath;
		BreadthFirstDirectedPaths wPath;

		private DBFS(int v, int w) {
			vPath = new BreadthFirstDirectedPaths(digraph, v);
			wPath = new BreadthFirstDirectedPaths(digraph, w);
			length = Integer.MAX_VALUE;
			ancestor = -1;
			findAncestor();
		}

		private DBFS(Iterable<Integer> v, Iterable<Integer> w) {
			vPath = new BreadthFirstDirectedPaths(digraph, v);
			wPath = new BreadthFirstDirectedPaths(digraph, w);
			length = Integer.MAX_VALUE;
			ancestor = -1;
			findAncestor();
		}

		private void findAncestor() {
			for (int i = 0; i < digraph.V(); i++)
				if (isAncestor(i) && length(i) < length) {
					length = length(i);
					ancestor = i;
				}
			if (ancestor == -1)
				length = -1;
		}

		private boolean isAncestor(int i) {
			return vPath.hasPathTo(i) && wPath.hasPathTo(i);
		}

		private int length(int i) {
			return vPath.distTo(i) + wPath.distTo(i);
		}

		private int getAncestorLength() {
			return length;
		}

		private int getAncestor() {
			return ancestor;
		}
	}

	// do unit testing of this class
	public static void main(String[] args) {
		String d1 = "wordnet/digraph1.txt";
		In in = new In(d1);
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);

		while (!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w = StdIn.readInt();
			int length = sap.length(v, w);
			int ancestor = sap.ancestor(v, w);
			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		}
	}
}
