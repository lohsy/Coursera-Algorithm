import java.util.Arrays;

public class Brute {

	public static void main(String[] args) {
		String filename = args[0];
		// String filename = "collinear-testing/rs1423.txt";

		In in = new In(filename);
		int N = in.readInt();

		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		Point[] pts = new Point[N];
		for (int i = 0; i < N; i++) {
			pts[i] = new Point(in.readInt(), in.readInt());
			pts[i].draw();
		}
		in.close();

		Arrays.sort(pts);

		for (int a = 0; a < N - 3; a++)
			for (int b = a + 1; b < N - 2; b++)
				for (int c = b + 1; c < N - 1; c++)
					for (int d = c + 1; d < N; d++) {
						Point p1 = pts[a];
						Point p2 = pts[b];
						Point p3 = pts[c];
						Point p4 = pts[d];

						if (p1.slopeTo(p2) == p1.slopeTo(p3)
								&& p1.slopeTo(p2) == p1.slopeTo(p4)) {

							StdOut.println(p1.toString() + " -> "
									+ p2.toString() + " -> " + p3.toString()
									+ " -> " + p4.toString());
							p1.drawTo(p4);
						}
					}
	}

}
