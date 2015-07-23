public class PercolationStats {

	private double thresholds [];
	
	// perform T independent experiments on an N-by-N grid
	public PercolationStats(int N, int T) {
		thresholds = new double [T];
		
		for(int e=1; e<=T; e++){
			//StdOut.println("Experiment " + e);
			Percolation p = new Percolation(N);
			int count=0;
			while(true){
				int i = StdRandom.uniform(N) + 1;
				int j = StdRandom.uniform(N) + 1;
				if(!p.isOpen(i, j)){
					p.open(i, j);
					count++;
					if(p.percolates()){
						//StdOut.println((double)count/((double)N*N));
						thresholds[e-1] = (double)count/((double)N*N);
						break;
					}
				}
			}
		}
		
//		StdOut.println("mean\t = " + mean());
//		StdOut.println("stddev\t = " + stddev());
//		StdOut.println("95% confidence interval\t = " 
//		+ confidenceLo() + " , " + confidenceHi());
	}

	// sample mean of percolation threshold
	public double mean() {
		return StdStats.mean(thresholds);
	}

	// sample standard deviation of percolation threshold
	public double stddev() {
		return StdStats.stddev(thresholds); 
	}

	// low endpoint of 95% confidence interval
	public double confidenceLo() {
		double mean = mean();
		double sd = stddev();
		return mean - (sd*1.96/Math.sqrt(thresholds.length));
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi() {
		double mean = mean();
		double sd = stddev();
		return mean + (sd*1.96/Math.sqrt(thresholds.length));
	}

	// test client (described below)
	public static void main(String[] args) {
		new PercolationStats(2, 100000);
	}
}
