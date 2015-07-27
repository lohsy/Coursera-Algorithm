
//to call from command line, go to bin folder and type this
//java -cp ~/Downloads/stdlib.jar: Subset 3
public class Subset {

	public static void main (String [] args) {
		
		int K = Integer.parseInt(args[0]);
		String allElements [] = StdIn.readAllStrings();
	
		RandomizedQueue<String> rq = new RandomizedQueue<String>();
		for (String s : allElements) rq.enqueue(s);
		
		for (int i = 0; i < K; i++) 	StdOut.println(rq.dequeue());
	}
}
