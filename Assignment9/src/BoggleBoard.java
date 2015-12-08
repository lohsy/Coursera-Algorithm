import java.util.Random;

import edu.princeton.cs.algs4.In;


public class BoggleBoard {
	
	private final char [][] board;
	private Random r;
	
	// Initializes a random 4-by-4 Boggle board.
    // (by rolling the Hasbro dice)
    public BoggleBoard() {
    	this(4,4);
    }

    // Initializes a random M-by-N Boggle board.
    // (using the frequency of letters in the English language)
    public BoggleBoard(int M, int N) {
    	board = new char[M][N];
    	r = new Random();
      	for(int i=0;i<board.length;i++)
    		for(int j=0;j<board[i].length;j++){
    			board[i][j] = getRandomChar();
    		}
    }

    private char getRandomChar () {
    	return (char)((int) 'A' + r.nextInt(26));
    }
    
    // Initializes a Boggle board from the specified filename.
    public BoggleBoard(String filename) {
    	In in = new In(filename);
    	String [] params = in.readLine().split("\\s+");
    	board = new char[Integer.parseInt(params[0])][Integer.parseInt(params[1])];
    	
    	String [] lines = in.readAllLines();
    	
    	for(int i=0;i<board.length;i++){
    		String [] tokens = lines[i].split("\\s+");
    		for(int j=0;j<board[0].length;j++) {
    			board[i][j] = tokens[j].charAt(0);
    		}
    	}		
    }

    // Initializes a Boggle board from the 2d char array.
    // (with 'Q' representing the two-letter sequence "Qu")
    public BoggleBoard(char[][] a) {
    	board = a;
    }

    // Returns the number of rows.
    public int rows() {
    	return board.length;
    }

    // Returns the number of columns.
    public int cols() {
    	return board[0].length;
    }

    // Returns the letter in row i and column j.
    // (with 'Q' representing the two-letter sequence "Qu")
    public char getLetter(int i, int j) {
    	return board[i][j];
    }

    // Returns a string representation of the board.
    public String toString() {
    	StringBuilder s = new StringBuilder();
    	for(int i=0;i<board.length;i++) {
    		for(int j=0;j<board[0].length;j++){
    			s.append(board[i][j]).append(" ");
    		}
    		s.append("\n");
    	}
    	return s.toString();
    }
    

}
