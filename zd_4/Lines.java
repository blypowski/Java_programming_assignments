//autor: Jakub Marczyk

import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Lines implements LinesInterface {
	Set<Point> pointSet = new HashSet<Point>();
	Set<Segment> segmentSet = new HashSet<Segment>();
	
	@Override
	public void addPoints(Set<Point> points) {
		for(LinesInterface.Point p:points) {
			
			if(!pointSet.contains(p))
				pointSet.add(p);
		}
	}
	
	@Override
	public void addSegments(Set<Segment> segments) {
		for(Segment s: segments) {
			//System.out.println("checking "+s);
			if(!segmentSetContains(s, segmentSet)) {	
				//System.out.println("adding "+ s);
				segmentSet.add(s);
			}
			
		}
	}
	
	
	//chyba ok
	@Override
	public Map<Point, Set<Segment>> getMapEndpointToSegments(){
		
		if(this.pointSet.isEmpty())
			return new HashMap<Point, Set<Segment>>();
		
		Map<Point, Set<Segment>> endpointMap = new HashMap<Point, Set<Segment>>();
		
		for(Point p: pointSet) {
			Set<Segment> tempSet = new HashSet<Segment>();
			for(Segment s: segmentSet) {
				//System.out.println("checking " + s + "for point " + p);
				if(s.getEndpoint1().equals(p) || s.getEndpoint2().equals(p)) {
					//System.out.println("an endpoint of " + s + " matches " + p);
					if(!segmentSetContains(s, tempSet)) {
						//System.out.println("adding " + p + " segment " + s + "to tempset");
						tempSet.add(s);
					}
				}
			}
			
			if(!endpointMap.keySet().contains(p)) {
				endpointMap.put(p, tempSet);
			}
			
		}
		//System.out.println("getMapEndpointToSegments exits successfully");
		return endpointMap;
	}
	
	boolean segmentSetContains(Segment s, Set<Segment> sSet) {
		for(Segment ss: sSet) {
			if(s.getEndpoint1().equals(ss.getEndpoint1())) {
				if(s.getEndpoint2().equals(ss.getEndpoint2())) {
					//System.out.println("It is already there");
					return true;
				}
			}
			else if(s.getEndpoint2().equals(ss.getEndpoint1())) {
				if(s.getEndpoint1().equals(ss.getEndpoint2()))
				{
					//System.out.println("It is already there");
					return true;
				}
			}
				
		}
		
		return false;
	}
	
	@Override
	public List<Segment> findConnection(Point start, Point end){
		Traverser t = new Traverser();
		List<Segment> list = t.findConnection(start, end);
		return list;
	}
	
	
	Set<Point> getNeighbours(Point p, Set<Segment> segments){
		Set<Point> neighbours = new HashSet<Point>();
		for(Segment s: segments) {
			neighbours.add(getNeighbour(s, p));
		}
		
		return neighbours;
	}
	
	Point getNeighbour(Segment s, Point p) {
		if(s.getEndpoint1().getName().equals(p.getName())) {
			return s.getEndpoint2();
		}
		else if(s.getEndpoint2().getName().equals(p.getName())) {
			return s.getEndpoint1();
		}
		else {
			System.out.println("getNeighbor error!!!");
			return null;
		}
	}
	
	@Override
	public Map<Point, Map<Integer, Set<Point>>> getReachableEndpoints() {
		Traverser t = new Traverser();
		t.traverseAll();
		return t.getDistances();
	}
	
	//-----------------------------------------------------
	
	class Traverser {
		Map<Point, Map<Integer, Set<Point>>> distances;
		Map<Point, Set<Segment>> segmentMap;

		public Traverser() {
			distances = new HashMap<>();
			segmentMap = getMapEndpointToSegments();
		}
		
		public List<Segment> findConnection(Point start, Point end){
			Walker w = new Walker(start);
			List<Segment> pointList = w.findPath(start, end);
			return pointList;
		}
		
		//funkcja zapełnia HashMapę distances
		public Map<Point, Map<Integer, Set<Point>>> traverseAll() {
			for(Point p: pointSet) {
				distances.put(p, new Walker(p).visit());
			}
			
			return distances;
		}
		
		class Walker {
			Point startPoint;
			Point endPoint;
			Map<Integer, Set<Point>> dist;
			List<Segment> path;
			
			public Walker(Point startPoint) {
				this.startPoint = startPoint;
				this.dist = null;
				this.path = null;
			}
			
			public Map<Integer, Set<Point>> visit() {
				dist = new HashMap<>();
				
				for(int i=1;i<=4;i++) {
					dist.put(i, new HashSet<>());
				}
				
				visit(new ArrayList<Point>(), startPoint);
				
				return dist;
			}
			
			public void visit(List<Point> l, Point p) {
				//wstawiam p do HashMapy odległości
				List<Point> list = new ArrayList<Point>(l);
				if(!list.contains(p)) {
					list.add(p);
					
					if((list.size()-1)>=1 && (list.size()-1)<=4) {
						dist.get(list.size()-1).add(p);
					}
					
					//wywołuję visit() dla nieodwiedzonych sąsiednich wierzchołków
					Set<Point> neighbours = getNeighbours(p, segmentMap.get(p));
					for(Point neigh: neighbours) {
						visit(list, neigh);
					}
				}
				
			}
			
			
			public List<Segment> findPath(Point start, Point end){
				//System.out.println(segmentMap);
				this.path = null;
				this.startPoint = start;
				this.endPoint = end;
				Set<Segment> neighbors = segmentMap.get(start);
				for(Segment neigh: neighbors) {
					pathWalker(startPoint, neigh, new ArrayList<Segment>(), new HashSet<Point>());
				}
				return getPath();
			}
			
			public void pathWalker( Point p, Segment s, List<Segment> segmentList, Set<Point> visitedPoints) {
				//System.out.println("pathWalker: current point: " + p + " current segment: " + s + "segment list:" + segmentList + " visited: " + visitedPoints);
				List<Segment> list = new ArrayList<Segment>(segmentList);
				Set<Point> visited = new HashSet<Point>(visitedPoints);
				Point otherPoint = getNeighbour(s, p);
				if(!visited.contains(p)) {
					//System.out.println(p + "not in visted nodes, adding");
					visited.add(p);
					if(!visited.contains(otherPoint)) {
						list.add(s);
						//System.out.println(s + " added to list, current list: " + list);
						//jeżeli znalazłem koniec, to ustawiam za path bieżącą ścieżkę
						//(jak jest krótsza od aktualnej znalezionej)
						if(otherPoint.equals(endPoint)) {
							System.out.println("Found path " + list);
							if(path!=null) {
								if(list.size()<path.size()) {
									//System.out.println("path is shorter than " + path);
									path = list;
								}
							} else {
								path = list;
							}
						//w przeciwnym wypadku przeszukuję dalej dla wszystkich sąsiednich krawędzi
						} else {
							Set<Segment> neighbours = segmentMap.get(otherPoint);
							for(Segment neigh: neighbours) {
								pathWalker(otherPoint, neigh, list, visited);
							}
						}
					}
				}
			}
			
			public List<Segment> getPath(){
				if(path==null)
					path=new ArrayList<Segment>();
				return path;
			}
			
		}
		
		public Map<Point, Map<Integer, Set<Point>>> getDistances() {
			return distances;
		}
		
	}
	
	//-------------------------------------------------------------------------
	
	public void printSet() {
		
		Iterator<LinesInterface.Point> it = this.pointSet.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void printSegments() {
		Iterator<LinesInterface.Segment> it = this.segmentSet.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
	}
}