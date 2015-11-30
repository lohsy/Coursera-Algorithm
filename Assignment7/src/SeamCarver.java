import java.awt.Color;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

public class SeamCarver {

	private final Picture pic;
	private int[][] intensity;

	// private boolean isTransposed;
	private int width, height;

	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture) {
		if (picture == null)
			throw new NullPointerException();

		pic = new Picture(picture);
		width = picture.width();
		height = picture.height();

		intensity = new int[width()][height()];
		for (int j = 0; j < height(); j++)
			for (int i = 0; i < width(); i++)
				intensity[i][j] = pic.get(i, j).getRGB();

		// isTransposed = false;
	}

	// current picture
	public Picture picture() {
		Picture result = new Picture(width(), height());

		for (int i = 0; i < result.width(); i++)
			for (int j = 0; j < result.height(); j++)
				result.set(i, j, new Color(intensity[i][j]));

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
			Color top = new Color(intensity[x][y - 1]);
			Color bot = new Color(intensity[x][y + 1]);
			Color left = new Color(intensity[x - 1][y]);
			Color right = new Color(intensity[x + 1][y]);

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
		// if (!isTransposed) {
		// transpose();
		// isTransposed = true;
		// }
		return findSeam(true);
	}

	// transpose energy map
	private void transpose() {
		int[][] temp = new int[intensity[0].length][intensity.length];

		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[0].length; j++) {
				temp[i][j] = intensity[j][i];
			}
		}
		intensity = temp;
	}

	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		// if (isTransposed) {
		// transpose();
		// isTransposed = false;
		// }
		return findSeam(false);
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
		// if (isTransposed) {
		for (int i = 0; i < seam.length; i++)
			for (int j = seam[i]; j < intensity[0].length - 1; j++)
				intensity[i][j] = intensity[i][j + 1];
		// } else {
		// for (int i = 0; i < seam.length; i++)
		// for (int j = seam[i]; j < intensity.length - 1; j++)
		// intensity[j][i] = intensity[j + 1][i];
		// }
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
		// if (isTransposed) {
		// for (int j = 0; j < seam.length; j++)
		// for (int i = seam[j]; i < intensity[0].length - 1; i++)
		// intensity[j][i] = intensity[j][i+1];
		// } else {
		for (int j = 0; j < seam.length; j++)
			for (int i = seam[j]; i < intensity.length - 1; i++)
				intensity[i][j] = intensity[i + 1][j];
		// }
	}

	private int[] findSeam(boolean isHorizontal) {
		// create 2 extra vertex
		// 1 at top and 1 at bottom that connects to energy
		int[] edgeTo = new int[width() * height() + 2];
		double[] distTo = new double[width() * height() + 2];
		for (int i = 1; i < distTo.length; i++)
			distTo[i] = Double.POSITIVE_INFINITY;

		// relax top vertex (linked to all vertex in top row)
		int ncol = isHorizontal ? height() : width();
		int nrow = !isHorizontal ? height() : width();
		for (int i = 0; i < ncol; i++) {
			double currentEnergy = isHorizontal ? energy(0, i) : energy(i, 0);
			if (currentEnergy < distTo[coordsToIndex(i, 0, ncol)]) {
				distTo[coordsToIndex(i, 0, ncol)] = currentEnergy;
				edgeTo[coordsToIndex(i, 0, ncol)] = 0;
			}
		}

		// consider each pixel in topological order
		// (left to right, top to bottom) except bot row
		for (int j = 0; j < nrow - 1; j++)
			for (int i = 0; i < ncol; i++)
				relax(i, j, ncol, edgeTo, distTo, isHorizontal);

		// relax bot vertex (linked to all vertex in bot row)
		for (int i = 0; i < ncol; i++) {
			if (distTo[coordsToIndex(i, nrow - 1, ncol)] < distTo[distTo.length - 1]) {
				distTo[distTo.length - 1] = distTo[coordsToIndex(i, nrow - 1,
						ncol)];
				edgeTo[distTo.length - 1] = coordsToIndex(i, nrow - 1, ncol);
			}
		}

		// build the seam
		int x = edgeTo[edgeTo.length - 1];
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(indexToX(x, ncol));

		while (edgeTo[x] != 0) {
			stack.push(indexToX(edgeTo[x], ncol));
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

	private void relax(int i, int j, int ncol, int[] edgeTo, double[] distTo,
			boolean isHorizontal) {
		// check diag left, bot and diag right weights
		for (int k = i - 1; k <= i + 1; k++) {
			if (k < 0 || k >= ncol)
				continue;
			double currentEnergy = isHorizontal ? energy(j + 1, k) : energy(k,
					j + 1);
			if (distTo[coordsToIndex(i, j, ncol)] + currentEnergy < distTo[coordsToIndex(
					k, j + 1, ncol)]) {
				distTo[coordsToIndex(k, j + 1, ncol)] = distTo[coordsToIndex(i,
						j, ncol)] + currentEnergy;
				edgeTo[coordsToIndex(k, j + 1, ncol)] = coordsToIndex(i, j,
						ncol);
			}
		}
	}

	private int coordsToIndex(int i, int j, int ncol) {
		return j * ncol + i + 1;
	}

	private int indexToX(int index, int ncol) {
		return (index - 1) % ncol;
	}
}

// public class SeamCarver {
//
// private final Picture pic;
// private double[][] e;
// private int[][] intensity;
//
// private boolean isTransposed;
// private int width, height;
//
// // create a seam carver object based on the given picture
// public SeamCarver(Picture picture) {
// if (picture == null)
// throw new NullPointerException();
//
// pic = new Picture(picture);
// width = picture.width();
// height = picture.height();
//
// intensity = new int[width()][height()];
// for (int j = 0; j < height(); j++)
// for (int i = 0; i < width(); i++)
// intensity[i][j] = pic.get(i, j).getRGB();
//
// e = new double[width()][height()];
// for (int j = 0; j < height(); j++)
// for (int i = 0; i < width(); i++)
// e[i][j] = energy(i, j);
//
// isTransposed = false;
// }
//
// // current picture
// public Picture picture() {
// Picture result = new Picture(width(), height());
//
// for (int i = 0; i < result.width(); i++)
// for (int j = 0; j < result.height(); j++)
// result.set(i, j, new Color(intensity[i][j]));
//
// return result;
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
// if (x < 0 || x >= width() || y < 0 || y >= height())
// throw new IndexOutOfBoundsException();
//
// if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
// return 1000.0;
// else {
// Color top = new Color(intensity[x][y - 1]);
// Color bot = new Color(intensity[x][y + 1]);
// Color left = new Color(intensity[x - 1][y]);
// Color right = new Color(intensity[x + 1][y]);
//
// int diffx = getPixelDiff(left, right);
// int diffy = getPixelDiff(top, bot);
//
// return Math.sqrt(diffx + diffy);
// }
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
// // sequence of indices for horizontal seam
// public int[] findHorizontalSeam() {
// if (!isTransposed) {
// transpose();
// isTransposed = true;
// }
// return findSeam();
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
// // sequence of indices for vertical seam
// public int[] findVerticalSeam() {
// if (isTransposed) {
// transpose();
// isTransposed = false;
// }
// return findSeam();
// }
//
// // remove horizontal seam from current picture
// public void removeHorizontalSeam(int[] seam) {
// if (seam == null)
// throw new NullPointerException();
// if (seam.length != width())
// throw new IllegalArgumentException();
// for (int i = 0; i < seam.length - 1; i++)
// if (Math.abs(seam[i] - seam[i + 1]) > 1)
// throw new IllegalArgumentException();
// for (int i = 0; i < seam.length; i++)
// if (seam[i] < 0 || seam[i] >= height())
// throw new IllegalArgumentException();
//
// height--;
// for (int i = 0; i < seam.length; i++)
// for (int j = seam[i]; j < intensity[0].length - 1; j++)
// intensity[i][j] = intensity[i][j + 1];
//
// if (!isTransposed) {
// transpose();
// isTransposed = true;
// }
//
// // create a transposed energy map
// double[][] newE = new double[e.length - 1][e[0].length];
//
// for (int j = 0; j < newE[0].length; j++)
// for (int i = 0; i < newE.length; i++) {
// if (i < seam[j] - 1)
// newE[i][j] = e[i][j];
// else if (seam[j] - i == 1 || seam[j] - i == 0)
// newE[i][j] = energy(j, i);
// else
// newE[i][j] = e[i + 1][j];
// }
// e = newE;
// }
//
// // remove vertical seam from current picture
// public void removeVerticalSeam(int[] seam) {
// if (seam == null)
// throw new NullPointerException();
// if (seam.length != height())
// throw new IllegalArgumentException();
// for (int i = 0; i < seam.length - 1; i++)
// if (Math.abs(seam[i] - seam[i + 1]) > 1)
// throw new IllegalArgumentException();
// for (int i = 0; i < seam.length; i++)
// if (seam[i] < 0 || seam[i] >= width())
// throw new IllegalArgumentException();
//
// width--;
// for (int j = 0; j < seam.length; j++)
// for (int i = seam[j]; i < intensity.length - 1; i++)
// intensity[i][j] = intensity[i + 1][j];
//
// if (isTransposed) {
// transpose();
// isTransposed = false;
// }
//
// double[][] newE = new double[width()][height()];
// for (int j = 0; j < height(); j++)
// for (int i = 0; i < width(); i++) {
// if (i < seam[j] - 1)
// newE[i][j] = e[i][j];
// else if (seam[j] - i == 1 || seam[j] - i == 0)
// newE[i][j] = energy(i, j);
// else
// newE[i][j] = e[i + 1][j];
// }
// e = newE;
// }
//
// private int[] findSeam() {
// // create 2 extra vertex
// // 1 at top and 1 at bottom that connects to energy
// int[] edgeTo = new int[width() * height() + 2];
// double[] distTo = new double[width() * height() + 2];
// for (int i = 1; i < distTo.length; i++)
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
// // consider each pixel in topological order
// // (left to right, top to bottom) except bot row
// for (int j = 0; j < e[0].length - 1; j++)
// for (int i = 0; i < e.length; i++)
// relax(i, j, edgeTo, distTo);
//
// // relax bot vertex (linked to all vertex in bot row)
// for (int i = 0; i < e.length; i++) {
// if (distTo[coordsToIndex(i, e[0].length - 1)] < distTo[distTo.length - 1]) {
// distTo[distTo.length - 1] = distTo[coordsToIndex(i,
// e[0].length - 1)];
// edgeTo[distTo.length - 1] = coordsToIndex(i, e[0].length - 1);
// }
// }
//
// // build the seam
// int x = edgeTo[edgeTo.length - 1];
// Stack<Integer> stack = new Stack<Integer>();
// stack.push(indexToX(x));
//
// while (edgeTo[x] != 0) {
// stack.push(indexToX(edgeTo[x]));
// x = edgeTo[x];
// }
//
// int[] seam = new int[stack.size()];
// int count = 0;
// for (int i : stack) {
// seam[count] = i;
// count++;
// }
// return seam;
// }
//
// private void relax(int i, int j, int[] edgeTo, double[] distTo) {
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
// }