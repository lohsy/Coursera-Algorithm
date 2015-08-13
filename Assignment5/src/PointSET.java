public class PointSET {

	private SET<Point2D> set;

	// construct an empty set of points
	public PointSET() {
		set = new SET<Point2D>();
	}

	// is the set empty?
	public boolean isEmpty() {
		return set.isEmpty();
	}

	// number of points in the set
	public int size() {
		return set.size();
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null)
			throw new NullPointerException();
		set.add(p);
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null)
			throw new NullPointerException();
		return set.contains(p);
	}

	// draw all points to standard draw
	public void draw() {
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(.01);

		for (Point2D p : set)
			StdDraw.point(p.x(), p.y());
		StdDraw.show(0);
	}

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new NullPointerException();

		Queue<Point2D> q = new Queue<Point2D>();
		for (Point2D p : set) {
			if (rect.contains(p))
				q.enqueue(p);
		}
		return q;
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new NullPointerException();
		if (isEmpty())
			return null;

		Point2D nearest = null;
		double distance = Double.MAX_VALUE;
		for (Point2D point : set) {
			double curDistance = point.distanceTo(p);
			if (curDistance < distance) {
				distance = curDistance;
				nearest = point;
			}
		}
		return nearest;
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
		String filename = args[0];
		In in = new In(filename);
		StdDraw.show(0);

		// initialize the data structures with N points from standard input
		PointSET brute = new PointSET();
		while (!in.isEmpty()) {
			double x = in.readDouble();
			double y = in.readDouble();
			Point2D p = new Point2D(x, y);
			brute.insert(p);
		}

		brute.draw();
	}
}
