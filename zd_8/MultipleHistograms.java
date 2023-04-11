import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

//autor: Jakub Marczyk

public class MultipleHistograms implements Histogram {
	
	int bins;
	Consumer<Histogram.HistogramResult> histogramConsumer;

	@Override
	public void setup(int bins, Consumer<Histogram.HistogramResult> histogramConsumer) {
		this.bins = bins;
		this.histogramConsumer = histogramConsumer;
	}

	@Override
	public void addVector(int vectorID, Vector vector) {
		new Thread(new VectorCounter(new HistogramData(vectorID, bins, vector, histogramConsumer))).start();
	}

}

class VectorCounter implements Runnable {
	HistogramData data;
	
	public VectorCounter(HistogramData data) {
		this.data = data;
	}

	@Override
	public void run() {
		Map<Integer, Integer> histogram = new HashMap<>();
		
		for(int i=0;i<data.v.getSize();i++) {
			int val = data.v.getValue(i);
			if(histogram.containsKey(val)) {
				histogram.put(val, histogram.get(val)+1);
			}
			else
				histogram.put(val, 1);
		}
		data.histogram = histogram;
		System.out.println(histogram);
		new Thread(new Notifier(data)).start();
	}
	
}

class HistogramData {
	public int id;
	public Vector v;
	public int bins;
	public Consumer<Histogram.HistogramResult> histogramConsumer;
	public Map<Integer, Integer> histogram;
	
	public HistogramData(int id, int bins, Vector v, Consumer<Histogram.HistogramResult> histogramConsumer) {
		this.id = id;
		this.v = v;
		this.histogramConsumer = histogramConsumer;
		this.bins = bins;
		this.histogram = null;
	}
	
	public Histogram.HistogramResult getRecord(){
		return new Histogram.HistogramResult(id, histogram);
	}
}

class Notifier implements Runnable {
	HistogramData data;
	public Notifier(HistogramData data) {
		this.data = data;
	}

	@Override
	public void run() {
		if(data.histogramConsumer!=null)
		data.histogramConsumer.accept(data.getRecord());
	}
	
	
	
}
