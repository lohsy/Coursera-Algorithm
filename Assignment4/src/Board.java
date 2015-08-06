public class Board {

	private int N;
	private char[] t;
	private int spaceIndex;
	private boolean isGoal;
	private int man, ham;
	private String boardString;

	// construct a board from an N-by-N array of blocks
	// (where blocks[i][j] = block in row i, column j)
	public Board(int[][] blocks) {
		if (blocks == null)
			throw new NullPointerException();

		N = blocks.length;
		t = new char[N * N];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				t[i * N + j] = (char) blocks[i][j];
				if (t[i * N + j] == (char) 0)
					spaceIndex = i * N + j;
			}
		}

		calculateMan();
		calculateHam();
		calcualteIsGoal();
		getString();
	}

	// board dimension N
	public int dimension() {
		return N;
	}

	private void calculateHam() {
		ham = 0;
		for (int i = 0; i < t.length; i++) {
			if (i == spaceIndex)
				continue;
			if (t[i] != (char) (i + 1))
				ham++;
		}
	}

	// number of blocks out of place
	public int hamming() {
		return ham;
	}

	// sum of Manhattan distances between blocks and goal
	private void calculateMan() {
		man = 0;
		for (int i = 0; i < t.length; i++) {
			if (i == spaceIndex)
				continue;
			if (t[i] != (char) (i + 1)) {
				int eRow = (int) (t[i] - 1) / N;
				int eCol = (int) (t[i] - 1) % N;
				int cRow = i / N;
				int cCol = i % N;
				man += Math.abs(eRow - cRow) + Math.abs(eCol - cCol);
			}
		}
	}

	public int manhattan() {
		return man;
	}

	private void calcualteIsGoal() {
		isGoal = true;
		for (int i = 0; i < t.length; i++) {
			if (i == spaceIndex)
				continue;
			if (t[i] != (char) (i + 1)) {
				isGoal = false;
				break;
			}
		}
	}

	// is this board the goal board?
	public boolean isGoal() {
		return isGoal;
	}

	private int[][] createDuplicateBoard() {
		int[][] dup = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				dup[i][j] = (int) t[i * N + j];
		return dup;
	}

	// a board that is obtained by exchanging two adjacent blocks in the same
	// row
	public Board twin() {

		int tb[][] = createDuplicateBoard();
		int spaceRow = spaceIndex / N;
		int swapRow;
		if (spaceRow == N - 1)
			swapRow = spaceRow - 1;
		else
			swapRow = spaceRow + 1;

		int tmp = tb[swapRow][0];
		tb[swapRow][0] = tb[swapRow][1];
		tb[swapRow][1] = tmp;

		return new Board(tb);
	}

	// does this board equal y?
	public boolean equals(Object y) {
		if (y == this)
			return true;
		if (y == null)
			return false;
		if (y.getClass() != this.getClass())
			return false;
		Board that = (Board) y;
		return this.toString().contentEquals(that.toString());
	}

	// all neighboring boards
	public Iterable<Board> neighbors() {
		Stack<Board> stack = new Stack<Board>();

		int spaceRow = spaceIndex / N;
		int spaceCol = spaceIndex % N;

		// top
		if (spaceRow > 0) {
			int tb[][] = createDuplicateBoard();
			int tmp = tb[spaceRow][spaceCol];
			tb[spaceRow][spaceCol] = tb[spaceRow - 1][spaceCol];
			tb[spaceRow - 1][spaceCol] = tmp;
			stack.push(new Board(tb));
		}
		// bottom
		if (spaceRow < N - 1) {
			int tb[][] = createDuplicateBoard();
			int tmp = tb[spaceRow][spaceCol];
			tb[spaceRow][spaceCol] = tb[spaceRow + 1][spaceCol];
			tb[spaceRow + 1][spaceCol] = tmp;
			stack.push(new Board(tb));
		}
		// left
		if (spaceCol > 0) {
			int tb[][] = createDuplicateBoard();
			int tmp = tb[spaceRow][spaceCol];
			tb[spaceRow][spaceCol] = tb[spaceRow][spaceCol - 1];
			tb[spaceRow][spaceCol - 1] = tmp;
			stack.push(new Board(tb));
		}
		// right
		if (spaceCol < N - 1) {
			int tb[][] = createDuplicateBoard();
			int tmp = tb[spaceRow][spaceCol];
			tb[spaceRow][spaceCol] = tb[spaceRow][spaceCol + 1];
			tb[spaceRow][spaceCol + 1] = tmp;
			stack.push(new Board(tb));
		}
		return stack;
	}

	private void getString() {
		StringBuilder s = new StringBuilder();
		s.append(N).append("\n");
		for (int i = 0; i < t.length; i++) {
			s.append((int) t[i]);
			if (i % N == N - 1)
				s.append("\n");
			else
				s.append(" ");
		}
		boardString = s.toString();
	}

	// string representation of this board (in the output format specified
	// below)
	public String toString() {
		return boardString;
	}

	// unit tests (not graded)
	public static void main(String[] args) {

	}

}
