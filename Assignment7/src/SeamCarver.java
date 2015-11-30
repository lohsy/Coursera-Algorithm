import java.awt.Color;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

public class SeamCarver {

	private final Picture pic;
	private double[][] e;
	private double[] distTo;
	private int[] edgeTo;
	private Color[][] intensity;

	private boolean isTransposed;
	private int width, height;

	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture) {
		if (picture == null)
			throw new NullPointerException();

		pic = new Picture(picture);
		width = picture.width();
		height = picture.height();

		intensity = new Color[width()][height()];
		for (int j = 0; j < height(); j++)
			for (int i = 0; i < width(); i++)
				intensity[i][j] = pic.get(i, j);

		e = new double[width()][height()];
		for (int j = 0; j < height(); j++)
			for (int i = 0; i < width(); i++)
				e[i][j] = energy(i, j);

		isTransposed = false;
	}

	// current picture
	public Picture picture() {
		Picture result = new Picture(width(), height());

		for (int i = 0; i < result.width(); i++)
			for (int j = 0; j < result.height(); j++)
				result.set(i, j, intensity[i][j]);

		return result;
	}

	// width of current picture
	public int width() {
		return width;
	}

	// height of current picture
	public int height() {
		return height;
	}

	// energy of pixel at column x and row y
	public double energy(int x, int y) {
		if (x < 0 || x >= width() || y < 0 || y >= height())
			throw new IndexOutOfBoundsException();

		if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
			return 1000.0;
		else {
			Color top = intensity[x][y - 1];
			Color bot = intensity[x][y + 1];
			Color left = intensity[x - 1][y];
			Color right = intensity[x + 1][y];

			int diffx = getPixelDiff(left, right);
			int diffy = getPixelDiff(top, bot);

			return Math.sqrt(diffx + diffy);
		}
	}

	private int getPixelDiff(Color c1, Color c2) {
		return getColorDiff(c1.getRed(), c2.getRed())
				+ getColorDiff(c1.getGreen(), c2.getGreen())
				+ getColorDiff(c1.getBlue(), c2.getBlue());
	}

	private int getColorDiff(int i, int j) {
		return (i - j) * (i - j);
	}

	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		if (!isTransposed) {
			transpose();
			isTransposed = true;
		}
		findSeam();
		return buildSeam();
	}

	// transpose energy map
	private void transpose() {
		double[][] newE = new double[e[0].length][e.length];

		for (int i = 0; i < newE.length; i++) {
			for (int j = 0; j < newE[0].length; j++) {
				newE[i][j] = e[j][i];
			}
		}
		e = newE;
	}

	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		if (isTransposed) {
			transpose();
			isTransposed = false;
		}
		findSeam();
		return buildSeam();
	}

	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		if (seam == null)
			throw new NullPointerException();
		if (seam.length != width())
			throw new IllegalArgumentException();
		for (int i = 0; i < seam.length - 1; i++)
			if (Math.abs(seam[i] - seam[i + 1]) > 1)
				throw new IllegalArgumentException();
		for (int i = 0; i < seam.length; i++)
			if (seam[i] < 0 || seam[i] >= height())
				throw new IllegalArgumentException();

		height--;
		for (int i = 0; i < seam.length; i++)
			for (int j = seam[i]; j < intensity[0].length - 1; j++)
				intensity[i][j] = intensity[i][j + 1];

		// create a transposed energy map
		double[][] newE = new double[e.length - 1][e[0].length];
		for (int j = 0; j < newE[0].length; j++)
			for (int i = 0; i < newE.length; i++) {
				if (i < seam[j] - 1)
					newE[i][j] = e[i][j];
				else if (seam[j] - i == 1 || seam[j] - i == 0)
					newE[i][j] = energy(j, i);
				else
					newE[i][j] = e[i + 1][j];
			}
		e = newE;
	}

	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		if (seam == null)
			throw new NullPointerException();
		if (seam.length != height())
			throw new IllegalArgumentException();
		for (int i = 0; i < seam.length - 1; i++)
			if (Math.abs(seam[i] - seam[i + 1]) > 1)
				throw new IllegalArgumentException();
		for (int i = 0; i < seam.length; i++)
			if (seam[i] < 0 || seam[i] >= width())
				throw new IllegalArgumentException();

		width--;
		for (int j = 0; j < seam.length; j++)
			for (int i = seam[j]; i < intensity.length - 1; i++)
				intensity[i][j] = intensity[i + 1][j];

		double[][] newE = new double[width()][height()];
		for (int j = 0; j < height(); j++)
			for (int i = 0; i < width(); i++) {
				if (i < seam[j] - 1)
					newE[i][j] = e[i][j];
				else if (seam[j] - i == 1 || seam[j] - i == 0)
					newE[i][j] = energy(i, j);
				else
					newE[i][j] = e[i + 1][j];
			}
		e = newE;
	}

	private void findSeam() {
		// create 2 extra vertex
		// 1 at top and 1 at bottom that connects to energy
		edgeTo = new int[width() * height() + 2];
		distTo = new double[width() * height() + 2];
		for (int i = 1; i < distTo.length; i++)
			distTo[i] = Double.POSITIVE_INFINITY;

		// relax top vertex (linked to all vertex in top row)
		for (int i = 0; i < e.length; i++) {
			if (e[i][0] < distTo[coordsToIndex(i, 0)]) {
				distTo[coordsToIndex(i, 0)] = e[i][0];
				edgeTo[coordsToIndex(i, 0)] = 0;
			}
		}

		// consider each pixel in topological order
		// (left to right, top to bottom) except bot row
		for (int j = 0; j < e[0].length - 1; j++)
			for (int i = 0; i < e.length; i++)
				relax(i, j);

		// relax bot vertex (linked to all vertex in bot row)
		for (int i = 0; i < e.length; i++) {
			if (distTo[coordsToIndex(i, e[0].length - 1)] < distTo[distTo.length - 1]) {
				distTo[distTo.length - 1] = distTo[coordsToIndex(i,
						e[0].length - 1)];
				edgeTo[distTo.length - 1] = coordsToIndex(i, e[0].length - 1);
			}
		}
	}

	private int[] buildSeam() {

		int x = edgeTo[edgeTo.length - 1];
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(indexToX(x));

		while (edgeTo[x] != 0) {
			stack.push(indexToX(edgeTo[x]));
			x = edgeTo[x];
		}

		int[] seam = new int[stack.size()];
		int count = 0;
		for (int i : stack) {
			seam[count] = i;
			count++;
		}
		return seam;
	}

	private void relax(int i, int j) {
		// check diag left, bot and diag right weights
		for (int k = i - 1; k <= i + 1; k++) {
			if (k < 0 || k >= e.length)
				continue;
			if (distTo[coordsToIndex(i, j)] + e[k][j + 1] < distTo[coordsToIndex(
					k, j + 1)]) {
				distTo[coordsToIndex(k, j + 1)] = distTo[coordsToIndex(i, j)]
						+ e[k][j + 1];
				edgeTo[coordsToIndex(k, j + 1)] = coordsToIndex(i, j);
			}
		}
	}

	private int coordsToIndex(int i, int j) {
		return j * e.length + i + 1;
	}

	private int indexToX(int index) {
		return (index - 1) % e.length;
	}
}

// public class SeamCarver {
//
// private final Picture pic;
// private double[][] e;
// private double[] distTo;
// private int[] edgeTo;
//
// private int width, height;
//
// // create a seam carver object based on the given picture
// public SeamCarver(Picture picture) {
// if (picture == null)
// throw new NullPointerException();
//
// this.pic = new Picture(picture);
// width = picture.width();
// height = picture.height();
// }
//
// // build energy map
// private void buildEnergyMap() {
// // create energy map
// e = new double[width() - 2][height() - 2];
// for (int j = 0; j < height() - 2; j++) {
// for (int i = 0; i < width() - 2; i++) {
// e[i][j] = energy(i + 1, j + 1);
// }
// }
// }
//
// // transpose energy map
// private void transpose() {
// double[][] newE = new double[e[0].length][e.length];
//
// for (int i = 0; i < newE.length; i++) {
// for (int j = 0; j < newE[0].length; j++) {
// newE[i][j] = e[j][i];
// }
// }
// e = newE;
// }
//
// // current picture
// public Picture picture() {
// return pic;
// }
//
// // width of current picture
// public int width() {
// return width;
// }
//
// // height of current picture
// public int height() {
// return height;
// }
//
// // energy of pixel at column x and row y
// public double energy(int x, int y) {
// if (x < 0 || x > width() || y < 0 || y > height())
// throw new IndexOutOfBoundsException("X: " + x + " Y: " + y);
//
// if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
// return 1000.0;
// else {
// Color top = pic.get(x, y - 1);
// Color bot = pic.get(x, y + 1);
// Color left = pic.get(x - 1, y);
// Color right = pic.get(x + 1, y);
//
// int diffx = getPixelDiff(left, right);
// int diffy = getPixelDiff(top, bot);
//
// return Math.sqrt(diffx + diffy);
// }
// }
//
// // sequence of indices for horizontal seam
// public int[] findHorizontalSeam() {
// buildEnergyMap();
// transpose();
// findSeam();
// return buildSeam();
// }
//
// // sequence of indices for vertical seam
// public int[] findVerticalSeam() {
// buildEnergyMap();
// findSeam();
// return buildSeam();
// }
//
// // remove horizontal seam from current picture
// public void removeHorizontalSeam(int[] seam) {
// if (seam == null)
// throw new NullPointerException();
// if (seam.length != width())
// throw new IllegalArgumentException();
//
// height--;
//
// Picture result = new Picture(width, height);
// for (int i = 0; i < width; i++) {
// for (int j = 0; j < height; j++) {
//
// if (seam[i] < 0 || seam[i] > height)
// throw new IllegalArgumentException();
//
// if (j < seam[i])
// result.set(i, j, pic.get(i, j));
// else
// result.set(i, j, pic.get(i, j + 1));
// }
// }
// pic = result;
// }
//
// // remove vertical seam from current picture
// public void removeVerticalSeam(int[] seam) {
// if (seam == null)
// throw new NullPointerException();
// if (seam.length != height())
// throw new IllegalArgumentException();
//
// width--;
//
// Picture result = new Picture(width, height);
// for (int j = 0; j < height; j++) {
// for (int i = 0; i < width; i++) {
//
// if (seam[j] < 0 || seam[j] > width)
// throw new IllegalArgumentException();
//
// if (i < seam[j])
// result.set(i, j, pic.get(i, j));
// else
// result.set(i, j, pic.get(i + 1, j));
// }
// }
// pic = result;
// }
//
// private int getPixelDiff(Color c1, Color c2) {
// return getColorDiff(c1.getRed(), c2.getRed())
// + getColorDiff(c1.getGreen(), c2.getGreen())
// + getColorDiff(c1.getBlue(), c2.getBlue());
// }
//
// private int getColorDiff(int i, int j) {
// return (i - j) * (i - j);
// }
//
// private void findSeam() {
//
// // create 2 extra vertex
// // 1 at top and 1 at bottom that connects to energy
// edgeTo = new int[(e.length * e[0].length) + 2];
// distTo = new double[(e.length * e[0].length) + 2];
// for (int i = 0; i < distTo.length; i++)
// distTo[i] = Double.POSITIVE_INFINITY;
//
// // relax top vertex (linked to all vertex in top row)
// for (int i = 0; i < e.length; i++) {
// if (e[i][0] < distTo[coordsToIndex(i, 0)]) {
// distTo[coordsToIndex(i, 0)] = e[i][0];
// edgeTo[coordsToIndex(i, 0)] = 0;
// }
// }
//
// // consider each pixel (not on border) in topological order
// // (left to right, top to bottom) except bot row
// for (int j = 0; j < e[0].length - 1; j++)
// for (int i = 0; i < e.length; i++)
// relax(i, j);
//
// // relax bot vertex (linked to all vertex in bot row)
// for (int i = 0; i < e.length; i++) {
// if (distTo[coordsToIndex(i, e[0].length - 1)] < distTo[distTo.length - 1]) {
// distTo[distTo.length - 1] = distTo[coordsToIndex(i,
// e[0].length - 1)];
// edgeTo[distTo.length - 1] = coordsToIndex(i, e[0].length - 1);
// }
// }
// }
//
// private int[] buildSeam() {
// int x = edgeTo[edgeTo.length - 1];
//
// Stack<Integer> stack = new Stack<Integer>();
// stack.push(indexToX(x) + 1);
//
// while (edgeTo[x] != 0) {
// stack.push(indexToX(edgeTo[x]) + 1);
// x = edgeTo[x];
// }
//
// int[] seam = new int[stack.size() + 2];
// seam[0] = stack.peek() - 1;
// int count = 1;
// for (int i : stack) {
// seam[count] = i;
// count++;
// }
// seam[seam.length - 1] = seam[--count] - 1;
//
// return seam;
// }
//
// private void relax(int i, int j) {
// // check diag left, bot and diag right weights
// for (int k = i - 1; k <= i + 1; k++) {
// if (k < 0 || k >= e.length)
// continue;
// if (distTo[coordsToIndex(i, j)] + e[k][j + 1] < distTo[coordsToIndex(
// k, j + 1)]) {
// distTo[coordsToIndex(k, j + 1)] = distTo[coordsToIndex(i, j)]
// + e[k][j + 1];
// edgeTo[coordsToIndex(k, j + 1)] = coordsToIndex(i, j);
// }
// }
// }
//
// private int coordsToIndex(int i, int j) {
// return j * e.length + i + 1;
// }
//
// private int indexToX(int index) {
// return (index - 1) % e.length;
// }
//
// public static void main(String[] args) {
// // if (args.length != 3) {
// //
// //
// StdOut.println("Usage:\njava ResizeDemo [image filename] [num cols to remove] [num rows to remove]");
// // return;
// // }
// //
// // Picture inputImg = new Picture(args[0]);
// // int removeColumns = Integer.parseInt(args[1]);
// // int removeRows = Integer.parseInt(args[2]);
//
// Picture inputImg = new Picture("seamCarving/chameleon.png");
// int removeColumns = Integer.parseInt("400");
// int removeRows = Integer.parseInt("200");
//
// StdOut.printf("image is %d columns by %d rows\n", inputImg.width(),
// inputImg.height());
// SeamCarver sc = new SeamCarver(inputImg);
//
// Stopwatch sw = new Stopwatch();
//
// for (int i = 0; i < removeRows; i++) {
// int[] horizontalSeam = sc.findHorizontalSeam();
// sc.removeHorizontalSeam(horizontalSeam);
// }
//
// for (int i = 0; i < removeColumns; i++) {
// int[] verticalSeam = sc.findVerticalSeam();
// sc.removeVerticalSeam(verticalSeam);
// }
//
// Picture outputImg = sc.picture();
//
// StdOut.printf("new image size is %d columns by %d rows\n", sc.width(),
// sc.height());
//
// StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
//
// inputImg.show();
// outputImg.show();
// }
//
// }
