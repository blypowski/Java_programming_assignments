//autor:Jakub Marczyk

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

class DataHolder {
	public int numberOfThreads;
	public int numberOfBins;
	public ReentrantLock lock;
	public Vector vector;
	Map<Integer,Integer> histogram;
	public boolean isReady;
	public AtomicInteger flags;
	
	public DataHolder(ReentrantLock lock, int numberOfThreads, int numberOfBins) {
		this.flags = new AtomicInteger(0);
		this.numberOfThreads = numberOfThreads;
		this.numberOfBins = numberOfBins;
		
		this.lock = lock;
		histogram = new ConcurrentHashMap<>();
		
		this.isReady = false;
	}
	
	public void setVector(Vector vector) {
		this.vector = vector;
	}
	
	public Map<Integer, Integer> getMap() {
		return histogram;
	}
}

class VectorCounter implements Runnable {
	DataHolder data;
	ReentrantLock lock;
	Vector vector;
	int n;
	int numberOfThreads;
	Map<Integer, Integer> histogram;
	Map<Integer, Integer> tempHistogram;
	
	
	public VectorCounter(DataHolder data, int n) {
		this.histogram = data.histogram;
		this.data = data;
		this.lock = data.lock;
		this.vector = data.vector;
		this.n = n;
		this.numberOfThreads = data.numberOfThreads;
		this.tempHistogram = new HashMap<Integer, Integer>();
		}
	
	@Override
	public void run() {
		System.out.println("Hello from thread" + n);
		
		//każdy wątek uzupełnia co n-tą liczbę histogramu
		for(int i=n;i<vector.getSize();i+=numberOfThreads) {
			int readEntry = vector.getValue(i);
				if(!tempHistogram.containsKey(readEntry)) {
					tempHistogram.put(readEntry, 1);
				} else {
					tempHistogram.put(readEntry, tempHistogram.get(readEntry)+1);
				}
				//histogram.merge(readEntry, 0, (v1, v2)-> v1+1); //i tutaj wsadź z concurrenthashmapy
				//System.out.println(n + " read " + readEntry + " at position "+ i + " from vector and put " + vector.getValue(i) + "=>" + histogram.get(readEntry));
		}
		
		System.out.println("skończyłem");
		
		tempHistogram.forEach((key, value)-> histogram.merge(key, value, (v1, v2)-> v1+v2));
		
		System.out.println("skończyłem merge'ować");
				int flag = data.flags.incrementAndGet();
				System.out.println("thread " + n + " completed its job");
				if(flag == data.numberOfThreads) {
					System.out.println("and the work is done!");
					data.isReady = true;
					System.out.println("The histogram is" + histogram);
				}
				else
					System.out.println("but there's still work to be done");
		System.out.println("I'm unlocked and exiting");
	}
}

public class FastHistogram implements Histogram {
	DataHolder data;
	ReentrantLock lock;

	@Override
	public void setup(int threads, int bins) {
		lock = new ReentrantLock();
		data = new DataHolder(lock, threads, bins);
	}

	@Override
	public void setVector(Vector vector) {
		data.setVector(vector);
		for(int i=0;i<data.numberOfThreads;i++) {
			new Thread(new VectorCounter(data, i)).start();
		}
	}

	@Override
	public boolean isReady() {
		boolean value = data.isReady;
				if(value)
				System.out.println("IT IS READY!!!");
		return value;
	}

	@Override
	public Map<Integer, Integer> histogram(){
		if(data.isReady==true) {
			System.out.println("histogram is ready: ");
			return data.histogram;
		}
		else {
					System.out.println("histogram not yet ready");
		}
		return null;
	}

}
