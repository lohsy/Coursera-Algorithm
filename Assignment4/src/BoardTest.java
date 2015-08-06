import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

public class BoardTest {

	int board1[][] = new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
	int board2[][] = new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };

	@Test
	public void testConstructor() {
		// just make sure no run time error
		Board b1 = new Board(board1);
		Board b2 = new Board(board2);
	}

	@Test
	public void testEquals() {
		Board b1 = new Board(board1);
		Board b2 = new Board(board1);

		Assert.assertTrue(b1.equals(b2));

		Board b3 = new Board(new int[][] { { 8, 1, 3 }, { 4, 0, 2 },
				{ 7, 6, 5 } });
		Board b4 = new Board(new int[][] { { 8, 1, 3 }, { 4, 0, 2 },
				{ 7, 6, 5 } });

		Assert.assertTrue(b3.equals(b4));

		Board b5 = new Board(board1);
		Board b6 = new Board(board2);

		Assert.assertTrue(!b5.equals(b6));
	}

	@Test
	public void testDimension() {
		Board b1 = new Board(board1);
		Assert.assertEquals(board1.length, b1.dimension());
	}

	@Test
	public void testHamming() {
		Board b1 = new Board(board1);
		Assert.assertEquals(0, b1.hamming());
		Board b2 = new Board(board2);
		Assert.assertEquals(5, b2.hamming());
	}

	@Test
	public void testManhattan() {
		Board b1 = new Board(board1);
		Assert.assertEquals(0, b1.manhattan());
		Board b2 = new Board(board2);
		Assert.assertEquals(10, b2.manhattan());
	}

	@Test
	public void testGoal() {
		Board b1 = new Board(board1);
		Assert.assertTrue(b1.isGoal() == true);
		Board b2 = new Board(board2);
		Assert.assertTrue(b2.isGoal() == false);
	}

	@Test
	public void testTwin() {
		Board b1 = new Board(new int[][] { { 1, 2, 3 }, { 4, 5, 6 },
				{ 7, 8, 0 } });
		Board twin1 = b1.twin();
		Assert.assertTrue(!b1.equals(twin1));
		Assert.assertTrue(!twin1.isGoal());
		Assert.assertEquals(2, twin1.hamming());
		Assert.assertEquals(2, twin1.manhattan());

		Board b2 = new Board(new int[][] { { 0, 2, 3 }, { 4, 5, 6 },
				{ 7, 8, 1 } });
		Board twin2 = b2.twin();
		Assert.assertTrue(!b2.equals(twin2));
		Assert.assertTrue(!twin2.isGoal());
		Assert.assertEquals(3, twin2.hamming());
		Assert.assertEquals(6, twin2.manhattan());
	}

	@Test
	public void testToString() {
		Board b1 = new Board(new int[][] { { 1, 2, 3 }, { 4, 5, 6 },
				{ 7, 8, 0 } });
		System.out.println(b1.toString());
		Board b2 = new Board(new int[][] { { 0, 2, 3 }, { 4, 5, 6 },
				{ 7, 8, 1 } });
		System.out.println(b2.toString());
	}

	@Test
	public void testIterator() {
		Board b1 = new Board(new int[][] { { 1, 2, 3 }, { 4, 5, 6 },
				{ 7, 8, 0 } });
		int count1 = 0;
		for (Board c : b1.neighbors()) {
			Assert.assertTrue(!c.isGoal());
			Assert.assertTrue(!b1.equals(c));
			count1++;
		}
		Assert.assertEquals(2, count1);

		Board b2 = new Board(new int[][] { { 1, 2, 3 }, { 4, 0, 6 },
				{ 7, 8, 5 } });
		int count2 = 0;
		for (Board c : b2.neighbors()) {
			Assert.assertTrue(!c.isGoal());
			Assert.assertTrue(!b2.equals(c));
			count2++;
		}
		Assert.assertEquals(4, count2);
	}
}
