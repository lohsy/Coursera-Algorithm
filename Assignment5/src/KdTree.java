public class KdTree {

	private int size;
	private Node root;

	private class Node {
		private Point2D p;
		private RectHV rect;
		private Node lb, rt;

		public Node(Point2D p, RectHV rect) {
			this.p = p;
			if (rect == null)
				this.rect = new RectHV(0, 0, 1, 1);
			else
				this.rect = rect;
			lb = rt = null;
		}
	}

	// construct an empty set of points
	public KdTree() {
		size = 0;
		root = null;
	}

	// is the set empty?
	public boolean isEmpty() {
		return size == 0;
	}

	// number of points in the set
	public int size() {
		return size;
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null)
			throw new NullPointerException();

		if (isEmpty()) {
			root = new Node(p, null);
			size++;
		} else {
			root = insert(root, p, true);
		}
	}

	/**
	 * @param c
	 *            Current Node
	 * @param p
	 *            Point to insert
	 * @param x
	 *            true is x is to be compared, false if y is to be compared
	 * @return root
	 */
	private Node insert(Node c, Point2D p, boolean x) {
		// go left if smaller
		if (x) {
			if (p.x() < c.p.x()) {
				// if empty - insert
				if (c.lb == null) {
					c.lb = new Node(p, new RectHV(c.rect.xmin(), c.rect.ymin(),
							c.p.x(), c.rect.ymax()));
					size++;
				} else { // else continue recursing
					c.lb = insert(c.lb, p, !x);
				}
			} else { // go right
				// if empty - insert
				if (c.rt == null) {
					c.rt = new Node(p, new RectHV(c.p.x(), c.rect.ymin(),
							c.rect.xmax(), c.rect.ymax()));
					size++;
				} else { // else continue recursing
					c.rt = insert(c.rt, p, !x);
				}
			}
		} else {
			if (p.y() < c.p.y()) {
				// if empty - insert
				if (c.lb == null) {
					c.lb = new Node(p, new RectHV(c.rect.xmin(), c.rect.ymin(),
							c.rect.xmax(), c.p.y()));
					size++;
				} else { // else continue recursing
					c.lb = insert(c.lb, p, !x);
				}
			} else { // go right
				// if empty - insert
				if (c.rt == null) {
					c.rt = new Node(p, new RectHV(c.rect.xmin(), c.p.y(),
							c.rect.xmax(), c.rect.ymax()));
					size++;
				} else { // else continue recursing
					c.rt = insert(c.rt, p, !x);
				}
			}
		}
		return c;
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null)
			throw new NullPointerException();

		return contains(root, p, true);
	}

	private boolean contains(Node c, Point2D p, boolean x) {
		if (c == null)
			return false;
		if (c.p.equals(p))
			return true;

		if (x) {
			if (p.x() < c.p.x())
				return contains(c.lb, p, !x);
			else
				return contains(c.rt, p, !x);
		} else {
			if (p.y() < c.p.y())
				return contains(c.lb, p, !x);
			else
				return contains(c.rt, p, !x);
		}
	}

	// draw all points to standard draw
	public void draw() {
		draw(root, true);
	}

	private void draw(Node c, boolean x) {
		// draw current point
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(.01);
		StdDraw.point(c.p.x(), c.p.y());

		// draw line that divides the boundary
		if (x) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.setPenRadius();
			StdDraw.line(c.p.x(), c.rect.ymin(), c.p.x(), c.rect.ymax());
		} else {
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.setPenRadius();
			StdDraw.line(c.rect.xmin(), c.p.y(), c.rect.xmax(), c.p.y());
		}

		// recursively
		if (c.lb != null)
			draw(c.lb, !x);
		if (c.rt != null)
			draw(c.rt, !x);

		StdDraw.show(0);
	}

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new NullPointerException();

		Queue<Point2D> q = new Queue<Point2D>();
		range(root, rect, q);
		return q;
	}

	private void range(Node c, RectHV rect, Queue<Point2D> q) {
		// if null return
		if (c == null)
			return;

		// Add point if inside rect
		if (rect.contains(c.p)) {
			q.enqueue(c.p);
		}

		// check if need search subtrees
		if (c.lb != null && c.lb.rect.intersects(rect))
			range(c.lb, rect, q);
		if (c.rt != null && c.rt.rect.intersects(rect))
			range(c.rt, rect, q);
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new NullPointerException();
		if (isEmpty())
			return null;

		return nearest(root, p, root.p, true);
	}

	private Point2D nearest(Node c, Point2D p, Point2D nearest, boolean x) {

		// if null return
		if (c == null)
			return nearest;

		// Check if distance is smaller
		Double nD = nearest.distanceSquaredTo(p);
		Double cD = c.p.distanceSquaredTo(p);
		if (cD < nD) {
			nD = cD;
			nearest = c.p;
		}

		// Recursive search
		if (x) {
			// left priority
			if (p.x() < c.p.x()) {
				// recurse only if point to rect distance is less than min
				// distance
				if (c.lb != null && c.lb.rect.distanceSquaredTo(p) < nD)
					nearest = nearest(c.lb, p, nearest, !x);
				if (c.rt != null && c.rt.rect.distanceSquaredTo(p) < nD)
					nearest = nearest(c.rt, p, nearest, !x);
			} else { // right priority
				if (c.rt != null && c.rt.rect.distanceSquaredTo(p) < nD)
					nearest = nearest(c.rt, p, nearest, !x);
				if (c.lb != null && c.lb.rect.distanceSquaredTo(p) < nD)
					nearest = nearest(c.lb, p, nearest, !x);
			}
		} else {
			// right priority
			if (p.y() < c.p.y()) {
				if (c.lb != null && c.lb.rect.distanceSquaredTo(p) < nD)
					nearest = nearest(c.lb, p, nearest, !x);
				if (c.rt != null && c.rt.rect.distanceSquaredTo(p) < nD)
					nearest = nearest(c.rt, p, nearest, !x);
			} else { // right priority
				if (c.rt != null && c.rt.rect.distanceSquaredTo(p) < nD)
					nearest = nearest(c.rt, p, nearest, !x);
				if (c.lb != null && c.lb.rect.distanceSquaredTo(p) < nD)
					nearest = nearest(c.lb, p, nearest, !x);
			}
		}

		return nearest;
	}

	public static void main(String[] args) {
		String filename = args[0];
		In in = new In(filename);
		StdDraw.show(0);

		// initialize the data structures with N points from standard input
		KdTree kd = new KdTree();
		while (!in.isEmpty()) {
			double x = in.readDouble();
			double y = in.readDouble();
			Point2D p = new Point2D(x, y);
			kd.insert(p);
		}

		System.out.println(kd.size);
		kd.draw();

		kd.nearest(new Point2D(0.5, 0.1));
	} // unit testing of the methods (optional)
}
