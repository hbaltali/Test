package hbaltali.wbs;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ForkJoinActionTest {

	static int counter = 0;
	
	public static void main(String[] args) throws InterruptedException {
		
		long start = System.currentTimeMillis();
		
		int[] zahlen = new int[1_000_000];
		
//		final Random RAND_GEN = new Random();
//		
//		for(int i = 0; i < zahlen.length; i++) {
//			zahlen[i] = RAND_GEN.nextInt(10) + 1;
//		}
		
		ForkJoinPool pool = new ForkJoinPool();
		RandNumberAction aufgabe = new RandNumberAction(zahlen, 0, zahlen.length);
		pool.invoke(aufgabe);
		
		long ende = System.currentTimeMillis();
		
		System.err.println(ende - start + "ms");
		
		System.err.println(counter);
		
		Thread.sleep(2000);
		
		System.err.println(counter);
		
		Thread.sleep(2000);
		
		System.err.println(counter);
	}
	
	private static class RandNumberAction extends RecursiveAction {
		
		private static final Random RAND_GEN = new Random();
		
		private int[] zahlen;
		private int start;
		private int ende;
		
		public RandNumberAction(int[] zahlen, int start, int ende) {
			this.zahlen = zahlen;
			this.start = start;
			this.ende = ende;

		}

		@Override
		protected void compute() {
			
			if(ende - start <= 100) {
				for(int i = start; i < ende; i++) {
					zahlen[i] = RAND_GEN.nextInt(10) + 1;
				}
			}
			else {
				int range = start + ((ende - start) / 2);
				RandNumberAction t1 = new RandNumberAction(zahlen, start, range);
				RandNumberAction t2 = new RandNumberAction(zahlen, range, ende);
				t1.fork(); // Aufgabe abspliten und in die Aufgaben-Queue des ForkJoinPools eintragen
				t2.compute(); // Selbst erledigen
				t1.join();
				
			}counter++;
		}
	}
}
