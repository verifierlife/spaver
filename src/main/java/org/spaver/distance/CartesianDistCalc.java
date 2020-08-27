package org.spaver.distance;

import java.util.Collection;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.spaver.context.SpatialContext;
import org.spaver.shape.Circle;
import org.spaver.shape.Point;
import org.spaver.shape.Rectangle;
import org.spaver.space.WeightedGraph;

public class CartesianDistCalc extends AbstractDistanceCalculator {

	WeightedGraph weightedGraph;
	public SimpleWeightedGraph<String, DefaultWeightedEdge> jGraphT;

	public static final CartesianDistCalc INSTANCE = new CartesianDistCalc();
	public static final CartesianDistCalc INSTANCE_SQUARED = new CartesianDistCalc(true);

	private final boolean squared;

	public CartesianDistCalc() {
		this.squared = false;
	}

	/**
	 * @param squared Set to true to have
	 *                {@link #distance(org.locationtech.spatial4j.shape.Point, org.locationtech.spatial4j.shape.Point)}
	 *                return the square of the correct answer. This is a performance
	 *                optimization used when sorting in which the actual distance
	 *                doesn't matter so long as the sort order is consistent.
	 */
	public CartesianDistCalc(boolean squared) {
		this.squared = squared;
	}

	public double distance(Point from, double toX, double toY) {
		double xSquaredPlusYSquared = distanceSquared(from.getX(), from.getY(), toX, toY);
		if (squared)
			return xSquaredPlusYSquared;

		return Math.sqrt(xSquaredPlusYSquared);
	}

	private static double distanceSquared(double fromX, double fromY, double toX, double toY) {
		double deltaX = fromX - toX;
		double deltaY = fromY - toY;
		return deltaX * deltaX + deltaY * deltaY;
	}

	/**
	 * Distance from point to a line segment formed between points 'v' and 'w'. It
	 * respects the "squared" option.
	 */
	// TODO add to generic DistanceCalculator and develop geo versions.
	public double distanceToLineSegment(Point point, double vX, double vY, double wX, double wY) {
		// Translated from: http://bl.ocks.org/mbostock/4218871
		double d = distanceSquared(vX, vY, wX, wY);
		double toX;
		double toY;
		if (d <= 0) {
			toX = vX;
			toY = vY;
		} else {
			// t = ((point[0] - v[0]) * (w[0] - v[0]) + (point[1] - v[1]) * (w[1] - v[1])) /
			// d
			double t = ((point.getX() - vX) * (wX - vX) + (point.getY() - vY) * (wY - vY)) / d;
			if (t < 0) {
				toX = vX;
				toY = vY;
			} else if (t > 1) {
				toX = wX;
				toY = wY;
			} else {
				toX = vX + t * (wX - vX);
				toY = vY + t * (wY - vY);
			}
		}
		return distance(point, toX, toY);
	}

	@Override
	public boolean pointInCircle(Point from, double toX, double toY, double distance) {
		double deltaX = from.getX() - toX;
		double deltaY = from.getY() - toY;
		return deltaX * deltaX + deltaY * deltaY <= distance * distance;
	}

	public Point pointOnBearing(Point from, double distDEG, double bearingDEG, SpatialContext ctx, Point reuse) {
		if (distDEG == 0) {
			if (reuse == null)
				return from;
			reuse.reset(from.getX(), from.getY());
			return reuse;
		}
		double bearingRAD = DistanceUtils.toRadians(bearingDEG);
		double x = from.getX() + Math.sin(bearingRAD) * distDEG;
		double y = from.getY() + Math.cos(bearingRAD) * distDEG;
		if (reuse == null) {
			return ctx.makePoint(x, y);
		} else {
			reuse.reset(x, y);
			return reuse;
		}
	}

	public Rectangle calcBoxByDistFromPt(Point from, double distDEG, SpatialContext ctx, Rectangle reuse) {
		double minX = from.getX() - distDEG;
		double maxX = from.getX() + distDEG;
		double minY = from.getY() - distDEG;
		double maxY = from.getY() + distDEG;
		if (reuse == null) {
			return ctx.makeRectangle(minX, maxX, minY, maxY);
		} else {
			reuse.reset(minX, maxX, minY, maxY);
			return reuse;
		}
	}

	public double calcBoxByDistFromPt_yHorizAxisDEG(Point from, double distDEG, SpatialContext ctx) {
		return from.getY();
	}

	public double area(Rectangle rect) {
		return rect.getArea(null);
	}

	public double area(Circle circle) {
		return circle.getArea(null);
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		CartesianDistCalc that = (CartesianDistCalc) o;

		if (squared != that.squared)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return (squared ? 1 : 0);
	}

	/**
	 * return distance matrix
	 * 
	 */
//	public double[][] returnDistMatrix() {
//		double[][] dMatrix = new double[weightedGraph.getNumberOfLocations()][weightedGraph.getNumberOfLocations()];
//		FloydWarshallShortestPaths<String, DefaultWeightedEdge> path = new FloydWarshallShortestPaths<String, DefaultWeightedEdge>(
//				this.jGraphT);
//		Collection<GraphPath<String, DefaultWeightedEdge>> pathCollection = path.getShortestPaths();
//		for (GraphPath<String, DefaultWeightedEdge> gPath : pathCollection) {
//			int start = weightedGraph.getPoint(gPath.getStartVertex());//.getPosition(gPath.getStartVertex());
//			//int start = weightedGraph.getPosition(gPath.getStartVertex());
//			int end = weightedGraph.getPosition(gPath.getEndVertex());
//			dMatrix[start][end] = gPath.getWeight();
//		}
//
//		for (int i = 0; i < weightedGraph.getNumberOfLocations(); i++)
//			dMatrix[i][i] = 0;
//		return dMatrix;
//	}

	// // return distance matrix
	public double[][] returnDistMatrixOld() {
		double[][] dMatrix = new double[weightedGraph.getNumberOfLocations()][weightedGraph.getNumberOfLocations()];

		for (int i = 0; i < weightedGraph.getNumberOfLocations(); i++) {
			for (int j = 0; j < weightedGraph.getNumberOfLocations(); j++) {
				if (j == i)
					dMatrix[j][j] = 0;
				else if (i < j) {
					DijkstraShortestPath<String, DefaultWeightedEdge> path = new DijkstraShortestPath<String, DefaultWeightedEdge>(
							jGraphT, weightedGraph.getLocation(i).getLabel(), weightedGraph.getLocation(i).getLabel());
					dMatrix[i][j] = path.getPathLength();
				}
			}
		}
		for (int i = 0; i < weightedGraph.getNumberOfLocations(); i++) {
			for (int j = 0; j < weightedGraph.getNumberOfLocations(); j++) {
				if (i > j)
					dMatrix[i][j] = dMatrix[j][i];
			}
		}
		return dMatrix;
	}

}
