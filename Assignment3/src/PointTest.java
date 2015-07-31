import static org.junit.Assert.*;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

public class PointTest {

	@Test
	public void testConstructor() {
		int px = 1;
		int py = 2;
		Point p = new Point(1, 2);

		assertPoint(p, px, py);
	}

	@Test
	public void testCompare() {
		int N = 10;
		Point[] pts = new Point[N];

		// Test 1
		for (int i = 0; i < N; i++)
			pts[i] = new Point(i, i);
		StdRandom.shuffle(pts);
		Arrays.sort(pts);
		for (int i = 0; i < N; i++)
			assertPoint(pts[i], i, i);

		// Test 2
		for (int i = N - 1; i >= 0; i--)
			pts[i] = new Point(i, i);
		Arrays.sort(pts);
		for (int i = 0; i < N; i++)
			assertPoint(pts[i], i, i);

		// Test 3
		for (int i = 0; i < N; i++)
			pts[i] = new Point(0, i);
		for (int i = 0; i < N; i++)
			assertPoint(pts[i], 0, i);

		// Test 4
		for (int i = 0; i < N; i++)
			pts[i] = new Point(i, 0);
		for (int i = 0; i < N; i++)
			assertPoint(pts[i], i, 0);

	}

	@Test
	public void testSlopeTo() {

		// Test 1
		Point p1 = new Point(0, 0);
		Point p2 = new Point(10, 10);
		Assert.assertEquals(1.0, p1.slopeTo(p2), 0.1);
		Assert.assertEquals(1.0, p2.slopeTo(p1), 0.1);
		Assert.assertEquals(Double.NEGATIVE_INFINITY, p1.slopeTo(p1), 0.1);
		Assert.assertEquals(Double.NEGATIVE_INFINITY, p2.slopeTo(p2), 0.1);

		// Test 2
		Point q1 = new Point(1234, 5678);
		Point q2 = new Point(14000, 10000);
		Point q3 = new Point(18000, 10000);
		Point q4 = new Point(19000, 10000);
		Assert.assertEquals((10000.0 - 5678.0) / (14000.0 - 1234.0),
				q1.slopeTo(q2), 0);
		Assert.assertEquals((10000.0 - 5678.0) / (18000.0 - 1234.0),
				q1.slopeTo(q3), 0);
		Assert.assertEquals((10000.0 - 5678.0) / (19000.0 - 1234.0),
				q1.slopeTo(q4), 0);

	}

	private void assertPoint(Point p, int px, int py) {
		String s = p.toString().substring(1, p.toString().length() - 1);
		int x = Integer.parseInt(s.split(",")[0].trim());
		int y = Integer.parseInt(s.split(",")[1].trim());

		Assert.assertEquals(px, x);
		Assert.assertEquals(py, y);
	}
}
