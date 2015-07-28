import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;


public class RandomizedQueueTest {

	@Test
	public void testRQConstructor() {
		RandomizedQueue<Integer> d = new RandomizedQueue<Integer>();
		Assert.assertTrue(d.size() == 0);
		Assert.assertTrue(d.isEmpty());
	}
	
	@Test
	public void testRQEnqueue() {
		RandomizedQueue<Integer> d = new RandomizedQueue<Integer>();
		for(int i=0;i<1000;i++) d.enqueue(i);
		Assert.assertTrue(d.size() == 1000);
		Assert.assertTrue(!d.isEmpty());
	}
	
	@Test
	public void testRQdequeue() {
		RandomizedQueue<Integer> d = new RandomizedQueue<Integer>();
		for(int i=0;i<1000;i++) d.enqueue(i);
		
		d.dequeue();
		
		Assert.assertTrue(d.size() == 999);
		Assert.assertTrue(!d.isEmpty());
	}
	
	@Test
	public void testRQdequeueTillEmpty() {
		RandomizedQueue<Integer> d = new RandomizedQueue<Integer>();
		for(int i=0;i<10;i++) d.enqueue(i);
		
		for(int i=0;i<10;i++) d.dequeue();
		
		Assert.assertTrue(d.size() == 0);
		Assert.assertTrue(d.isEmpty());
	}
	
	@Test
	public void testRQdequeueRandomness() {
		
		int randomCount = 0;
		
		for (int count = 0; count < 10; count++) {
			RandomizedQueue<Integer> d = new RandomizedQueue<Integer>();	
			for(int i = 0; i < 10 ; i ++) {
				d.enqueue(i);
			}
		
			int sameHits = 0;
			for (int i = 0; i < 10 ; i ++) {
				if (d.dequeue() == i)
					sameHits++;
			}
			
			if (sameHits <= 2)
				randomCount++;
		}
		
		Assert.assertTrue(randomCount >= 8);
	}
	
	@Test
	public void testRQSample() {
		RandomizedQueue<Integer> d = new RandomizedQueue<Integer>();	
		for(int i = 0; i < 10 ; i ++) {
			d.enqueue(i);
		}
		Assert.assertTrue(d.sample() != null);
		Assert.assertTrue(d.size() == 10);
		Assert.assertTrue(!d.isEmpty());
		
		
	}
	
	@Test
	public void testRQSampleRandomness() {
		RandomizedQueue<Integer> d = new RandomizedQueue<Integer>();	
		for(int i = 0; i < 10 ; i ++) {
			d.enqueue(i);
		}
		
		int hits [] = new int [10];
		
		for (int i = 0; i < 1000 ; i ++) {
			hits[d.sample()]++;
		}
		
		for (int i : hits) {
			System.out.println(i);
			Assert.assertTrue(i < 130 && i > 70);
		}
	}
	
	@Test
	public void testRQIterator() {
		RandomizedQueue<Integer> d = new RandomizedQueue<Integer>();	
		for(int i = 0; i < 10 ; i ++) {
			d.enqueue(i);
		}
		
		for (int i : d) Assert.assertTrue(i>=0 && i<=9);
		
		int hits [] = new int [10];
		
		for (int count = 0; count < 1000; count++)
			for (int i : d) hits[i]++;
		
		for (int i : hits) {
			Assert.assertTrue(i == 1000);
		}
	}
	
	@Test
	public void testRQIteratorRandomness () {
		RandomizedQueue<Integer> d = new RandomizedQueue<Integer>();	
		for(int i = 0; i < 10 ; i ++) {
			d.enqueue(i);
		}
		
		int seq1 [] = new int[10];
		int seq2 [] = new int[10];
		
		int count = 0;
		for (int i : d) {
			seq1[count] = i;
			count++;
		}
		
		count = 0;
		for (int i : d) {
			seq2[count] = i;
			count++;
		}
		
		int hits = 0;
		for (int i = 0; i < 10; i++) {
			if(seq1[i] == seq2[i])
				hits++;
		}
		
		Assert.assertTrue(hits <= 10);
	}
	
	@Test
	public void testRQIteratorIndependence() {
		RandomizedQueue<Integer> d = new RandomizedQueue<Integer>();	
		for(int i = 0; i < 10 ; i ++) {
			d.enqueue(i);
		}
		
		for (int i : d) {
			System.out.print("Outer: " + i + " Inner: ");
			for (int j : d) {
				System.out.print(j + " ");
			}
			System.out.println();
		}
	}
	
	@Test
	public void testRQFailed1() {
		RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();	
		rq.enqueue(654);
        rq.size();//        ==> 1
        rq.dequeue();//     ==> 654
        rq.isEmpty() ;//    ==> true
        rq.size();//        ==> 0
        rq.size();//        ==> 0
        rq.enqueue(863);//
	}
}
