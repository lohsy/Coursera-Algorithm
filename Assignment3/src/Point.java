/*************************************************************************
 * Name: Michael Loh 
 * Email: michaellohsy@gmail.com
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

	// compare points by slope
	public final Comparator<Point> SLOPE_ORDER = new Comparator<Point>() {

		@Override
		public int compare(Point pt1, Point pt2) {
			double slope1 = slopeTo(pt1);
			double slope2 = slopeTo(pt2);

			int result;
			if (slope1 < slope2)
				result = -1;
			else if (slope1 > slope2)
				result = 1;
			else
				result = 0;

			return result;
		}
	}; // YOUR DEFINITION HERE

	private final int x; // x coordinate
	private final int y; // y coordinate

	// create the point (x, y)
	public Point(int x, int y) {
		/* DO NOT MODIFY */
		this.x = x;
		this.y = y;
	}

	// plot this point to standard drawing
	public void draw() {
		/* DO NOT MODIFY */
		StdDraw.point(x, y);
	}

	// draw line between this point and that point to standard drawing
	public void drawTo(Point that) {
		/* DO NOT MODIFY */
		StdDraw.line(this.x, this.y, that.x, that.y);
	}

	// slope between this point and that point
	public double slopeTo(Point that) {
		double slope;

		// degenerate line segment
		if (that.x == this.x && that.y == this.y)
			slope = Double.NEGATIVE_INFINITY;
		else if (that.x == this.x) // vertical line segment
			slope = Double.POSITIVE_INFINITY;
		else if (that.y == this.y) // horizontal line segment
			slope = 0;
		else
			slope = (double) (that.y - this.y) / (double) (that.x - this.x);

		return slope;
	}

	// is this point lexicographically smaller than that one?
	// comparing y-coordinates and breaking ties by x-coordinates
	public int compareTo(Point that) {
		int result;
		if (this.y < that.y)
			result = -1;
		else if (this.y > that.y)
			result = 1;
		else if (this.x < that.x)
			result = -1;
		else if (this.x > that.x)
			result = 1;
		else
			result = 0;
		return result;
	}

	// return string representation of this point
	public String toString() {
		/* DO NOT MODIFY */
		return "(" + x + ", " + y + ")";
	}

	// unit test
	public static void main(String[] args) {
		/* YOUR CODE HERE */
	}
}
