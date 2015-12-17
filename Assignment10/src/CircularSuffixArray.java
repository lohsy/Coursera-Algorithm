
public class CircularSuffixArray {

    private static final int R             = 256;   // extended ASCII alphabet size
    private static final int CUTOFF        =  15;   // cutoff to insertion sort
	
	//private Integer [] indexes;
    private int [] indexes;
	
	// circular suffix array of s
	public CircularSuffixArray(String s) {
		if (s == null)
			throw new NullPointerException();

		indexes = new int[s.length()];
		for (int i = 0; i < indexes.length; i++)
			indexes[i] = i;
		int[] aux = new int[s.length()];

		sort(s, indexes, 0, s.length() - 1, aux);
		
		//System.out.println("MSD");
		//for(int i : indexes) {
		//	System.out.println(s.charAt(i) + " " + i);
		//}
		//System.out.println("Done\n\n");
		//
		//System.out.println("OLD");
		//generateSuffixArray(s);
	}
	
	// length of s
	public int length() {
		return indexes.length;
	}

	// returns index of ith sorted suffix
	public int index(int i) {
		if (i < 0 || i >= length())
			throw new IndexOutOfBoundsException();
		return indexes[i];
	}

//	private void generateSuffixArray(final String s) {
//		
//		indexes = new Integer[s.length()];
//		for (int i = 0; i < indexes.length; i++)
//			indexes[i] = i;
//
//		Arrays.sort(indexes, new Comparator<Integer>() {
//
//			@Override
//			public int compare(Integer i1, Integer i2) {
//
//				for (int i = 0; i < s.length(); i++) {
//					if (s.charAt((i1 + i) % length()) < s.charAt((i2 + i)
//							% length()))
//						return -1;
//					if (s.charAt((i1 + i) % length()) > s.charAt((i2 + i)
//							% length()))
//						return 1;
//				}
//
//				return 0;
//			}
//		});
//		
//		for(int i : indexes) {
//			System.out.println(s.charAt(i) + " " + i);
//		}
//	}
	
	// sort from indexes[lo] to indexes[hi], which are indexes to chars of s
	private static void sort(String s, int[] indexes, int lo, int hi, int[] aux) {

		// cutoff to insertion sort for small subarrays
		if (hi <= lo + CUTOFF) {
			insertion(s, indexes, lo, hi);
			return;
		}

		// compute frequency counts
		int[] count = new int[R + 2];
		for (int i = lo; i <= hi; i++) {
			int c = s.charAt(indexes[i]);
			count[c + 2]++;
		}

		// transform counts to indicies
		for (int r = 0; r < R + 1; r++)
			count[r + 1] += count[r];

		// distribute
		for (int i = lo; i <= hi; i++) {
			int c = s.charAt(indexes[i]);
			aux[count[c + 1]++] = indexes[i];
		}

		// copy back
		for (int i = lo; i <= hi; i++)
			indexes[i] = aux[i - lo];

		// recursively sort for each character (excludes sentinel -1)
		for (int r = 0; r < R; r++)
			sort(s, indexes, lo + count[r], lo + count[r + 1] - 1, aux);
	}
    
    // insertion sort a[lo..hi]
	private static void insertion(String s, int[] indexes, int lo, int hi) {
		for (int i = lo; i <= hi; i++)
			for (int j = i; j > lo && less(s, indexes[j], indexes[j - 1]); j--)
				exch(indexes, j, j - 1);
	}

	// is string starting at offset1 lesser then string starting at offset2 
	private static boolean less(String s, int offset1, int offset2) {
		for (int i = 0; i < s.length(); i++) {
			int index1 = (offset1 + i) % s.length();
			int index2 = (offset2 + i) % s.length();
			if (s.charAt(index1) < s.charAt(index2))
				return true;
			if (s.charAt(index1) > s.charAt(index2))
				return false;
		}
		return false;
	}
    
    // exchange indexes i and j
	private static void exch(int[] indexes, int i, int j) {
		int temp = indexes[i];
		indexes[i] = indexes[j];
		indexes[j] = temp;
	}
    
	// unit testing of the methods (optional)
	public static void main(String[] args) {
		new CircularSuffixArray("abcdefghijklmnopqrstuvwxyz0123456789");
	}
}
