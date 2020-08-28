package org.spaver.space;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.spaver.context.SpatialContext;
import org.spaver.context.SpatialContextFactory;
//import org.spaver.dijkstra.State;
import org.spaver.distance.CartesianDistCalc;
import org.spaver.shape.Line;
import org.spaver.shape.Point;
import org.spaver.shape.Rectangle;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class WeightedGraph extends Canvas implements TopometricModel<Point, WeightEdge> {

	ArrayList<Point> locList; // list of locations
	ArrayList<WeightEdge> wEdgeList; // list of edges
	double[][] dM;
	public SpatialContext spatialContext;

	public WeightedGraph() {
		this.wEdgeList = new ArrayList<WeightEdge>();
		spatialContext = new SpatialContext(new SpatialContextFactory());
	}

	/**
	 * Add point with label
	 */
	public void addPiont(String label) {
		Point point = new Point(spatialContext, label);
		locList.add(point);
	}

	/**
	 * Add point with a point
	 */
	public void addPoint(Point point) {
		locList.add(point);
	}

	/**
	 * Add edge
	 */
	public void addEdge(int start, int end) {
		locList.get(start).addNeighbour(locList.get(end));
		locList.get(end).addNeighbour(locList.get(start));
		wEdgeList.add(new WeightEdge(locList.get(start), locList.get(end)));
	}

	/**
	 * locations position from labels
	 */
	public Point getPoint(String label) {
		for (Point point : this.locList) {
			if (point.getLabel().equals(label)) {
				return point;
			}
		}
		return null;
	}

	/**
	 * locations Set
	 */
	public Set<Point> getPoints() {
		Set<Point> setLoc = new HashSet<Point>(locList);
		return setLoc;
	}

	/**
	 * edges set
	 */
	public Set<WeightEdge> getEdges() {
		Set<WeightEdge> setEdg = new HashSet<WeightEdge>(wEdgeList);
		return setEdg;
	}

	/**
	 * @param points
	 * @return the neighbors of each point
	 */
	public ArrayList<Point> closure(ArrayList<Point> points) {
		Point point;
		ArrayList<Point> closureSet = new ArrayList<Point>();
		for (int i = 0; i < points.size(); i++) {
			point = points.get(i);
			closureSet.addAll(point.getNeighbourd());
		}
		return closureSet;
	}

	public Object[] getEdgesArray() {
		return wEdgeList.toArray();
	}

	/**
	 * Neighborhoods
	 */
	public ArrayList<Point> returnNeighbourd(Point point) {
		return point.getNeighbourd();
	}

	// Boundary
	public Set<Point> getExternalBorder(Set<Point> subSetLoc) {
		Set<Point> externalBorder = new HashSet<Point>();
		for (Point l : subSetLoc) {
			for (Point lAdj : l.getNeighbourd()) {
				if (!subSetLoc.contains(lAdj)) {
					if (!externalBorder.contains(lAdj)) {
						externalBorder.add(lAdj);
					}
				}
			}
		}
		return externalBorder;
	}

	// distance Matrix
//	public void dMcomputation() {
//		CartesianDistCalc infoGraph = new CartesianDistCalc();
//		this.dM = infoGraph.returnDistMatrix();
//	}

	// distance Matrix
	public void dMcomputationOld() {
		CartesianDistCalc infoGraph = new CartesianDistCalc();
		this.dM = infoGraph.returnDistMatrixOld();
	}

	public double[][] getDM() {
		return this.dM;
	}

	public int getNumberOfLocations() {
		return this.locList.size();
	}

	public Point getLocation(int j) {
		return this.locList.get(j);
	}

	/**
	 * 
	 * @return the number of edges
	 */
	public int getNumberOfEdges() {
		return this.wEdgeList.size();
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	public WeightEdge getEdge(int i) {
		return this.wEdgeList.get(i);
	}

	public double getWeight(Point point1, Point point2) {
		for (WeightEdge edge : wEdgeList) {
			if (edge.source.equals(point1) && edge.destination.equals(point2)) {
				return edge.weight;
			}
		}
		return -1;
	}
//
//	public static WeightedGraph createGrid(int rows, int columns, double distance) {
//		WeightedGraph graph = new WeightedGraph();
//		for (int i = 0; i < rows; i++) {
//			for (int j = 0; j < columns; j++) {
//				graph.addPoint(new Po);.addPiont(label);;//.addpoint();//addLoc((i + 1) + "_" + (j + 1), i * columns + j);
//			}
//		}
//		for (int i = 0; i < rows; i++) {
//			for (int j = 0; j < columns; j++) {
//				if (j != columns - 1) {
//					graph.addEdge(i * columns + j, i * columns + (j + 1), distance);
//				}
//				if (i != rows - 1) {
//					graph.addEdge(i * columns + j, (i + 1) * columns + j, distance);
//				}
//			}
//		}
//		return graph;
//	}

	public Set<Point> pre(Point point) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Point> post(Point point) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Point> pre(Set<Point> points) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Point> post(Set<Point> points) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Point> getSet(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	private int numOfConnections = 5;
	WeightedGraph weightedGraph;
	Point startPoint;
	Point goal;
	private int RRTMultiplier = 8;
	private int optimiseDistance = 40;

	List<Node> RRTPoints = new ArrayList<Node>();
	ArrayList<Point> PRMPoints = new ArrayList<Point>();
	ArrayList<WeightEdge> PRMWeightedEdge = new ArrayList<WeightEdge>();

	List<Rectangle> obstacles = new ArrayList<Rectangle>();

	List<List<Rectangle>> obstacleSets = new ArrayList<List<Rectangle>>();
	ArrayList<Point> shortestPathPoints = new ArrayList<Point>();
	private int size;

	public WeightedGraph(WeightedGraph weightedGraph, Point goal) {
		this.weightedGraph = weightedGraph;
		this.goal = goal;
		PRMPoints = weightedGraph.PRMPoints;
		PRMWeightedEdge = weightedGraph.PRMWeightedEdge;
	}

	public WeightedGraph(int size) {
		super(size, size);
		spatialContext = new SpatialContext(new SpatialContextFactory());
		this.size = size;
		
		List<Rectangle> rectangle1 = new ArrayList<Rectangle>();
		rectangle1.add(new Rectangle(300, 250, 400, 300, spatialContext));
		rectangle1.add(new Rectangle(300, 50, 400, 100, spatialContext));

		List<Rectangle> rectangle2 = new ArrayList<Rectangle>();
		rectangle2.add(new Rectangle(300, 0, 400, 400, spatialContext));
		rectangle2.add(new Rectangle(50, 350, 250, 450, spatialContext));

		List<Rectangle> rectangle3 = new ArrayList<Rectangle>();
		rectangle3.add(new Rectangle(50, 50, 450, 100, spatialContext));
		rectangle3.add(new Rectangle(50, 400, 450, 450, spatialContext));
		rectangle3.add(new Rectangle(50, 105, 100, 450, spatialContext));
		rectangle3.add(new Rectangle(400, 50, 450, 395, spatialContext));

		obstacleSets.add(rectangle1);
		obstacleSets.add(rectangle2);
		obstacleSets.add(rectangle3);
		// obstacleSets.add(new ArrayList<Object>());
		reset();
	}

	public WeightedGraph(WeightedGraph weightedGraph2) {
		this.weightedGraph = weightedGraph2;
		connectShortestPath();
	}

	private void paintBackground() {
		GraphicsContext g = getGraphicsContext2D();
		getGraphicsContext2D().setFill(Color.WHITE);
		getGraphicsContext2D().fillRect(0, 0, size, size);

		int b = 2;
		Random rand = new Random();
		for (Rectangle rect : obstacles) {
			g.setFill(Color.BLACK);
			g.fillRect(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
			g.setFill(Color.INDIANRED);
			g.fillRect(rect.getMinX() + b, rect.getMinY() + b, rect.getWidth() - 2 * b, rect.getHeight() - 2 * b);
		}
	}

	private void repaintRRT() {
		paintBackground();

		GraphicsContext g = getGraphicsContext2D();
		int d = 2;

		g.setFill(Color.BLUE);
		for (Node n : RRTPoints) {
			g.fillOval(n.point.x - d, n.point.y - d, 2 * d, 2 * d);
		}

		g.setStroke(Color.BLACK);
		for (Node n : RRTPoints) {
			if (n.parent != null) {
				g.strokeLine(n.point.x, n.point.y, n.parent.point.x, n.parent.point.y);
			}
		}
	}

	/**
	 * add n point randomly according to PRM
	 * 
	 * @param n
	 */
	public void addPRM(int n) {
		GraphicsContext g = getGraphicsContext2D();
		int b = 2;

		paintBackground();

		for (int i = 0; i < PRMPoints.size(); i++) {
			double x = PRMPoints.get(i).x;
			double y = PRMPoints.get(i).y;

			g.setFill(Color.BLUE);
			g.fillOval(x - b, y - b, 2 * b, 2 * b);
		}

		for (int i = 0; i < n; i++) {
			boolean free = false;
			int x = 0;
			int y = 0;
			while (!free) {
				Random random = new Random();
				x = random.nextInt((int) getWidth());
				y = random.nextInt((int) getHeight());
				Point point = spatialContext.makePoint(x, y);
				free = true;
				for (Rectangle rect : obstacles) {
					if (rect.contains(point)) {
						free = false;
						break;
					}
				}
			}
			g.setFill(Color.BLUE);
			g.fillOval(x - b, y - b, 2 * b, 2 * b);
			Point point = spatialContext.makePoint(x, y);
			point.setLabel("" + i);
			point.setAccumulatedDistance(Double.MAX_VALUE);
			PRMPoints.add(point);
		}

		// Testing these points and their distance
		for (int i = 0; i < n; i++) {
			Point point = PRMPoints.get(i);
			System.out.println("(x=" + point.x + ", y=" + point.y + ", r=" + point.getAccumulatedDistance() + ")");
		}
	}

	public void addRRT(int n) {
		Random rand = new Random();
		GraphicsContext g = getGraphicsContext2D();

		for (int j = 0; j < n; j++) {

			int x = rand.nextInt((int) getWidth());
			int y = rand.nextInt((int) getHeight());

			double closestDistance = 99999;
			Node closestNode = null;

			boolean tooClose = false;

			for (Node node : RRTPoints) {
				double dist = Math
						.sqrt((node.point.x - x) * (node.point.x - x) + (node.point.y - y) * (node.point.y - y));

				if (dist < 10) {
					tooClose = true;
				}
				if (dist < closestDistance) {
					closestDistance = dist;
					closestNode = node;
				}
			}

			if (tooClose || closestNode == null) {
				continue;
			}

			double delX = RRTMultiplier * ((x - closestNode.point.x) / closestDistance);
			double delY = RRTMultiplier * ((y - closestNode.point.y) / closestDistance);

			double newX = delX + closestNode.point.x;
			double newY = delY + closestNode.point.y;

			boolean collision = false;

			Point newPoint = spatialContext.makePoint(newX, newY);
			Line line = new Line(spatialContext, closestNode.point, newPoint);
			for (Rectangle rect : obstacles) {
				if (line.intersects(rect)) {
					collision = true;
					break;
				}
			}

			if (collision) {
				collision = false;

				newX = (float) delX / 2 + closestNode.point.x;
				newY = (float) delY / 2 + closestNode.point.y;
				line = new Line(spatialContext, closestNode.point, newPoint);

				for (Rectangle r : obstacles) {
					if (line.intersects(r)) {
						collision = true;
						break;
					}
				}

				if (!collision) {

					int d = 2;

					int r = rand.nextInt(255);
					int ge = rand.nextInt(255);
					int b = rand.nextInt(255);

					g.setFill(Color.rgb(r, ge, b));
					g.setFill(Color.BLUE);
					g.fillOval(newX - d, newY - d, 2 * d, 2 * d);

					RRTPoints.add(new Node(closestNode, spatialContext.makePoint(newX, newY)));
					g.setStroke(Color.BLACK);
					g.strokeLine(closestNode.point.x, closestNode.point.y, newX, newY);

				}
			} else {

				int d = 2;

				int r = rand.nextInt(255);
				int ge = rand.nextInt(255);
				int b = rand.nextInt(255);

				g.setFill(Color.rgb(r, ge, b));
				g.setFill(Color.BLUE);
				g.fillOval(newX - d, newY - d, 2 * d, 2 * d);

				RRTPoints.add(new Node(closestNode, spatialContext.makePoint(newX, newY)));
				g.setStroke(Color.BLACK);
				g.strokeLine(closestNode.point.x, closestNode.point.y, newX, newY);

			}

			if (false) {

				int d = 2;
				g.setFill(Color.RED);
				g.fillOval(x - d, y - d, 2 * d, 2 * d);
			}
		}
	}

	public void addRRTStar(int n) {
		Random rand = new Random();
		GraphicsContext g = getGraphicsContext2D();

		for (int j = 0; j < n; j++) {

			int x = rand.nextInt(((int) getWidth()));
			int y = rand.nextInt(((int) getHeight()));

			double closestDistance = 99999;
			boolean tooClose = false;

			Node closestNode = null;

			for (Node node : RRTPoints) {
				double dist = Math
						.sqrt((node.point.x - x) * (node.point.x - x) + (node.point.y - y) * (node.point.y - y));
				if (dist < 10) {
					tooClose = true;
				}

				if (dist < closestDistance) {
					closestDistance = dist;
					closestNode = node;
				}
			}

			if (tooClose || closestNode == null) {
				continue;
			}

			double delX = RRTMultiplier * ((x - closestNode.point.x) / closestDistance);
			double delY = RRTMultiplier * ((y - closestNode.point.y) / closestDistance);

			double newX = delX + closestNode.point.x;
			double newY = delY + closestNode.point.y;

			List<Node> closeNodes = new ArrayList<Node>();
			int maxDist = optimiseDistance;

			for (Node node : RRTPoints) {
				double dist = Math.sqrt(
						(node.point.x - newX) * (node.point.x - newX) + (node.point.y - newY) * (node.point.y - newY));

				if (dist < maxDist) {
					node.helper = dist;
					closeNodes.add(node);
				}
			}

			closestNode = null;
			double smallestDist = 9999;

			Point newPoint = spatialContext.makePoint(newX, newY);
			for (Node node : closeNodes) {
				boolean collision = false;
				Line line = new Line(spatialContext, closestNode.point, newPoint);
				for (Rectangle rect : obstacles) {
					if (line.intersects(rect)) {
						collision = true;
						break;
					}
				}
				if (!collision) {
					if (node.distance + node.helper < smallestDist) {
						smallestDist = node.distance + node.helper;
						closestNode = node;
					}
				}
			}

			if (closestNode == null) {
				continue;
			}

			Node toAdd;
			int d = 2;

			int r = rand.nextInt(255);
			int ge = rand.nextInt(255);
			int b = rand.nextInt(255);

			g.setFill(Color.rgb(r, ge, b));
			g.setFill(Color.BLUE);
			g.fillOval(newX - d, newY - d, 2 * d, 2 * d);

			toAdd = new Node(closestNode, spatialContext.makePoint(newX, newY));
			RRTPoints.add(toAdd);
			g.setStroke(Color.BLACK);
			g.strokeLine(closestNode.point.x, closestNode.point.y, newX, newY);

			// Point newPoint = spatialContext.makePoint(newX, newY);
			boolean changed = false;
			for (Node node : closeNodes) {
				if (node.helper + toAdd.distance < node.distance) {
					boolean canConnect = true;
					Line line = new Line(spatialContext, closestNode.point, newPoint);

					for (Rectangle rect : obstacles) {
						if (line.intersects(rect)) {
							canConnect = false;
							break;
						}
					}

					if (canConnect) {
						node.parent = toAdd;
						node.distance = node.parent.distance + node.helper;
						changed = true;
					}
				}
			}
			if (changed) {
				repaintRRT();
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public void connect() {
		for (int i = 0; i < PRMPoints.size(); i++) {
			Point point = PRMPoints.get(i);
			ArrayList<Point> closestPoints = new ArrayList<Point>(); // A set of closest points
			ArrayList<Double> closestEdges = new ArrayList<Double>(); // A set of closest edges

			// 5 null points and 5 maximum edges
			for (int j = 0; j < numOfConnections; j++) {
				closestPoints.add(null);
				closestEdges.add(99999.0);
			}
			// Get the distance from other points with intersections with obstacle.
			for (Point otherPoint : PRMPoints) {
				if (otherPoint != point) {
					boolean intersects = false;
					Line line = new Line(spatialContext, otherPoint, point);
					for (Rectangle rect : obstacles) {
						if (line.intersects(rect)) {
							intersects = true;
						}
					}
					if (intersects) {
						continue;
					}
					double distance = Math.sqrt((otherPoint.x - point.x) * (otherPoint.x - point.x)
							+ (otherPoint.y - point.y) * (otherPoint.y - point.y));
					for (int j = 0; j < numOfConnections; j++) {
						if (closestEdges.get(j) > distance) {
							closestEdges.add(j, distance);
							closestPoints.add(j, otherPoint);
							break;
						}
					}
				}
			}
			ArrayList<Point> fiveclosestPoint = new ArrayList<Point>();
			ArrayList<Double> fiveclosestEdge = new ArrayList<Double>();
			System.out.println("closestPoints.size(): " + closestPoints.size());
			System.out.println("numOfConnections: " + numOfConnections);
			for (int j = 0; j < numOfConnections; j++) {
				Point closePoint = closestPoints.get(j);
				double closeEdge = closestEdges.get(j);
				if (closePoint != null) {
					GraphicsContext g = getGraphicsContext2D();
					g.setStroke(Color.BLACK);
					g.strokeLine(closePoint.x, closePoint.y, point.x, point.y);
					fiveclosestPoint.add(closePoint);
					fiveclosestEdge.add(closeEdge);
					PRMWeightedEdge.add(new WeightEdge(point, closePoint));
				}
			}
			point.setNeighborPointsList(fiveclosestPoint);
			point.setNeighborDistanceList(fiveclosestEdge);
		}
	}

	public void reset() {
		RRTPoints.clear();
		PRMPoints.clear();
		RRTPoints.add(new Node(spatialContext.makePoint(250, 250)));
		paintBackground();
	}

	/**
	 * a list of obstacles according to the number n
	 * 
	 * @param n
	 */
	public void setObstacles(int n) {
		obstacles = obstacleSets.get(n);
		reset();
	}

	public void setRRTMultiplier(int m) {
		RRTMultiplier = m;
	}

	// *************************************************************************************
	// *************************************************************************************

	ArrayList<Point> nodes;
	ArrayList<WeightEdge> edges;
	Set<Point> settledNodes;
	Set<Point> unSettledNodes;
	Map<Point, Point> predecessors;
	Map<Point, Double> distance;

	public void execute(Point source) {
		settledNodes = new HashSet<Point>();
		unSettledNodes = new HashSet<Point>();
		distance = new HashMap<Point, Double>();
		predecessors = new HashMap<Point, Point>();
		distance.put(source, 0.0);
		unSettledNodes.add(source);
		nodes = PRMPoints;
		edges = PRMWeightedEdge;
		///wEdgeList = new ArrayList<>(getEdges());
		while (unSettledNodes.size() > 0) {
			Point node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}

	private void findMinimalDistances(Point point) {
		ArrayList<Point> adjacentNodes = point.getNeighbourd();// getNeighbors(point);

		for (int i = 0; i < adjacentNodes.size(); i++) {
			Point target = adjacentNodes.get(i);
			if (getShortestDistance(target) > getShortestDistance(point) + getDistance(point, target)) {
				distance.put(target, getShortestDistance(point) + getDistance(point, target));
				predecessors.put(target, point);
				unSettledNodes.add(target);
			}
		}

		for (Point target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(point) + getDistance(point, target)) {
				distance.put(target, getShortestDistance(point) + getDistance(point, target));
				predecessors.put(target, point);
				unSettledNodes.add(target);
			}
		}
	}

	private double getDistance(Point node, Point target) {
		for (WeightEdge edge : edges) {
			if (edge.getSource().equals(node) && edge.getDestination().equals(target)) {
				return edge.weight;
			}
		}
		throw new RuntimeException("Should not happen");
	}

//	private List<Point> getNeighbors(Point node) {
//		List<Point> neighbors = new ArrayList<Point>();
//		for (WeightEdge edge : edges) {
//			if (edge.getSource().equals(node) && !isSettled(edge.getDestination())) {
//				neighbors.add(edge.getDestination());
//			}
//		}
//		return neighbors;
//	}

	private Point getMinimum(Set<Point> Pointes) {
		Point minimum = null;
		for (Point point : Pointes) {
			if (minimum == null) {
				minimum = point;
			} else {
				if (getShortestDistance(point) < getShortestDistance(minimum)) {
					minimum = point;
				}
			}
		}
		return minimum;
	}

	private boolean isSettled(Point point) {
		return settledNodes.contains(point);
	}

	private double getShortestDistance(Point destination) {
		Double d = distance.get(destination);
		if (d == null) {
			return Double.MAX_VALUE;
		} else {
			return d;
		}
	}

	/*
	 * This method returns the path from the source to the selected target and NULL
	 * if no path exists
	 */
	public ArrayList<Point> getPath(Point target) {
		ArrayList<Point> path = new ArrayList<Point>();
		Point step = target;
		// check if a path exists
		if (predecessors.get(step) == null) {
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		// Put it into the correct order
		Collections.reverse(path);
		return path;
	}
	// ************************************************************************************************
	// ************************************************************************************************

	/**
	 * connect the shortest Path according to these points from getPath(goal)
	 */
	public void connectShortestPath() {
		System.out.println("The number of point: " + PRMPoints.size());
		startPoint = getMinXPoint(PRMPoints);
		System.out.println("The start point is: "+ startPoint);
		execute(startPoint);
		goal = getMaxXPoint(PRMPoints);
		System.out.println("The goal point is: "+ goal);
		shortestPathPoints = getPath(goal);

			for (int i = 1; i < shortestPathPoints.size(); i++) {
				Point point = shortestPathPoints.get(i);
				System.out.println("point: " + point);
				//if (!point.equals(goal)) {
					GraphicsContext g = getGraphicsContext2D();
					g.setStroke(Color.RED);
					g.strokeLine(startPoint.x, startPoint.y, point.x, point.y);
					startPoint = point;
				//}
			}
	}

//	public ArrayList<Point> findPath() {
//		System.out.println("The number of point: " + PRMPoints.size());
//		// Point startPoint = getStart(PRMPoints);
//		goal = getMaxYPoint(PRMPoints);
//		// System.out.println("The start point is: "+ getStart(PRMPoints));
//		// System.out.println("The start point is: "+ getMaxYPOint(PRMPoints));
//
//		ArrayList<Point> pointList = PRMPoints;
//		startPoint = getMinXPoint(pointList);
//		startPoint.setAccumulatedDistance(0);
//		shortestPathPoints.add(startPoint);
//		// ArrayList<Point> pointList = new ArrayList<Point>(weightedGraph.getPoints());
//		Point currentPoint = startPoint;
//		// while (!pointList.isEmpty()) {
//		while (!currentPoint.isVisited()) {
//			System.out.println("The current point is: " + currentPoint);
//			if (currentPoint.equals(goal))
//				break;
//
//			for (int i = 0; i < currentPoint.getNeighborEdgeList().size(); i++) {
//
//				Point neighborPoint = currentPoint.getNeighborPointsList().get(i);
//
//				double distance = currentPoint.getAccumulatedDistance() + currentPoint.getNeighborEdgeList().get(i);
//				System.out.println("neighborPoint.getAccumulatedDistance(): " + neighborPoint.getAccumulatedDistance());
//				// System.out.println("distance: "+distance);
//				if (distance < neighborPoint.getAccumulatedDistance()) {
//					System.out.println("neighbor points: " + neighborPoint);
//					neighborPoint.setAccumulatedDistance(distance);
//					// neighborPoint.setPreviousPoint(startPoint);
//					shortestPathPoints.add(neighborPoint);
//				}
//			}
//			int index = 0;
//			if (index < currentPoint.getNeighborPointsList().size()) {
//				currentPoint.setVisited(true);
//				currentPoint = currentPoint.getNeighborPointsList().get(index);
//				index++;
//			}
//		}
//		return shortestPathPoints;
//	}

	/**
	 * Achieving a minimum path with a list of state name
	 * 
	 * @param list
	 * @return
	 */
	public Point getMinXPoint(ArrayList<Point> list) {
		GraphicsContext g = getGraphicsContext2D();
		int index = 0;
		double xcor = list.get(0).getX();
		for (int i = 1; i < list.size(); i++) {
			if (xcor > list.get(i).getX()) {
				xcor = list.get(i).getX();
				index = i;
			}
		}
		Point minXPoint = list.get(index);
		g.setFill(Color.CYAN);
		g.fillRect(minXPoint.x - 3, minXPoint.y - 3, 6, 6);//.fillOval(minXPoint.x - 3, minXPoint.y - 3, 6, 6);
		return minXPoint;
	}

	/**
	 * Achieving a minimum path with a list of state name
	 * 
	 * @param list
	 * @return
	 */
	public Point getMaxXPoint(ArrayList<Point> list) {
		GraphicsContext g = getGraphicsContext2D();
		int index = 0;
		double xcor = list.get(0).getX();
		for (int i = 1; i < list.size(); i++) {
			if (xcor < list.get(i).getX()) {
				xcor = list.get(i).getX();
				index = i;
			}
		}
		Point maxYPoint = list.get(index);
		g.setFill(Color.FUCHSIA);
		g.fillOval(maxYPoint.x - 3, maxYPoint.y - 3, 6, 6);
		return maxYPoint;
	}

}
