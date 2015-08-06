public class Solver {

	private Node last;
	private int totalMoves;
	private boolean isSolvable;

	private class Node implements Comparable<Node> {

		private Board board;
		private int moves;
		private Node prev;

		public Node(Board board, int moves, Node parent) {
			this.board = board;
			this.moves = moves;
			prev = parent;
		}

		public int man() {
			return board.manhattan();
		}
		
		public int ham() {
			return board.hamming();
		}
		
		public int moves() {
			return moves;
		}
		
		@Override
		public int compareTo(Node that) {
			int result;
			if (this.man() + this.moves() < that.man() + that.moves())
				result = -1;
			else if (this.man() + this.moves() > that.man() + that.moves())
				result = 1;
			else {
				if(this.ham() + this.moves() < that.ham() + that.moves())
					result = -1;
				else if(this.ham() + this.moves() > that.ham() + that.moves())
					result = 1;
				else 
					result = 0;
			}
			return result;
		}
	}

	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial) {
		MinPQ<Node> pq = new MinPQ<Node>();
		MinPQ<Node> pqTwin = new MinPQ<Node>();

		pq.insert(new Node(initial, 0, null));
		pqTwin.insert(new Node(initial.twin(), 0, null));

		isSolvable = false;
		totalMoves = 0;

		// Start solver
		//int whileLoopCounter = 0;
		while (true) {
			Node candidate = pq.delMin();
			Node candidateTwin = pqTwin.delMin();

			if (candidate.board.isGoal()) {
				isSolvable = true;
				last = candidate;
				totalMoves = last.moves;
				break;
			}

			if (candidateTwin.board.isGoal()) {
				isSolvable = false;
				break;
			}

			for (Board n : candidate.board.neighbors()) {
				if (candidate.prev == null || !candidate.prev.board.equals(n))
					pq.insert(new Node(n, candidate.moves + 1, candidate));
			}

			for (Board n : candidateTwin.board.neighbors()) {
				if (candidateTwin.prev == null
						|| !candidateTwin.prev.board.equals(n))
					pqTwin.insert(new Node(n, candidateTwin.moves + 1,
							candidateTwin));
			}

			//StdOut.println("Loop: " + whileLoopCounter++);
		}
	}

	// is the initial board solvable?
	public boolean isSolvable() {
		return isSolvable;
	}

	// min number of moves to solve initial board; -1 if unsolvable
	public int moves() {
		if(isSolvable())
			return totalMoves;
		else
			return -1;
	}

	// sequence of boards in a shortest solution; null if unsolvable
	public Iterable<Board> solution() {
		if(isSolvable()){
			Stack<Board> solution = new Stack<Board>();
			Node tmp = last;
			while (tmp != null) {
				solution.push(tmp.board);
				tmp = tmp.prev;
			}
			return solution;
		} else {
			return null;
		}
	}

	// solve a slider puzzle (given below)
	public static void main(String[] args) {
		// create initial board from file
		// In in = new In(args[0]);
		In in = new In("8puzzle/puzzle3x3-15.txt");
		int N = in.readInt();
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}
}
