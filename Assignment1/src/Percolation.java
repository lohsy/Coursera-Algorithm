
public class Percolation {

	private int N; 
	private boolean [][] grid;
	private WeightedQuickUnionUF uf;
	
	// create N-by-N grid, with all sites blocked
	public Percolation(int N) throws IllegalArgumentException {
		this.N = N;
		if (N < 0) 
			throw new IllegalArgumentException("N is less than 0");
		grid = new boolean [N][N];
		uf = new WeightedQuickUnionUF(N*N);
	}

	// open site (row i, column j) if  it is not open already
	public void open(int i, int j) throws IndexOutOfBoundsException {
		validateIndexes(i, j);
		
		if (!isOpen(i, j)) {
			grid[i-1][j-1] = true;
			if (i > 1)
				if (grid[i-1][j-1] & grid[i-2][j-1])
					uf.union(coordsToIndex(i, j),coordsToIndex(i-1, j));
			if (i < N)
				if (grid[i-1][j-1] & grid[i][j-1])
					uf.union(coordsToIndex(i, j),coordsToIndex(i+1, j));
			if (j > 1)
				if (grid[i-1][j-1] & grid[i-1][j-2])
					uf.union(coordsToIndex(i, j),coordsToIndex(i, j-1));
			if (j < N)
				if (grid[i-1][j-1] & grid[i-1][j])
					uf.union(coordsToIndex(i, j),coordsToIndex(i, j+1));

		}
	}

	// is site (row i, column j) open?
	public boolean isOpen(int i, int j) throws IndexOutOfBoundsException {
		validateIndexes(i, j);
		return grid[i-1][j-1];
	}

	// is site (row i, column j) full?
	public boolean isFull(int i, int j) throws IndexOutOfBoundsException {
		validateIndexes(i, j);
		
		boolean full = false;
		if (grid[i-1][j-1]){
			for(int index = 1; index <= N; index++) {
				if (grid[0][index-1])
					full = uf.connected(coordsToIndex(1, index),
						coordsToIndex(i, j));
				if (full)
					break;
			}
		}
		return full;
	}

	// does the system percolate?
	public boolean percolates() {
		boolean percolates = false;
		for (int index = 1;index <= N; index++) {
			if (grid[N-1][index-1])
				percolates = isFull(N, index);
			if (percolates)
				break;
		}
		return percolates;
	}

	private void validateIndexes(int i, int j) throws IndexOutOfBoundsException {
		if (i < 1 || i > N || j  < 1 || j > N)
			throw new IndexOutOfBoundsException("i: " + i + " j: " + j);
	}
	
	private int coordsToIndex(int i, int j) {
		return (i-1) * N + (j-1);
	}
	
	// test client (optional)
	public static void main(String[] args) {
		
	}
}