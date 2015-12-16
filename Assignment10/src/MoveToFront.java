import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

	// apply move-to-front encoding, reading from standard input and writing to
	// standard output
	public static void encode() {

		char chars[] = new char[256];
		for (int i = 0; i < chars.length; i++) {
			chars[i] = (char) i;
		}

		while (!BinaryStdIn.isEmpty()) {
			char c = BinaryStdIn.readChar();
			for (int i = 0; i < chars.length; i++) {
				if (chars[i] == c) {
					moveFront(chars, i);
					BinaryStdOut.write(i, 8);
					break;
				}
			}
		}
		BinaryStdOut.close();
	}

	private static int moveFront(char[] chars, int index) {
		char tmp = chars[index];
		for (int i = index - 1; i >= 0; i--) {
			chars[i + 1] = chars[i];
		}
		chars[0] = tmp;
		return index;
	}

	// apply move-to-front decoding, reading from standard input and writing to
	// standard output
	public static void decode() {
		char chars[] = new char[256];
		for (int i = 0; i < chars.length; i++) {
			chars[i] = (char) i;
		}

		while (!BinaryStdIn.isEmpty()) {
			char c = BinaryStdIn.readChar();
			char out = chars[c];
			moveFront(chars, c);
			BinaryStdOut.write(out, 8);
		}
		BinaryStdOut.close();
	}

	// if args[0] is '-', apply move-to-front encoding
	// if args[0] is '+', apply move-to-front decoding
	public static void main(String[] args) {

		if (args[0].charAt(0) == '-')
			MoveToFront.encode();
		else if (args[0].charAt(0) == '+')
			MoveToFront.decode();
		else {
			BinaryStdOut.write("Invalid argument");
		}
	}
}