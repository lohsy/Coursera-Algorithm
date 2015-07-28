import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

	private Item [] q;
	private int itemSize, nullSize;
	private int first;

	// construct an empty randomized queue
	public RandomizedQueue() {
		q = (Item []) new Object [2];
		itemSize = nullSize = 0;
		first = 0;
	}

	// is the queue empty?
	public boolean isEmpty() {
		return itemSize == 0;
	}

	// return the number of items on the queue
	public int size() {
		return itemSize;
	}

	private void resize(int capacity){
		Item [] tmp = (Item []) new Object [capacity];
		int count = 0;
		for(int i=0;i<q.length;i++){
			if(q[(first+i)%q.length] != null) {
				tmp[count] = q[(first+i)%q.length];
				count++;
			}
		}
		q = tmp;
		first = 0;
		nullSize = 0;
	}
	
	// add the item
	public void enqueue(Item item) {
		if (item == null)
			throw new NullPointerException();
		
		if (itemSize + nullSize == q.length) 
			resize(2*itemSize + nullSize);
		q[itemSize + nullSize] = item;
		itemSize++;
	}

	// remove and return a random item
	public Item dequeue() {
		if (isEmpty())
			throw new NoSuchElementException();

		int index;
		do {
			index = StdRandom.uniform(0, itemSize + nullSize);
		} while (q[index] == null);
		
		if(index == first)
			first++; 
		if(first == q.length)
			first = 0;
		
		Item item = q[index];
		q[index] = null;
		
		itemSize--;	nullSize++;
		
		if( (double) itemSize / (double) (itemSize + nullSize) <= 0.25)
			resize(itemSize*2);
		
		return item;
	}

	// return (but do not remove) a random item
	public Item sample() {
		if (isEmpty())
			throw new NoSuchElementException();
		
		int index;
		do {
			index = StdRandom.uniform(0, itemSize + nullSize);
		} while (q[index] == null);

		return q[index];
	}

	// return an independent iterator over items in random order
	@Override
	public Iterator<Item> iterator() {
		return new RandomizedQueueIterator();
	}

	private class RandomizedQueueIterator implements Iterator<Item> {

		private int currentIndex;
		private int indexes[];

		public RandomizedQueueIterator() {
			currentIndex = 0;
			indexes = new int[itemSize];
			int count = 0;
			for (int i = 0; i < q.length; i++) {
				if(q[i] != null) {
					indexes[count] = i;
					count++;
				}
			}
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

			Item item = q[indexes[currentIndex]];
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
