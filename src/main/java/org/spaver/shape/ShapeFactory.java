package org.spaver.shape;

import org.spaver.context.SpatialContext;
import org.spaver.context.SpatialContextFactory;

public class ShapeFactory {

	//SpatialContext getSpatialContext();

	protected final SpatialContext ctx;

	private final boolean normWrapLongitude;

	public ShapeFactory(SpatialContext ctx, SpatialContextFactory factory) {
		this.ctx = ctx;
		this.normWrapLongitude = factory.normWrapLongitude;
	}

	public SpatialContext getSpatialContext() {
		return ctx;
	}
	
	/** Construct a point. */
	public Point pointXY(double x, double y) {
		return new Point(x, y, ctx);
	}
	/** Construct a point of latitude, longitude coordinates */
	public Point pointLatLon(double latitude, double longitude) {
		return new Point(longitude, latitude, ctx);
	}
	/**
	 * Construct a point of 3 dimensions. The implementation might ignore
	 * unsupported dimensions like 'z' or throw an error.
	 */
	public Point pointXYZ(double x, double y, double z) {
		return pointXY(x, y); // or throw?
	}
	
	/** Construct a rectangle. */
	public Rectangle rect(Point lowerLeft, Point upperRight) {
		return rect(lowerLeft.getX(), upperRight.getX(), lowerLeft.getY(), upperRight.getY());
	}

	/**
	 * Construct a rectangle. If just one longitude is on the dateline (+/- 180) and
	 * if {@link SpatialContext#isGeo()} then potentially adjust its sign to ensure
	 * the rectangle does not cross the dateline (aka anti-meridian).
	 */
	public Rectangle rect(double minX, double maxX, double minY, double maxY) {
		Rectangle bounds = ctx.getWorldBounds();
		// Y
		if (minY < bounds.getMinY() || maxY > bounds.getMaxY())// NaN will pass
			System.out.println("Y values [" + minY + " to " + maxY + "] not in boundary " + bounds);
		if (minY > maxY)
			System.out.println("maxY must be >= minY: " + minY + " to " + maxY);
		// X
//		if (ctx.isGeo()) {
////	      verifyX(minX);
////	      verifyX(maxX);
//			// TODO consider removing this logic so that there is no normalization here
//			// if (minX != maxX) { USUALLY TRUE, inline check below
//			// If an edge coincides with the dateline then don't make this rect cross it
//			if (minX == 180 && minX != maxX) {
//				minX = -180;
//			} else if (maxX == -180 && minX != maxX) {
//				maxX = 180;
//			}
//			// }
//		} else {
		if (minX < bounds.getMinX() || maxX > bounds.getMaxX())// NaN will pass
			System.out.println("X values [" + minX + " to " + maxX + "] not in boundary " + bounds);
		if (minX > maxX)
			System.out.println("maxX must be >= minX: " + minX + " to " + maxX);
		// }
		return new Rectangle(minX, maxX, minY, maxY, ctx);
	}
	
	
	/**
	 * Construct a circle. The units of "distance" should be the same as x &amp; y.
	 */
	public Circle circle(double x, double y, double distance) {
		return circle(pointXY(x, y), distance);
	}

	/**
	 * Construct a circle. The units of "distance" should be the same as x &amp; y.
	 */
	public Circle circle(Point point, double distance) {
		if (distance < 0)
			System.out.println("distance must be >= 0; got " + distance);
//		if (ctx.isGeo()) {
//			if (distance > 180) {
//				// (it's debatable whether to error or not)
//				// throw new InvalidShapeException("distance must be <= 180; got " + distance);
//				distance = 180;
//			}
//			return new GeoCircle(point, distance, ctx);
//		} else {
		return new Circle(point, distance, ctx);
//		}
	}

}
