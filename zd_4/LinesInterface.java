import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LinesInterface {

	/**
	 * Punkt
	 */
	public interface Point {
		/**
		 * Nazwa odcinka. Różne punkty mają różne nazwy.
		 * 
		 * @return nazwa odcinka
		 */
		public String getName();
	}

	/**
	 * Odcinek. Odcinek nie ma kierunku. Równie dobrze prowadzi od krańca 1 do 2 jak
	 * i od 2 do 1.
	 */
	public interface Segment {
		/**
		 * Pierwszy kraniec odcinka
		 * 
		 * @return punkt będący pierwszym krańcem odcinka
		 */
		public Point getEndpoint1();

		/**
		 * Drugi kraniec odcinka
		 * 
		 * @return punkt będący drugim krańcem odcinka
		 */
		public Point getEndpoint2();
	}

	public void addPoints(Set<Point> points);
	
	
	public void addSegments(Set<Segment> segments);
	
	public Map<Point, Set<Segment>> getMapEndpointToSegments();

	public List<Segment> findConnection(Point start, Point end);

	public Map<Point, Map<Integer, Set<Point>>> getReachableEndpoints();
}
