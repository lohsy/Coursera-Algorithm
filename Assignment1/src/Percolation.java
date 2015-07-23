
public class Percolation {

	private int N; 
	private int vTop, vBot;
	private boolean [][] grid;
	private WeightedQuickUnionUF uf, uf2;
	
	// create N-by-N grid, with all sites blocked
	public Percolation(int N) throws IllegalArgumentException {
		this.N = N;
		if (N <= 0) 
			throw new IllegalArgumentException("N is less than 0");
		grid = new boolean [N][N];
		uf = new WeightedQuickUnionUF(N*N + 2);
		uf2 = new WeightedQuickUnionUF(N*N + 2);
		
		//virtual top and bot indexes
		vTop = N*N;
		vBot = N*N + 1;
	}

	// open site (row i, column j) if  it is not open already
	public void open(int i, int j) throws IndexOutOfBoundsException {
		if (!isOpen(i, j)) {
			grid[i-1][j-1] = true;
			
			if (i > 1) // one row above
				if (isOpen(i-1, j)) {
					uf.union(coordsToIndex(i, j),coordsToIndex(i-1, j));
					uf2.union(coordsToIndex(i, j),coordsToIndex(i-1, j));
				}
			
			if (i < N) // one row below
				if (isOpen(i+1, j)) {
					uf.union(coordsToIndex(i, j),coordsToIndex(i+1, j));
					uf2.union(coordsToIndex(i, j),coordsToIndex(i+1, j));
				}
			
			if (j > 1) // one column left
				if (isOpen(i, j-1)) {
					uf.union(coordsToIndex(i, j),coordsToIndex(i, j-1));
					uf2.union(coordsToIndex(i, j),coordsToIndex(i, j-1));
				}
			
			
			if (j < N) // one column right
				if (isOpen(i,j+1)) {
					uf.union(coordsToIndex(i, j),coordsToIndex(i, j+1));
					uf2.union(coordsToIndex(i, j),coordsToIndex(i, j+1));
				}
			
			if (i == 1) { // top row connect with virtual top
				uf.union(coordsToIndex(i, j), vTop);
				uf2.union(coordsToIndex(i, j), vTop);
			}
			
			if (i == N) // bot row connect with virtual bot
				uf.union(coordsToIndex(i, j), vBot);
		}
	}

	// is site (row i, column j) open?
	public boolean isOpen(int i, int j) throws IndexOutOfBoundsException {
		validateIndexes(i, j);
		return grid[i-1][j-1];
	}

	// is site (row i, column j) full?
	public boolean isFull(int i, int j) throws IndexOutOfBoundsException {	
		boolean full = false;
		if (isOpen(i, j))
			full = uf2.connected(coordsToIndex(i, j), vTop);
		return full;
	}

	// does the system percolate?
	public boolean percolates() {
		return uf.connected(vTop, vBot);
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