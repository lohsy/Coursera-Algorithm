import edu.princeton.cs.algs4.SuffixArrayX;

public class CircularSuffixArray {

	private SuffixArrayX sa;
	
	// circular suffix array of s
	public CircularSuffixArray(String s) {
		if (s == null)
			throw new NullPointerException();
		
		sa = new SuffixArrayX(s);
	
	}
	
	// length of s
	public int length() {
		return sa.length();
	}

	// returns index of ith sorted suffix
	public int index(int i) {
		if (i < 0 || i >= length())
			throw new IndexOutOfBoundsException();
		return sa.index(i);
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
		new CircularSuffixArray("ABRACADABRA!");
	}
}
