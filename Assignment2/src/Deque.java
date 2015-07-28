import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

	private Node first, last;
	private int size;

	private class Node {
		Item item;
		Node next;
		Node prev;
	}

	// construct an empty deque
	public Deque() {
		first = null;
		last = null;
		size = 0;
	}

	// is the deque empty?
	public boolean isEmpty() {
		return size == 0;
	}

	// return the number of items on the deque
	public int size() {
		return size;
	}

	// add the item to the front
	public void addFirst(Item item) {
		if (item == null)
			throw new NullPointerException();

		if (first == null) {
			first = new Node();
			first.item = item;
			first.next = null;
			first.prev = null;
			last = first;
		} else {
			Node oldFirst = first;
			first = new Node();
			first.item = item;
			first.next = oldFirst;
			first.prev = null;
			oldFirst.prev = first;
		}
		size++;
	}

	// add the item to the end
	public void addLast(Item item) {
		if (item == null)
			throw new NullPointerException();
		if (last == null) {
			last = new Node();
			last.item = item;
			last.next = null;
			last.prev = null;
			first = last;
		} else {
			Node oldLast = last;
			last = new Node();
			last.item = item;
			last.next = null;
			last.prev = oldLast;
			oldLast.next = last;
		}
		size++;
	}

	// remove and return the item from the front
	public Item removeFirst() {
		if (isEmpty())
			throw new NoSuchElementException();

		Item item = first.item;
		first = first.next;

		size--;

		if (isEmpty()) {
			first = null;
			last = null;
		} else {
			first.prev = null;
		}

		return item;
	}

	// remove and return the item from the end
	public Item removeLast() {
		if (isEmpty())
			throw new NoSuchElementException();

		Item item = last.item;
		last = last.prev;

		size--;

		if (isEmpty()) {
			first = null;
			last = null;
		} else {
			last.next = null;
		}

		return item;
	}

	// return an iterator over items in order from front to end
	public Iterator<Item> iterator() {
		return new DequeIterator();
	}

	private class DequeIterator implements Iterator<Item> {

		private Node current = first;

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public Item next() {
			if (current == null)
				throw new NoSuchElementException();

			Item item = current.item;
			current = current.next;
			return item;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();

		}
	}

	// unit testing
	public static void main(String[] args) {

	}
}