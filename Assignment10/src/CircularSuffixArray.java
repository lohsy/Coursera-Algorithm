import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {

	private Integer [] indexes;
	
	// circular suffix array of s
	public CircularSuffixArray(String s) {
		if(s == null)
			throw new NullPointerException();
		
		generateSuffixArray(s);
	}
	
	// length of s
	public int length() {
		return indexes.length;
	}

	// returns index of ith sorted suffix
	public int index(int i) {
		if(i<0 || i>=length())
			throw new IndexOutOfBoundsException();
		return indexes[i];
	}

	private void generateSuffixArray(final String s) {
		
		indexes = new Integer [s.length()];
		for(int i=0;i<indexes.length;i++)
			indexes[i] = i;
		
		Arrays.sort(indexes, new Comparator<Integer>() {

			@Override
			public int compare(Integer i1, Integer i2) {
				
				for(int i=0;i<s.length();i++) {
					if(s.charAt((i1+i)%length()) < s.charAt((i2+i)%length()))
						return -1;
					if(s.charAt((i1+i)%length()) > s.charAt((i2+i)%length()))
						return 1;
				}
				
				return 0;
			}
		});
		
		//for(int i : indexes) {
		//	System.out.println(s.charAt(i) + " " + i);
		//}
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
		new CircularSuffixArray("ABRACADABRA!");
	}
}
