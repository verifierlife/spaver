package org.spaver.context;

import org.spaver.distance.CartesianDistCalc;
import org.spaver.distance.DistanceCalculator;
import org.spaver.distance.GeodesicSphereDistCalc;
import org.spaver.shape.Circle;
import org.spaver.shape.Point;
import org.spaver.shape.Rectangle;
import org.spaver.shape.ShapeFactory;

public class SpatialContext {

	/** A popular default SpatialContext implementation for geospatial. */
	public static final SpatialContext GEO = new SpatialContext(new SpatialContextFactory());

	private final ShapeFactory shapeFactory;
	private final DistanceCalculator calculator;
	private final Rectangle worldBounds;
 
	/**
	 * Called by
	 * {@link org.locationtech.spatial4j.context.SpatialContextFactory#newSpatialContext()}.
	 */
	public SpatialContext(SpatialContextFactory factory) {
		this.shapeFactory = factory.makeShapeFactory(this);

		if (factory.distCalc == null) {
			this.calculator = new CartesianDistCalc();
		} else {
			this.calculator = factory.distCalc;
		}

		// TODO remove worldBounds from Spatial4j: see Issue #55
		Rectangle bounds = factory.worldBounds;
		if (bounds == null) {
			this.worldBounds = new Rectangle(-Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE, this);
		} else {
			if (bounds.getMinX() > bounds.getMaxX())
				throw new IllegalArgumentException("worldBounds minX should be <= maxX: " + bounds);
			if (bounds.getMinY() > bounds.getMaxY())
				throw new IllegalArgumentException("worldBounds minY should be <= maxY: " + bounds);
			// hopefully worldBounds' rect implementation is compatible
			this.worldBounds = new Rectangle(bounds, this);
		}
	}

	/**
	 * Is the mathematical world model based on a sphere, or is it a flat plane? The
	 * word "geodetic" or "geodesic" is sometimes used to refer to the former, and
	 * the latter is sometimes referred to as "Euclidean" or "cartesian".
	 */
//	public boolean isGeo() {
//		return geo;
//	}

	/** A factory for {@link Shape}s. */
	public ShapeFactory getShapeFactory() {
		return shapeFactory;
	}

	public DistanceCalculator getDistCalc() {
		return calculator;
	}

	/** Convenience that uses {@link #getDistCalc()} */
	public double calcDistance(Point p, double x2, double y2) {
		return getDistCalc().distance(p, x2, y2);
	}

	/** Convenience that uses {@link #getDistCalc()} */
	public double calcDistance(Point p, Point p2) {
		return getDistCalc().distance(p, p2);
	}

	/**
	 * The extent of x &amp; y coordinates should fit within the return'ed
	 * rectangle. Do *NOT* invoke reset() on this return type.
	 */
	public Rectangle getWorldBounds() {
		return worldBounds;
	}

	/** Construct a point. */
	public Point makePoint(double x, double y) {
		return shapeFactory.pointXY(x, y);
	}

	/** Construct a rectangle. */
	public Rectangle makeRectangle(Point lowerLeft, Point upperRight) {
		return shapeFactory.rect(lowerLeft, upperRight);
	}

	/**
	 * Construct a rectangle. If just one longitude is on the dateline (+/- 180)
	 * (aka anti-meridian) then potentially adjust its sign to ensure the rectangle
	 * does not cross the dateline.
	 */
	public Rectangle makeRectangle(double minX, double maxX, double minY, double maxY) {
		return shapeFactory.rect(minX, maxX, minY, maxY);
	}

	/**
	 * Construct a circle. The units of "distance" should be the same as x &amp; y.
	 */
	public Circle makeCircle(double x, double y, double distance) {
		return shapeFactory.circle(x, y, distance);
	}

	/**
	 * Construct a circle. The units of "distance" should be the same as x &amp; y.
	 */
	public Circle makeCircle(Point point, double distance) {
		return shapeFactory.circle(point, distance);
	}

}
