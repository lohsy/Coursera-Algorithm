import java.util.Arrays;

public class Fast {

	public static void main(String[] args) {
		String filename = args[0];
		// String filename = "collinear-testing/input8.txt";

		In in = new In(filename);
		int N = in.readInt();
		System.out.println(N);
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		Point[] pts = new Point[N];
		for (int i = 0; i < N; i++) {
			pts[i] = new Point(in.readInt(), in.readInt());
			pts[i].draw();
		}
		in.close();

		for (int i = 0; i < N; i++) {
			Point[] tmp = Arrays.copyOf(pts, pts.length);
			Arrays.sort(tmp, pts[i].SLOPE_ORDER);

			for (int j = 1; j < N; j++) {
				double curSlope = pts[i].slopeTo(tmp[j]);
				int count = 1;
				while (j + 1 < N && pts[i].slopeTo(tmp[j + 1]) == curSlope) {
					j++;
					count++;
				}
				if (count >= 3) {
					Point[] col = Arrays.copyOfRange(tmp, j - count + 1, j + 2);
					col[col.length - 1] = pts[i];
					boolean isDuplicate = false;
					for (int k = 0; k < col.length - 1; k++) {
						if (pts[i].compareTo(col[k]) < 0) {
							isDuplicate = true;
							break;
						}
					}
					if (!isDuplicate) {
						Arrays.sort(col);
						for (int k = 0; k < col.length; k++) {
							if (k != col.length - 1)
								StdOut.print(col[k].toString() + " -> ");
							else
								StdOut.print(col[k].toString() + "\n");
						}
						col[0].drawTo(col[col.length - 1]);
					}
				}
			}
		}
	}
}
