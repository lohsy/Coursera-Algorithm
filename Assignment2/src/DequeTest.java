import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


public class DequeTest {
	
	@Test
	public void testDequeConstructor() {
		Deque<Integer> d = new Deque<Integer>();
		Assert.assertTrue(d.size() == 0);
		Assert.assertTrue(d.isEmpty());
	}
	
	@Test
	public void testDequeAddFirst() {
		Deque<Integer> d = new Deque<Integer>();
		d.addFirst(10);
		Assert.assertTrue(d.size() == 1);
		Assert.assertTrue(!d.isEmpty());
		
		d.addFirst(9);	d.addFirst(8);	d.addFirst(7);
		Assert.assertTrue(d.size() == 4);
	}
	
	@Test
	public void testDequeRemoveFirst() {
		Deque<Integer> d = new Deque<Integer>();
		d.addFirst(10); d.addFirst(9);	
		d.addFirst(8);	d.addFirst(7);
		
		Assert.assertEquals(7, (int) d.removeFirst());
		Assert.assertEquals(8, (int) d.removeFirst());
		Assert.assertEquals(9, (int) d.removeFirst());
		Assert.assertEquals(10, (int) d.removeFirst());
		Assert.assertTrue(d.size() == 0);
		Assert.assertTrue(d.isEmpty());
	}
	
	@Test
	public void testDequeAddLast() {
		Deque<String> d = new Deque<String>();
		String ts1 = "asd", ts2 = "qwe", ts3 = "zxc";
		d.addLast(ts1);
		Assert.assertTrue(d.size() == 1);
		Assert.assertTrue(!d.isEmpty());
		
		d.addLast(ts2);	d.addLast(ts3);
		Assert.assertTrue(d.size() == 3);
	}
	
	@Test
	public void testDequeRemoveLast() {
		Deque<String> d = new Deque<String>();
		String ts1 = "asd", ts2 = "qwe", ts3 = "zxc";
		d.addLast(ts1); d.addLast(ts2);	d.addLast(ts3);
		
		Assert.assertEquals(ts1, (String) d.removeFirst());
		Assert.assertEquals(ts2, (String) d.removeFirst());
		Assert.assertEquals(ts3, (String) d.removeFirst());
		Assert.assertTrue(d.size() == 0);
		Assert.assertTrue(d.isEmpty());
	}
	
	@Test 
	public void testIterator() {
		Deque<Integer> d = new Deque<Integer>();
		d.addFirst(10); d.addFirst(9);	
		d.addFirst(8);	d.addFirst(7);
		
		int count = 0 ;
		for(int i : d) {
			if (count == 0) 
				Assert.assertEquals(7, i);
			else if (count == 1)
				Assert.assertEquals(8, i);
			else if (count == 2)
				Assert.assertEquals(9, i);
			else if (count == 3)
				Assert.assertEquals(10, i);
			else 
				System.out.println("Error in testIterator1");
			count ++;
		}
		
		Deque<String> t = new Deque<String>();
		String ts1 = "asd", ts2 = "qwe", ts3 = "zxc";
		t.addFirst(ts3);	t.addFirst(ts2);	t.addFirst(ts1);
		
		count = 0 ;
		for(String s: t) {
			if (count == 0) 
				Assert.assertEquals(ts1, s);
			else if (count == 1)
				Assert.assertEquals(ts2, s);
			else if (count == 2)
				Assert.assertEquals(ts3, s);
			else 
				System.out.println("Error in testIterator2");
			count ++;
		}
	}
	
	@Test
	public void testDequeStagger() {
		Deque<Integer> d = new Deque<Integer>();
		d.addFirst(50);	d.addLast(82);
		d.addFirst(51); d.addLast(81);
		d.addFirst(52); d.addLast(80);
		Assert.assertTrue(d.size() == 6);
		
		//Check iteration
		for(int i : d){
			switch(i) {
				case 50: Assert.assertEquals(50, i); break;
				case 51: Assert.assertEquals(51, i); break;
				case 52: Assert.assertEquals(52, i); break;
				case 80: Assert.assertEquals(80, i); break;
				case 81: Assert.assertEquals(81, i); break;
				case 82: Assert.assertEquals(82, i); break;
				default: System.out.println("Error in " +
						"testDequeStagger");
			}
		}
		
		Assert.assertEquals(80, (int) d.removeLast());
		Assert.assertEquals(52, (int) d.removeFirst());
		Assert.assertEquals(81, (int) d.removeLast());
		Assert.assertEquals(51, (int) d.removeFirst());
		Assert.assertEquals(82, (int) d.removeLast());
		Assert.assertEquals(50, (int) d.removeFirst());
		
	}
	
	@Test 
	public void testDequeIteratorIndependence() {
		Deque<Integer> d = new Deque<Integer>();
		d.addFirst(50);	 d.addFirst(51);	d.addFirst(52); 
		
		for(int i : d) {
			System.out.print("Outer: " + i + " Inner: ");
			for(int j : d) {
				System.out.print(j + " ");
			}
			System.out.println();
		}
	}
}
