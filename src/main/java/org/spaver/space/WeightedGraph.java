package org.spaver.space;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.spaver.context.SpatialContext;
import org.spaver.distance.CartesianDistCalc;
import org.spaver.shape.Point;

public class WeightedGraph implements TopometricModel<Point, WeightEdge> {

	private ArrayList<Point> locList; // list of locations
	private ArrayList<WeightEdge> wEdgeList; // list of locations
	private double[][] dM;
	SpatialContext spatialContext;

	public WeightedGraph() {
		this.locList = new ArrayList<Point>();
		this.wEdgeList = new ArrayList<WeightEdge>();
	}

	/**
	 * Add location
	 */
	public void addLoc(String label, int position) {
		Point location = new Point(spatialContext, label, position);
		locList.add(location);
	}

	/**
	 * Add edge problem puoi avere diversi archi da stesso input output
	 */
	public void addEdge(int start, int end, double weight) {
		if (weight < 0.0) {
			throw new IllegalArgumentException();
		}
		locList.get(start).addNeighbour(locList.get(end));
		locList.get(end).addNeighbour(locList.get(start));
		wEdgeList.add(new WeightEdge(locList.get(start), locList.get(end), weight));
	}

	/**
	 * locations position
	 */
	public int getPosition(String label) {
		for (Point l : this.locList) {
			if (l.getLabel().equals(label)) {
				return l.getPosition();
			}
		}
		return 0;
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

	public Set<Point> closure(Set<Point> set) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object[] getEdgesArray() {
		return wEdgeList.toArray();
	}

	/**
	 * Neighborhoods
	 */
	public ArrayList<Point> returnNeighbourd(Point l) {
		return l.getNeighbourd();
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
	public void dMcomputation() {
		CartesianDistCalc infoGraph = new CartesianDistCalc();
		this.dM = infoGraph.returnDistMatrix();
	}

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

	public int getNumberOfEdges() {
		return this.wEdgeList.size();
	}

	public WeightEdge getEdge(int i) {
		return this.wEdgeList.get(i);
	}

	public double getWeight(int i, int j) {
		for (WeightEdge e : wEdgeList) {
			if ((e.lStart.getPosition() == i) && (e.lEnd.getPosition() == j)) {
				return e.weight;
			}
		}
		return -1;
	}

	public static WeightedGraph createGrid(int rows, int columns, double distance) {
		WeightedGraph model = new WeightedGraph();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				model.addLoc((i + 1) + "_" + (j + 1), i * columns + j);
			}
		}
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (j != columns - 1) {
					model.addEdge(i * columns + j, i * columns + (j + 1), distance);
				}
				if (i != rows - 1) {
					model.addEdge(i * columns + j, (i + 1) * columns + j, distance);
				}
			}
		}
		return model;
	}

	public Set<Point> pre(Point location) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Point> post(Point location) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Point> pre(Set<Point> locationSet) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Point> post(Set<Point> locationSet) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Point> getSet(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
