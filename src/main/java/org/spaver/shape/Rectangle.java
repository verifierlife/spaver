package org.spaver.shape;

import org.spaver.context.SpatialContext;
import org.spaver.distance.DistanceUtils;

public class Rectangle extends BaseShape<SpatialContext> implements Shape {

	private double minX;
	private double maxX;
	private double minY;
	private double maxY;

	/**
	 * A simple constructor without normalization / validation. Constructs a
	 * rectangle
	 */
	public Rectangle(double minX, double maxX, double minY, double maxY, SpatialContext ctx) {
		super(ctx);
		// TODO change to West South East North to be more consistent with OGC?
		reset(minX, maxX, minY, maxY);
	}

	/** A convenience constructor which pulls out the coordinates. */
	public Rectangle(Point lowerLeft, Point upperRight, SpatialContext ctx) {
		this(lowerLeft.getX(), upperRight.getX(), lowerLeft.getY(), upperRight.getY(), ctx);
	}

	/** Copy constructor. */
	public Rectangle(Rectangle rectangle, SpatialContext ctx) {
		this(rectangle.getMinX(), rectangle.getMaxX(), rectangle.getMinY(), rectangle.getMaxY(), ctx);
	}




	/**
	 * Expert: Resets the state of this shape given the arguments. This is a
	 * performance feature to avoid excessive Shape object allocation as well as
	 * some argument error checking. Mutable shapes is error-prone so use with care.
	 */
	public void reset(double minX, double maxX, double minY, double maxY) {
		assert !isEmpty();
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		assert minY <= maxY || Double.isNaN(minY) : "minY, maxY: " + minY + ", " + maxY;
	}

	public boolean isEmpty() {
		return Double.isNaN(minX);
	}

	public Rectangle getBuffered(double distance, SpatialContext ctx) {
		Rectangle worldBounds = ctx.getWorldBounds();
		double newMinX = Math.max(worldBounds.getMinX(), minX - distance);
		double newMaxX = Math.min(worldBounds.getMaxX(), maxX + distance);
		double newMinY = Math.max(worldBounds.getMinY(), minY - distance);
		double newMaxY = Math.min(worldBounds.getMaxY(), maxY + distance);
		return ctx.makeRectangle(newMinX, newMaxX, newMinY, newMaxY);
	}

	public boolean hasArea() {
		return maxX != minX && maxY != minY;
	}

	public double getArea(SpatialContext ctx) {
		if (ctx == null) {
			return getWidth() * getHeight();
		} else {
			return ctx.getDistCalc().area(this);
		}
	}
	/** Only meaningful for geospatial contexts. */
	public boolean getCrossesDateLine() {
		return (minX > maxX);
	}

	/**
	 * The height. In geospatial contexts, this is in degrees latitude. It will
	 * always be &gt;= 0.
	 */
	public double getHeight() {
		return maxY - minY;
	}

	/**
	 * The width. In geospatial contexts, this is generally in degrees longitude and
	 * is aware of the dateline (aka anti-meridian). It will always be &gt;= 0.
	 */
	public double getWidth() {
		double w = maxX - minX;
		if (w < 0) {// only true when minX > maxX (WGS84 assumed)
			w += 360;
			assert w >= 0;
		}
		return w;
	}

	/**
	 * The right edge of the X coordinate.
	 * @return
	 */
	public double getMaxX() {
		return maxX;
	}
	
	/**
	 * The top edge of the Y coordinate.
	 * @return
	 */
	public double getMaxY() {
		return maxY;
	}
	
	/**
	 * The left edge of the X coordinate.
	 * @return
	 */
	public double getMinX() {
		return minX;
	}
	
	/**
	 * The bottom edge of the Y coordinate.
	 * @return
	 */
	public double getMinY() {
		return minY;
	}

	public Rectangle getBoundingBox() {
		return this;
	}

	public SpatialRelation relate(Shape other) {
		if (isEmpty() || other.isEmpty())
			return SpatialRelation.DISCONNECT;
		if (other instanceof Point) {
			return relate((Point) other);
		}
		if (other instanceof Rectangle) {
			return relate((Rectangle) other);
		}
		return other.relate(this).transpose();
	}

	public SpatialRelation relate(Point point) {
		if (point.getY() > getMaxY() || point.getY() < getMinY())
			return SpatialRelation.DISCONNECT;
		// all the below logic is rather unfortunate but some dateline cases demand it
		double minX = this.minX;
		double maxX = this.maxX;
		double pX = point.getX();
//		if (ctx.isGeo()) {
//			// unwrap dateline and normalize +180 to become -180
//			double rawWidth = maxX - minX;
//			if (rawWidth < 0) {
//				maxX = minX + (rawWidth + 360);
//			}
//			// shift to potentially overlap
//			if (pX < minX) {
//				pX += 360;
//			} else if (pX > maxX) {
//				pX -= 360;
//			} else {
//				return SpatialRelation.CONTAINS;// short-circuit
//			}
//		}
		if (pX < minX || pX > maxX)
			return SpatialRelation.DISCONNECT;
		return SpatialRelation.NONTANGENCY;
	}

	/**
	 * The spatial relation between two rectangles
	 * 
	 * @param rect
	 * @return
	 */
	public SpatialRelation relate(Rectangle rect) {
		SpatialRelation yIntersect = relateYRange(rect.getMinY(), rect.getMaxY());
		if (yIntersect == SpatialRelation.DISCONNECT)
			return SpatialRelation.DISCONNECT;

		SpatialRelation xIntersect = relateXRange(rect.getMinX(), rect.getMaxX());
		if (xIntersect == SpatialRelation.DISCONNECT)
			return SpatialRelation.DISCONNECT;

		if (xIntersect == yIntersect)// in agreement
			return xIntersect;

		// if one side is equal, return the other
		if (getMinY() == rect.getMinY() && getMaxY() == rect.getMaxY())
			return xIntersect;
		if (getMinX() == rect.getMinX() && getMaxX() == rect.getMaxX() || verticalAtDateline(this, rect)) {
			return yIntersect;
		}
		return SpatialRelation.PARTIALOVERLAP;
	}

	// note: if vertical lines at the date line were normalized (say to -180.0) then
	// this method wouldn't be necessary.
	private static boolean verticalAtDateline(Rectangle rectangle, Rectangle rect2) {
		if (rectangle.getMinX() == rectangle.getMaxX() && rect2.getMinX() == rect2.getMaxX()) {
			if (rectangle.getMinX() == -180) {
				return rect2.getMinX() == +180;
			} else if (rectangle.getMinX() == +180) {
				return rect2.getMinX() == -180;
			}
		}
		return false;
	}

	// TODO might this utility move to SpatialRelation ?
	private static SpatialRelation relate_range(double int_min, double int_max, double ext_min, double ext_max) {
		if (ext_min > int_max || ext_max < int_min) {
			return SpatialRelation.DISCONNECT;
		} else if (ext_min == int_max || ext_max == int_min) {
			return SpatialRelation.EXTERCONNECT;
		} else if (ext_min > int_min && ext_max < int_max) {
			return SpatialRelation.PARTIALOVERLAP;
		} else if (ext_min == int_min) {
			if (ext_max == int_max) {
				return SpatialRelation.EQUAL;
			} else if (ext_max < int_max) {
				return SpatialRelation.TANGENCY;
			} else if (ext_max > int_max) {
				return SpatialRelation.TANGENCYINVERSE;
			}
		} else if (ext_max == int_max) {
			if (ext_min > int_min) {
				return SpatialRelation.TANGENCY;
			} else if (ext_min < int_min) {
				return SpatialRelation.TANGENCYINVERSE;
			}
		} else if (ext_min < int_min && ext_max > int_max) {
			return SpatialRelation.NONTANGENCY;
		}
		return SpatialRelation.NONTANGENCYINVERSE;
	}

	/**
	 * A specialization of {@link Shape#relate(Shape)} for a vertical line.
	 */
	public SpatialRelation relateYRange(double ext_minY, double ext_maxY) {
		return relate_range(minY, maxY, ext_minY, ext_maxY);
	}

	/**
	 * A specialization of {@link Shape#relate(Shape)} for a horizontal line.
	 */
	public SpatialRelation relateXRange(double ext_minX, double ext_maxX) {
		// For ext & this we have local minX and maxX variable pairs. We rotate them so
		// that minX <= maxX
		double minX = this.minX;
		double maxX = this.maxX;
//		if (ctx.isGeo()) {
//			// unwrap dateline, plus do world-wrap short circuit
//			double rawWidth = maxX - minX;
//			if (rawWidth == 360)
//				return SpatialRelation.CONTAINS;
//			if (rawWidth < 0) {
//				maxX = minX + (rawWidth + 360);
//			}
//			double ext_rawWidth = ext_maxX - ext_minX;
//			if (ext_rawWidth == 360)
//				return SpatialRelation.WITHIN;
//			if (ext_rawWidth < 0) {
//				ext_maxX = ext_minX + (ext_rawWidth + 360);
//			}
//			// shift to potentially overlap
//			if (maxX < ext_minX) {
//				minX += 360;
//				maxX += 360;
//			} else if (ext_maxX < minX) {
//				ext_minX += 360;
//				ext_maxX += 360;
//			}
//		}
		return relate_range(minX, maxX, ext_minX, ext_maxX);
	}

	@Override
	public String toString() {
		return "Rect(minX=" + minX + ",maxX=" + maxX + ",minY=" + minY + ",maxY=" + maxY + ")";
	}

	public Point getCenter() {
		if (Double.isNaN(minX))
			return ctx.makePoint(Double.NaN, Double.NaN);
		final double y = getHeight() / 2 + minY;
		double x = getWidth() / 2 + minX;
		if (minX > maxX)// WGS84
			x = DistanceUtils.normLonDEG(x);// in case falls outside the standard range
		return new Point(x, y, ctx);
	}

	public boolean equals(Object obj) {
		return equals(this, obj);
	}

	/**
	 * All {@link Rectangle} implementations should use this definition of
	 * {@link Object#equals(Object)}.
	 */
	public static boolean equals(Rectangle thiz, Object o) {
		assert thiz != null;
		if (thiz == o)
			return true;
		if (!(o instanceof Rectangle))
			return false;

		Rectangle rectangle = (Rectangle) o;

		if (Double.compare(rectangle.getMaxX(), thiz.getMaxX()) != 0)
			return false;
		if (Double.compare(rectangle.getMaxY(), thiz.getMaxY()) != 0)
			return false;
		if (Double.compare(rectangle.getMinX(), thiz.getMinX()) != 0)
			return false;
		if (Double.compare(rectangle.getMinY(), thiz.getMinY()) != 0)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return hashCode(this);
	}

	/**
	 * All {@link Rectangle} implementations should use this definition of
	 * {@link Object#hashCode()}.
	 */
	public static int hashCode(Rectangle thiz) {
		int result;
		long temp;
		temp = thiz.getMinX() != +0.0d ? Double.doubleToLongBits(thiz.getMinX()) : 0L;
		result = (int) (temp ^ (temp >>> 32));
		temp = thiz.getMaxX() != +0.0d ? Double.doubleToLongBits(thiz.getMaxX()) : 0L;
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = thiz.getMinY() != +0.0d ? Double.doubleToLongBits(thiz.getMinY()) : 0L;
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = thiz.getMaxY() != +0.0d ? Double.doubleToLongBits(thiz.getMaxY()) : 0L;
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * return the left and upper point
	 * @return
	 */
	public Point getLeftUpper() {
		return ctx.makePoint(minX, maxY);
	}

	/**
	 *  return the left and lower point
	 * @return
	 */
	public Point getLeftLower() {
		return ctx.makePoint(minX, minY);
	}

	/**
	 *  return the right and upper point
	 * @return
	 */
	public Point getRightUpper() {
		return ctx.makePoint(maxX, maxY);
	}
	
	/**
	 * return the right and lower point
	 * 
	 * @return
	 */
	public Point getRightLower() {
		return ctx.makePoint(maxX, minY);
	}

}
