package org.spaver.shape;

import java.util.ArrayList;

import org.spaver.context.SpatialContext;

public class Point extends BaseShape<SpatialContext> implements Shape {

	public double x;
	public double y;
	private String label;
	double accumulatedDistance;// record the accumulated distance
	ArrayList<Double> neighborDistanceList = new ArrayList<Double>();
	ArrayList<Point> neighborPointList = new ArrayList<Point>(); //
	SpatialContext spatialContext;
	Point previousPoint;
	boolean visited;

	/**
	 * Constructing a array list to store points
	 * 
	 * @param ctx
	 */
	public Point(SpatialContext ctx) {
		super(ctx);
		this.spatialContext = ctx;
	}

	/**
	 * Constructing a array list to store points
	 * 
	 * @param ctx
	 */
	public Point(SpatialContext ctx, String label) {
		super(ctx);
		this.spatialContext = ctx;
		this.label = label;
	}

	/**
	 * Constructing a array list to store points with labels and positions in
	 * 1-dimension.
	 * 
	 * @param ctx
	 * @param label
	 * @param position
	 */
	public Point(SpatialContext ctx, String label, double x, double y) {
		super(ctx);
		this.spatialContext = ctx;
		this.label = label;
		this.x = x;
		this.y = y;
	}

	public Point getPreviousPoint() {
		return previousPoint;
	}

	public void setPreviousPoint(Point previousPoint) {
		this.previousPoint = previousPoint;
	}
	
	public ArrayList<Double> getNeighborEdgeList() {
		return neighborDistanceList;
	}

	public void setNeighborDistanceList(ArrayList<Double> neighborDistanceList) {
		this.neighborDistanceList = neighborDistanceList;
	}
	
	public double getAccumulatedDistance() {
		return accumulatedDistance;
	}

	public void setAccumulatedDistance(double distance) {
		this.accumulatedDistance = distance;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Point getPoint(String label) {
		return new Point(x, y, spatialContext);
	}

	/**
	 * set a point and add it to the adj list
	 * @param x
	 * @param y
	 * @param label
	 */
	public void addPoint(double x, double y, String label) {
		Point point = new Point(spatialContext, label, x, y);
		neighborPointList.add(point);
	}

	public ArrayList<Point> getNeighborPointsList() {
		return neighborPointList;
	}

	public void setNeighborPointsList(ArrayList<Point> neighborPointList) {
		this.neighborPointList = neighborPointList;
	}

//	public boolean equals(Object obj) {
//		if ((obj != null) && (obj instanceof Point)) {
//			return ((Point) obj).getX() == this.getX() && ((Point) obj).getY() == this.getY();
//		}
//		return equals(this, obj);
//	}

	public String toString() {
		return getLabel() + ": Pt(x=" + x + ", y=" + y +", d="+ accumulatedDistance+")";
	}

	// neighbors
	public void addNeighbour(Point neighborPoint) {
		this.neighborPointList.add(neighborPoint);
	}

	public ArrayList<Point> getNeighbourd() {
		return this.neighborPointList;
	}

	public boolean isVisited() {
		return visited;
	}

	
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	/**
	 * A simple constructor of a point with (double) x and y.
	 * 
	 * @param x
	 * @param y
	 * @param ctx
	 */
	public Point(double x, double y, SpatialContext ctx) {
		super(ctx);
		reset(x, y);
	}

	public boolean isEmpty() {
		return Double.isNaN(x);
	}

	/**
	 * Expert: Resets the state of this shape given the arguments. This is a
	 * performance feature to avoid excessive Shape object allocation as well as
	 * some argument error checking. Mutable shapes is error-prone so use with care.
	 */
	public void reset(double x, double y) {
		assert !isEmpty();
		this.x = x;
		this.y = y;
	}

	/**
	 * The X coordinate, or Longitude in geospatial contexts.
	 * 
	 * @return
	 */
	public double getX() {
		return x;
	}

	/**
	 * The Y coordinate, or Latitude in geospatial contexts.
	 * 
	 * @return
	 */
	public double getY() {
		return y;
	}

	/**
	 * Convenience method that usually maps on
	 * {@link org.locationtech.spatial4j.shape.Point#getY()}
	 * 
	 * @return
	 */
	public double getLat() {
		return getY();
	}

	/**
	 * Convenience method that usually maps on
	 * {@link org.locationtech.spatial4j.shape.Point#getX()}
	 * 
	 * @return
	 */
	public double getLon() {
		return getX();
	}

	public Rectangle getBoundingBox() {
		return ctx.makeRectangle(this, this);
	}

	public Point getCenter() {
		return this;
	}

	public Circle getBuffered(double distance, SpatialContext ctx) {
		return ctx.makeCircle(this, distance);
	}

	public SpatialRelation relate(Shape other) {
		if (isEmpty() || other.isEmpty())
			return SpatialRelation.DISCONNECT;
		if (other instanceof Point)
			return this.equals(other) ? SpatialRelation.PARTIALOVERLAP : SpatialRelation.DISCONNECT;
		return other.relate(this).transpose();
	}

	public boolean hasArea() {
		return false;
	}

	public double getArea(SpatialContext ctx) {
		return 0;
	}

//	@Override
//	public String toString() {
//		return "Pt(x=" + x + ",y=" + y + ")";
//	}

	public boolean equals(Object o) {
		return equals(this, o);
	}

	/**
	 * All {@link Point} implementations should use this definition of
	 * {@link Object#equals(Object)}.
	 */
	public static boolean equals(Point thiz, Object o) {
		assert thiz != null;
		if (thiz == o)
			return true;
		if (!(o instanceof Point))
			return false;

		Point point = (Point) o;

		if (Double.compare(point.getX(), thiz.getX()) != 0)
			return false;
		if (Double.compare(point.getY(), thiz.getY()) != 0)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return hashCode(this);
	}

	/**
	 * All {@link Point} implementations should use this definition of
	 * {@link Object#hashCode()}.
	 */
	public static int hashCode(Point thiz) {
		int result;
		long temp;
		temp = thiz.getX() != +0.0d ? Double.doubleToLongBits(thiz.getX()) : 0L;
		result = (int) (temp ^ (temp >>> 32));
		temp = thiz.getY() != +0.0d ? Double.doubleToLongBits(thiz.getY()) : 0L;
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	public SpatialContext getContext() {
		// TODO Auto-generated method stub
		return null;
	}
}
