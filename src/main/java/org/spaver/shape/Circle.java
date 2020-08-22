package org.spaver.shape;

import org.spaver.context.SpatialContext;

public class Circle extends BaseShape<SpatialContext> implements Shape {

	protected final Point point;
	protected double radiusDEG;

	// calculated & cached
	protected Rectangle enclosingBox;

	public Circle(Point p, double radiusDEG, SpatialContext ctx) {
		super(ctx);
		// We assume any validation of params already occurred (including bounding dist)
		
		this.point = p;
		this.radiusDEG = point.isEmpty() ? Double.NaN : radiusDEG;
		this.enclosingBox = point.isEmpty() ? ctx.makeRectangle(Double.NaN, Double.NaN, Double.NaN, Double.NaN): ctx.getDistCalc().calcBoxByDistFromPt(point, this.radiusDEG, ctx, null);
	}

	/**
	 * Expert: Resets the state of this shape given the arguments. This is a
	 * performance feature to avoid excessive Shape object allocation as well as
	 * some argument error checking. Mutable shapes is error-prone so use with care.
	 */
	public void reset(double x, double y, double radiusDEG) {
		assert !isEmpty();
		point.reset(x, y);
		this.radiusDEG = radiusDEG;
		this.enclosingBox = ctx.getDistCalc().calcBoxByDistFromPt(point, this.radiusDEG, ctx, enclosingBox);
	}

	public boolean isEmpty() {
		return point.isEmpty();
	}

	public Point getCenter() {
		return point;
	}

	/**
	 * The distance from the point's center to its edge, measured in the same units
	 * as x &amp; y (e.g. degrees if WGS84).
	 */
	public double getRadius() {
		return radiusDEG;
	}

	public double getArea(SpatialContext ctx) {
		if (ctx == null) {
			return Math.PI * radiusDEG * radiusDEG;
		} else {
			return ctx.getDistCalc().area(this);
		}
	}

	public Circle getBuffered(double distance, SpatialContext ctx) {
		return ctx.makeCircle(point, distance + radiusDEG);
	}

	public boolean contains(double x, double y) {
		return ctx.getDistCalc().pointInCircle(point, x, y, radiusDEG);
	}

	public boolean hasArea() {
		return radiusDEG > 0;
	}

	/**
	 * Note that the bounding box might contain a minX that is &gt; maxX, due to
	 * WGS84 anti-meridian.
	 */
	public Rectangle getBoundingBox() {
		return enclosingBox;
	}

	public SpatialRelation relate(Shape other) {
		// This shortcut was problematic in testing due to distinctions of
		// CONTAINS/WITHIN for no-area shapes (lines, points).
//		    if (distance == 0) {
//		      return point.relate(other,ctx).intersects() ? SpatialRelation.WITHIN : SpatialRelation.DISJOINT;
//		    }
		if (isEmpty() || other.isEmpty())
			return SpatialRelation.DISCONNECT;
		if (other instanceof Point) {
			return relate((Point) other);
		}
		if (other instanceof Rectangle) {
			return relate((Rectangle) other);
		}
		if (other instanceof Circle) {
			return relate((Circle) other);
		}
		return other.relate(this).transpose();
	}

	public SpatialRelation relate(Point point) {
		return contains(point.getX(), point.getY()) ? SpatialRelation.NONTANGENCY : SpatialRelation.DISCONNECT;
	}

	public SpatialRelation relate(Rectangle r) {
		// Note: Surprisingly complicated!

		// --We start by leveraging the fact we have a calculated bbox that is "cheaper"
		// than use of DistanceCalculator.
		final SpatialRelation bboxSect = enclosingBox.relate(r);
		if (bboxSect == SpatialRelation.DISCONNECT || bboxSect == SpatialRelation.NONTANGENCY)
			return bboxSect;
		else if (bboxSect == SpatialRelation.NONTANGENCY && enclosingBox.equals(r))// nasty identity edge-case
			return SpatialRelation.NONTANGENCY;
		// bboxSect is INTERSECTS or CONTAINS
		// The result can be DISJOINT, CONTAINS, or INTERSECTS (not WITHIN)

		return relateRectanglePhase2(r, bboxSect);
	}

	protected SpatialRelation relateRectanglePhase2(final Rectangle r, SpatialRelation bboxSect) {
		// DOES NOT WORK WITH GEO CROSSING DATELINE OR WORLD-WRAP. Other methods handle
		// such cases.

		// At this point, the only thing we are certain of is that circle is *NOT*
		// WITHIN r, since the
		// bounding box of a circle MUST be within r for the circle to be within r.

		// Quickly determine if they are DISJOINT or not.
		// Find the closest & farthest point to the circle within the rectangle
		final double closestX, farthestX;
		final double xAxis = getXAxis();
		if (xAxis < r.getMinX()) {
			closestX = r.getMinX();
			farthestX = r.getMaxX();
		} else if (xAxis > r.getMaxX()) {
			closestX = r.getMaxX();
			farthestX = r.getMinX();
		} else {
			closestX = xAxis; // we don't really use this value but to check this condition
			farthestX = r.getMaxX() - xAxis > xAxis - r.getMinX() ? r.getMaxX() : r.getMinX();
		}

		final double closestY, farthestY;
		final double yAxis = getYAxis();
		if (yAxis < r.getMinY()) {
			closestY = r.getMinY();
			farthestY = r.getMaxY();
		} else if (yAxis > r.getMaxY()) {
			closestY = r.getMaxY();
			farthestY = r.getMinY();
		} else {
			closestY = yAxis; // we don't really use this value but to check this condition
			farthestY = r.getMaxY() - yAxis > yAxis - r.getMinY() ? r.getMaxY() : r.getMinY();
		}

		// If r doesn't overlap an axis, then could be disjoint. Test closestXY
		if (xAxis != closestX && yAxis != closestY) {
			if (!contains(closestX, closestY))
				return SpatialRelation.DISCONNECT;
		} // else CAN'T be disjoint if spans axis because earlier bbox check ruled that
			// out

		// Now, we know it's *NOT* DISJOINT and it's *NOT* WITHIN either.
		// Does circle CONTAINS r or simply intersect it?

		// If circle contains r, then its bbox MUST also CONTAIN r.
		if (bboxSect != SpatialRelation.NONTANGENCY)
			return SpatialRelation.PARTIALOVERLAP;

		// If the farthest point of r away from the center of the circle is contained,
		// then all of r is
		// contained.
		if (!contains(farthestX, farthestY))
			return SpatialRelation.PARTIALOVERLAP;

		// geodetic detection of farthest Y when rect crosses x axis can't be reliably
		// determined, so
		// check other corner too, which might actually be farthest
		if (point.getY() != getYAxis()) {// geodetic
			if (yAxis == closestY) {// r crosses north to south over x axis (confusing)
				double otherY = (farthestY == r.getMaxY() ? r.getMinY() : r.getMaxY());
				if (!contains(farthestX, otherY))
					return SpatialRelation.PARTIALOVERLAP;
			}
		}

		return SpatialRelation.NONTANGENCY;
	}

	/**
	 * The <code>Y</code> coordinate of where the circle axis intersect.
	 */
	protected double getYAxis() {
		return point.getY();
	}

	/**
	 * The <code>X</code> coordinate of where the circle axis intersect.
	 */
	protected double getXAxis() {
		return point.getX();
	}

	public SpatialRelation relate(Circle circle) {
		double crossDist = ctx.getDistCalc().distance(point, circle.getCenter());
		double aDist = radiusDEG, bDist = circle.getRadius();
		if (crossDist > aDist + bDist)
			return SpatialRelation.DISCONNECT;
		if (crossDist == aDist + bDist)
			return SpatialRelation.PARTIALOVERLAP;
		if (crossDist < aDist && crossDist + bDist == aDist)
			return SpatialRelation.TANGENCY;
		if (crossDist < bDist && crossDist + aDist == bDist)
			return SpatialRelation.TANGENCYINVERSE;
		if (crossDist < aDist && crossDist + bDist < aDist)
			return SpatialRelation.NONTANGENCY;
		if (crossDist < bDist && crossDist + aDist < bDist)
			return SpatialRelation.NONTANGENCYINVERSE;
		if ((crossDist == 0) && (aDist == bDist))
			return SpatialRelation.EQUAL;

		return SpatialRelation.DISCONNECT;
	}

	@Override
	public String toString() {
		return "Circle(" + point + ", d=" + radiusDEG + "бу)";
	}

	@Override
	public boolean equals(Object obj) {
		return equals(this, obj);
	}

	/**
	 * All {@link Circle} implementations should use this definition of
	 * {@link Object#equals(Object)}.
	 */
	public static boolean equals(Circle thiz, Object o) {
		assert thiz != null;
		if (thiz == o)
			return true;
		if (!(o instanceof Circle))
			return false;

		Circle circle = (Circle) o;

		if (!thiz.getCenter().equals(circle.getCenter()))
			return false;
		if (Double.compare(circle.getRadius(), thiz.getRadius()) != 0)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return hashCode(this);
	}

	/**
	 * All {@link Circle} implementations should use this definition of
	 * {@link Object#hashCode()}.
	 */
	public static int hashCode(Circle thiz) {
		int result;
		long temp;
		result = thiz.getCenter().hashCode();
		temp = thiz.getRadius() != +0.0d ? Double.doubleToLongBits(thiz.getRadius()) : 0L;
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * The right point of the circle
	 * 
	 * @return
	 */
	public Point getMaxX() {
		return ctx.makePoint(getXAxis() + radiusDEG, getYAxis());
	}

	/**
	 * the left point of the circle
	 * 
	 * @return
	 */
	public Point getMinX() {
		return ctx.makePoint(getXAxis() - radiusDEG, getYAxis());
	}

	/**
	 * The bottom point of the circle
	 * 
	 * @return
	 */
	public Point getMinY() {
		return ctx.makePoint(getXAxis(), getYAxis() - radiusDEG);
	}

	/**
	 * The above point of the circle
	 * 
	 * @return
	 */
	public Point getMaxY() {
		return ctx.makePoint(getXAxis(), getYAxis() + radiusDEG);
	}

}
