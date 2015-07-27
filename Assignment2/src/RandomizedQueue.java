import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item>{
	
	private Node first, last;
	private int N;
	
	private class Node {
		Item item;
		Node next;
		Node prev;
	}
	
	// construct an empty randomized queue
	public RandomizedQueue () {
		first = null;
		last = null;
		N = 0;
	}
	
	// is the queue empty?
	public boolean isEmpty () {
		return N == 0;
	}
	
	// return the number of items on the queue
	public int size() { 
		return N;
	}
	
	// add the item
	public void enqueue(Item item) {
		if(item == null) throw new NullPointerException();
		
		if (first == null) {
			first = new Node();
			first.item = item;
			first.next = null;
			first.prev = null;
			last = first;
		} else {
			Node oldLast = last;
			last = new Node();
			last.item = item;
			last.next = null;
			last.prev = oldLast;
			oldLast.next = last;
		}
		N++;
	}
	
	//remove and return a random item
	public Item dequeue() {
		if(isEmpty()) throw new NoSuchElementException();
		
		int index = StdRandom.uniform(0, N);
		Node tmp = first;
		for (int i = 0; i < index; i++) {
			tmp = tmp.next;
		}
		
		Item item = tmp.item;
		
		if (tmp.next == null && tmp.prev == null) { // 1 element in list
			first = last = null;
		} else if (tmp.next == null) { // last element in list
			last = last.prev;
			last.next = null;
		} else if( tmp.prev == null) { // first element in list
			first = first.next;
			first.prev = null;
		} else { //some where in middle of list
			Node prev = tmp.prev;
			Node next = tmp.next;
			prev.next = next;
			next.prev = prev;
		}
		tmp = null;
		N--;
		
		return item;
	}
	
	//return (but do not remove) a random item
	public Item sample() {
		int index = StdRandom.uniform(0, N);
		Node tmp = first;
		for (int i = 0; i < index; i++) {
			tmp = tmp.next;
		}
		
		Item item = tmp.item;
		
		return item;
	}
	
	//return an independent iterator over items in random order
	@Override
	public Iterator<Item> iterator() {
		return new RandomizedQueueIterator();
	}

	private class RandomizedQueueIterator implements Iterator<Item> {
		
		private int currentIndex;
		private int indexes [];
		
		public RandomizedQueueIterator() {
			currentIndex = 0;
			indexes = new int [N];
			for(int i=0; i < indexes.length; i++)
				indexes[i] = i;
			StdRandom.shuffle(indexes);
		}
		
		@Override
		public boolean hasNext() {
			return currentIndex < indexes.length;
		}

		@Override
		public Item next() {
			if (currentIndex >= indexes.length)
				throw new NoSuchElementException();
			
			Node current = first;
			for(int i = 0; i < indexes[currentIndex]; i++)
				current = current.next;
			Item item = current.item;
			current = current.next;
			currentIndex++;
			return item;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
			
		}
	}
	public static void main(String[] args) {
		
	}
}
