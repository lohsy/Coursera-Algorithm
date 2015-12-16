import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
    	
		String message = BinaryStdIn.readString();
		CircularSuffixArray csa = new CircularSuffixArray(message);

		StringBuilder sb = new StringBuilder();
		int first = 0;

		for (int i = 0; i < message.length(); i++) {
			int charPosition = (csa.index(i) + message.length() - 1)
					% message.length();
			sb.append(message.charAt(charPosition));
			if (csa.index(i) == 0)
				first = i;
		}

		BinaryStdOut.write(first);
		BinaryStdOut.write(sb.toString(), 8);
		BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
    	
		int first = BinaryStdIn.readInt();
		String t = BinaryStdIn.readString();
		char[] sorted = t.toCharArray();

		// sort the chars
		Arrays.sort(sorted);

		// obtain next array
		int[] next = new int[t.length()];

		int count = 0;
		for (int i = 0; i < sorted.length; i++) {

			if (i > 0 && sorted[i] == sorted[i - 1])
				count++;
			else
				count = 0;

			int countdown = count;
			for (int j = 0; j < t.length(); j++) {

				if (sorted[i] == t.charAt(j)) {
					if (countdown == 0) {
						next[i] = j;
						break;
					} else
						countdown--;
				}
			}
		}

		// build message
		StringBuilder sb = new StringBuilder();

		int index = first;
		for(int i=0;i<t.length();i++) {
			sb.append(sorted[index]);
			index = next[index];
		}

		BinaryStdOut.write(sb.toString(), 8);
		BinaryStdOut.close();
    	
//    	System.out.println("first: " + first);
//    	System.out.println("encoded: " + t);
//    	
//    	System.out.println("sorted encoded:");
//    	for(char c : sorted)
//    		System.out.print(c + " ");
//    	System.out.println();
//    	
//    	System.out.println("Next array:");
//    	for(int x : next)
//    		System.out.print(x + " ");
//    	System.out.println();
//    	
//    	System.out.println("Message");
//    	System.out.println(sb.toString());
    }

	// if args[0] is '-', apply Burrows-Wheeler encoding
	// if args[0] is '+', apply Burrows-Wheeler decoding
	public static void main(String[] args) {
		if (args[0].charAt(0) == '-')
			BurrowsWheeler.encode();
		else if (args[0].charAt(0) == '+')
			BurrowsWheeler.decode();
		else {
			BinaryStdOut.write("Invalid argument");
		}
	}
}

