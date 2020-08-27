package org.spaver.shape;

import org.spaver.context.SpatialContext;
import org.spaver.context.SpatialContextFactory;

import com.sun.javafx.geom.Line2D;
//import com.sun.javafx.geom.LineIterator;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;

public class Line extends BaseShape<SpatialContext> implements Shape {

	/**
	 * The X coordinate of the start point of the line segment.
	 */
	public double x1;

	/**
	 * The Y coordinate of the start point of the line segment.
	 */
	public double y1;

	/**
	 * The X coordinate of the end point of the line segment.
	 */
	public double x2;

	/**
	 * The Y coordinate of the end point of the line segment.
	 */
	public double y2;
	Point point1;
	Point point2;
	SpatialContext spatialContext;

	public Line(SpatialContext ctx) {
		super(ctx);
		spatialContext = new SpatialContext(new SpatialContextFactory());
	}

	/**
	 * Constructs and initializes a line from the specified point objects.
	 * 
	 * @param ctx
	 * @param point1: the start point of this line segment
	 * @param point2: the end point of this line segment
	 */
	public Line(SpatialContext ctx, Point point1, Point point2) {
		super(ctx);
		this.point1 = point1;
		this.point2 = point2;
		setLine(point1, point2);
	}

	@Override
	public SpatialRelation relate(Shape other) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Constructs and initializes a Line from the specified coordinates.
	 * 
	 * @param x1 the X coordinate of the start point
	 * @param y1 the Y coordinate of the start point
	 * @param x2 the X coordinate of the end point
	 * @param y2 the Y coordinate of the end point
	 */
	public Line(SpatialContext spatialContext, double x1, double y1, double x2, double y2) {
		super(spatialContext);
		setLine(x1, y1, x2, y2);
	}

	/**
	 * Constructs and initializes a line from the specified point objects.
	 * 
	 * @param p1 the start <code>Point2D</code> of this line segment
	 * @param p2 the end <code>Point2D</code> of this line segment
	 */

	/**
	 * Sets the location of the end points of this <code>Line2D</code> to the
	 * specified double coordinates.
	 * 
	 * @param x1 the X coordinate of the start point
	 * @param y1 the Y coordinate of the start point
	 * @param x2 the X coordinate of the end point
	 * @param y2 the Y coordinate of the end point
	 */
	public void setLine(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	/**
	 * Sets the location of the end points of this <code>Line2D</code> to the
	 * specified <code>Point2D</code> coordinates.
	 * 
	 * @param p1 the start <code>Point2D</code> of the line segment
	 * @param p2 the end <code>Point2D</code> of the line segment
	 */
	public void setLine(Point p1, Point p2) {
		setLine(p1.x, p1.y, p2.x, p2.y);
	}

	/**
	 * Sets the location of the end points of this <code>Line2D</code> to the same
	 * as those end points of the specified <code>Line2D</code>.
	 * 
	 * @param l the specified <code>Line2D</code>
	 */
	public void setLine(Line2D l) {
		setLine(l.x1, l.y1, l.x2, l.y2);
	}

	@Override
	public Rectangle getBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasArea() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getArea(SpatialContext ctx) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Point getCenter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SpatialContext getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Constructs and initializes a Line with coordinates (0, 0) -> (0, 0).
	 */

	/**
	 * {@inheritDoc}
	 */
	public Rectangle getBounds() {
		Rectangle b = new Rectangle(x1, x2, y1, y2, spatialContext);
		b.setBoundsAndSort(x1, y1, x2, y2);
		return b;
	}

	/**
	 * @inheritDoc
	 */
	public boolean contains(double x, double y) {
		return false;
	}

	/**
	 * @inheritDoc
	 */
	public boolean contains(double x, double y, double w, double h) {
		return false;
	}

	/**
	 * @inheritDoc
	 */
	public boolean contains(Point p) {
		return false;
	}

	/**
	 * @inheritDoc
	 */
	public boolean intersects(double x, double y, double w, double h) {
		int out1, out2;
		if ((out2 = outcode(x, y, w, h, x2, y2)) == 0) {
			return true;
		}
		double px = x1;
		double py = y1;
		while ((out1 = outcode(x, y, w, h, px, py)) != 0) {
			if ((out1 & out2) != 0) {
				return false;
			}
			if ((out1 & (OUT_LEFT | OUT_RIGHT)) != 0) {
				px = x;
				if ((out1 & OUT_RIGHT) != 0) {
					px += w;
				}
				py = y1 + (px - x1) * (y2 - y1) / (x2 - x1);
			} else {
				py = y;
				if ((out1 & OUT_BOTTOM) != 0) {
					py += h;
				}
				px = x1 + (py - y1) * (x2 - x1) / (y2 - y1);
			}
		}
		return true;
	}
	
	
	public boolean intersects(Rectangle rect) {
		double x = rect.getMinX(), y = rect.getMinY(), w = rect.getMaxX() - x, h = rect.getMaxY() - y;
		int out1, out2;
		if ((out2 = outcode(x, y, w, h, x2, y2)) == 0) {
			return true;
		}
		double px = x1;
		double py = y1;
		while ((out1 = outcode(x, y, w, h, px, py)) != 0) {
			if ((out1 & out2) != 0) {
				return false;
			}
			if ((out1 & (OUT_LEFT | OUT_RIGHT)) != 0) {
				px = x;
				if ((out1 & OUT_RIGHT) != 0) {
					px += w;
				}
				py = y1 + (px - x1) * (y2 - y1) / (x2 - x1);
			} else {
				py = y;
				if ((out1 & OUT_BOTTOM) != 0) {
					py += h;
				}
				px = x1 + (py - y1) * (x2 - x1) / (y2 - y1);
			}
		}
		return true;
	}
	
	
    public int outcode(double rx, double ry, double rwidth, double rheight, double x, double y) {
        /*
         * Note on casts to double below.  If the arithmetic of
         * x+w or y+h is done in int, then we may get integer
         * overflow. By converting to double before the addition
         * we force the addition to be carried out in double to
         * avoid overflow in the comparison.
         *
         * See bug 4320890 for problems that this can cause.
         */
        int out = 0;
        if (rwidth <= 0) {
            out |= OUT_LEFT | OUT_RIGHT;
        } else if (x < rx) {
            out |= OUT_LEFT;
        } else if (x > rx + (double) rwidth) {
            out |= OUT_RIGHT;
        }
        if (rheight <= 0) {
            out |= OUT_TOP | OUT_BOTTOM;
        } else if (y < ry) {
            out |= OUT_TOP;
        } else if (y > ry + (double) rheight) {
            out |= OUT_BOTTOM;
        }
        return out;
    }

    /**
     * The bitmask that indicates that a point lies to the left of
     * this <code>Rectangle2D</code>.
     */
    public static final int OUT_LEFT = 1;

    /**
     * The bitmask that indicates that a point lies above
     * this <code>Rectangle2D</code>.
     */
    public static final int OUT_TOP = 2;

    /**
     * The bitmask that indicates that a point lies to the right of
     * this <code>Rectangle2D</code>.
     */
    public static final int OUT_RIGHT = 4;

    /**
     * The bitmask that indicates that a point lies below
     * this <code>Rectangle2D</code>.
     */
    public static final int OUT_BOTTOM = 8;


	/**
	 * Returns an indicator of where the specified point {@code (px,py)} lies with
	 * respect to the line segment from {@code (x1,y1)} to {@code (x2,y2)}. The
	 * return value can be either 1, -1, or 0 and indicates in which direction the
	 * specified line must pivot around its first end point, {@code (x1,y1)}, in
	 * order to point at the specified point {@code (px,py)}.
	 * <p>
	 * A return value of 1 indicates that the line segment must turn in the
	 * direction that takes the positive X axis towards the negative Y axis. In the
	 * default coordinate system used by Java 2D, this direction is
	 * counterclockwise.
	 * <p>
	 * A return value of -1 indicates that the line segment must turn in the
	 * direction that takes the positive X axis towards the positive Y axis. In the
	 * default coordinate system, this direction is clockwise.
	 * <p>
	 * A return value of 0 indicates that the point lies exactly on the line
	 * segment. Note that an indicator value of 0 is rare and not useful for
	 * determining colinearity because of doubleing point rounding issues.
	 * <p>
	 * If the point is colinear with the line segment, but not between the end
	 * points, then the value will be -1 if the point lies "beyond {@code (x1,y1)}"
	 * or 1 if the point lies "beyond {@code (x2,y2)}".
	 *
	 * @param x1 the X coordinate of the start point of the specified line segment
	 * @param y1 the Y coordinate of the start point of the specified line segment
	 * @param x2 the X coordinate of the end point of the specified line segment
	 * @param y2 the Y coordinate of the end point of the specified line segment
	 * @param px the X coordinate of the specified point to be compared with the
	 *           specified line segment
	 * @param py the Y coordinate of the specified point to be compared with the
	 *           specified line segment
	 * @return an integer that indicates the position of the third specified
	 *         coordinates with respect to the line segment formed by the first two
	 *         specified coordinates.
	 */
	public static int relativeCCW(double x1, double y1, double x2, double y2, double px, double py) {
		x2 -= x1;
		y2 -= y1;
		px -= x1;
		py -= y1;
		double ccw = px * y2 - py * x2;
		if (ccw == 0.0f) {
			// The point is colinear, classify based on which side of
			// the segment the point falls on. We can calculate a
			// relative value using the projection of px,py onto the
			// segment - a negative value indicates the point projects
			// outside of the segment in the direction of the particular
			// endpoint used as the origin for the projection.
			ccw = px * x2 + py * y2;
			if (ccw > 0.0f) {
				// Reverse the projection to be relative to the original x2,y2
				// x2 and y2 are simply negated.
				// px and py need to have (x2 - x1) or (y2 - y1) subtracted
				// from them (based on the original values)
				// Since we really want to get a positive answer when the
				// point is "beyond (x2,y2)", then we want to calculate
				// the inverse anyway - thus we leave x2 & y2 negated.
				px -= x2;
				py -= y2;
				ccw = px * x2 + py * y2;
				if (ccw < 0.0f) {
					ccw = 0.0f;
				}
			}
		}
		return (ccw < 0.0f) ? -1 : ((ccw > 0.0f) ? 1 : 0);
	}

	/**
	 * Returns an indicator of where the specified point {@code (px,py)} lies with
	 * respect to this line segment. See the method comments of
	 * {@link #relativeCCW(double, double, double, double, double, double)} to
	 * interpret the return value.
	 * 
	 * @param px the X coordinate of the specified point to be compared with this
	 *           <code>Line2D</code>
	 * @param py the Y coordinate of the specified point to be compared with this
	 *           <code>Line2D</code>
	 * @return an integer that indicates the position of the specified coordinates
	 *         with respect to this <code>Line2D</code>
	 * @see #relativeCCW(double, double, double, double, double, double)
	 */
	public int relativeCCW(double px, double py) {
		return relativeCCW(x1, y1, x2, y2, px, py);
	}

	/**
	 * Returns an indicator of where the specified <code>Point2D</code> lies with
	 * respect to this line segment. See the method comments of
	 * {@link #relativeCCW(double, double, double, double, double, double)} to
	 * interpret the return value.
	 * 
	 * @param p the specified <code>Point2D</code> to be compared with this
	 *          <code>Line2D</code>
	 * @return an integer that indicates the position of the specified
	 *         <code>Point2D</code> with respect to this <code>Line2D</code>
	 * @see #relativeCCW(double, double, double, double, double, double)
	 */
	public int relativeCCW(Point p) {
		return relativeCCW(x1, y1, x2, y2, p.x, p.y);
	}

	/**
	 * Tests if the line segment from {@code (x1,y1)} to {@code (x2,y2)} intersects
	 * the line segment from {@code (x3,y3)} to {@code (x4,y4)}.
	 *
	 * @param x1 the X coordinate of the start point of the first specified line
	 *           segment
	 * @param y1 the Y coordinate of the start point of the first specified line
	 *           segment
	 * @param x2 the X coordinate of the end point of the first specified line
	 *           segment
	 * @param y2 the Y coordinate of the end point of the first specified line
	 *           segment
	 * @param x3 the X coordinate of the start point of the second specified line
	 *           segment
	 * @param y3 the Y coordinate of the start point of the second specified line
	 *           segment
	 * @param x4 the X coordinate of the end point of the second specified line
	 *           segment
	 * @param y4 the Y coordinate of the end point of the second specified line
	 *           segment
	 * @return <code>true</code> if the first specified line segment and the second
	 *         specified line segment intersect each other; <code>false</code>
	 *         otherwise.
	 */
	public static boolean linesIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4,
			double y4) {
		return ((relativeCCW(x1, y1, x2, y2, x3, y3) * relativeCCW(x1, y1, x2, y2, x4, y4) <= 0)
				&& (relativeCCW(x3, y3, x4, y4, x1, y1) * relativeCCW(x3, y3, x4, y4, x2, y2) <= 0));
	}

	/**
	 * Tests if the line segment from {@code (x1,y1)} to {@code (x2,y2)} intersects
	 * this line segment.
	 *
	 * @param x1 the X coordinate of the start point of the specified line segment
	 * @param y1 the Y coordinate of the start point of the specified line segment
	 * @param x2 the X coordinate of the end point of the specified line segment
	 * @param y2 the Y coordinate of the end point of the specified line segment
	 * @return <true> if this line segment and the specified line segment intersect
	 *         each other; <code>false</code> otherwise.
	 */
	public boolean intersectsLine(double x1, double y1, double x2, double y2) {
		return linesIntersect(x1, y1, x2, y2, this.x1, this.y1, this.x2, this.y2);
	}

	/**
	 * Tests if the specified line segment intersects this line segment.
	 * 
	 * @param l the specified <code>Line2D</code>
	 * @return <code>true</code> if this line segment and the specified line segment
	 *         intersect each other; <code>false</code> otherwise.
	 */
	public boolean intersectsLine(Line l) {
		return linesIntersect(l.x1, l.y1, l.x2, l.y2, this.x1, this.y1, this.x2, this.y2);
	}

	/**
	 * Returns the square of the distance from a point to a line segment. The
	 * distance measured is the distance between the specified point and the closest
	 * point between the specified end points. If the specified point intersects the
	 * line segment in between the end points, this method returns 0.0.
	 *
	 * @param x1 the X coordinate of the start point of the specified line segment
	 * @param y1 the Y coordinate of the start point of the specified line segment
	 * @param x2 the X coordinate of the end point of the specified line segment
	 * @param y2 the Y coordinate of the end point of the specified line segment
	 * @param px the X coordinate of the specified point being measured against the
	 *           specified line segment
	 * @param py the Y coordinate of the specified point being measured against the
	 *           specified line segment
	 * @return a double value that is the square of the distance from the specified
	 *         point to the specified line segment.
	 * @see #ptLineDistSq(double, double, double, double, double, double)
	 */
	public static double ptSegDistSq(double x1, double y1, double x2, double y2, double px, double py) {
		// Adjust vectors relative to x1,y1
		// x2,y2 becomes relative vector from x1,y1 to end of segment
		x2 -= x1;
		y2 -= y1;
		// px,py becomes relative vector from x1,y1 to test point
		px -= x1;
		py -= y1;
		double dotprod = px * x2 + py * y2;
		double projlenSq;
		if (dotprod <= 0f) {
			// px,py is on the side of x1,y1 away from x2,y2
			// distance to segment is length of px,py vector
			// "length of its (clipped) projection" is now 0.0
			projlenSq = 0f;
		} else {
			// switch to backwards vectors relative to x2,y2
			// x2,y2 are already the negative of x1,y1=>x2,y2
			// to get px,py to be the negative of px,py=>x2,y2
			// the dot product of two negated vectors is the same
			// as the dot product of the two normal vectors
			px = x2 - px;
			py = y2 - py;
			dotprod = px * x2 + py * y2;
			if (dotprod <= 0f) {
				// px,py is on the side of x2,y2 away from x1,y1
				// distance to segment is length of (backwards) px,py vector
				// "length of its (clipped) projection" is now 0.0
				projlenSq = 0f;
			} else {
				// px,py is between x1,y1 and x2,y2
				// dotprod is the length of the px,py vector
				// projected on the x2,y2=>x1,y1 vector times the
				// length of the x2,y2=>x1,y1 vector
				projlenSq = dotprod * dotprod / (x2 * x2 + y2 * y2);
			}
		}
		// Distance to line is now the length of the relative point
		// vector minus the length of its projection onto the line
		// (which is zero if the projection falls outside the range
		// of the line segment).
		double lenSq = px * px + py * py - projlenSq;
		if (lenSq < 0f) {
			lenSq = 0f;
		}
		return lenSq;
	}

	/**
	 * Returns the distance from a point to a line segment. The distance measured is
	 * the distance between the specified point and the closest point between the
	 * specified end points. If the specified point intersects the line segment in
	 * between the end points, this method returns 0.0.
	 *
	 * @param x1 the X coordinate of the start point of the specified line segment
	 * @param y1 the Y coordinate of the start point of the specified line segment
	 * @param x2 the X coordinate of the end point of the specified line segment
	 * @param y2 the Y coordinate of the end point of the specified line segment
	 * @param px the X coordinate of the specified point being measured against the
	 *           specified line segment
	 * @param py the Y coordinate of the specified point being measured against the
	 *           specified line segment
	 * @return a double value that is the distance from the specified point to the
	 *         specified line segment.
	 * @see #ptLineDist(double, double, double, double, double, double)
	 */
	public static double ptSegDist(double x1, double y1, double x2, double y2, double px, double py) {
		return (double) Math.sqrt(ptSegDistSq(x1, y1, x2, y2, px, py));
	}

	/**
	 * Returns the square of the distance from a point to this line segment. The
	 * distance measured is the distance between the specified point and the closest
	 * point between the current line's end points. If the specified point
	 * intersects the line segment in between the end points, this method returns
	 * 0.0.
	 *
	 * @param px the X coordinate of the specified point being measured against this
	 *           line segment
	 * @param py the Y coordinate of the specified point being measured against this
	 *           line segment
	 * @return a double value that is the square of the distance from the specified
	 *         point to the current line segment.
	 * @see #ptLineDistSq(double, double)
	 */
	public double ptSegDistSq(double px, double py) {
		return ptSegDistSq(x1, y1, x2, y2, px, py);
	}

	/**
	 * Returns the square of the distance from a <code>Point2D</code> to this line
	 * segment. The distance measured is the distance between the specified point
	 * and the closest point between the current line's end points. If the specified
	 * point intersects the line segment in between the end points, this method
	 * returns 0.0.
	 * 
	 * @param pt the specified <code>Point2D</code> being measured against this line
	 *           segment.
	 * @return a double value that is the square of the distance from the specified
	 *         <code>Point2D</code> to the current line segment.
	 * @see #ptLineDistSq(Point2D)
	 */
	public double ptSegDistSq(Point pt) {
		return ptSegDistSq(x1, y1, x2, y2, pt.x, pt.y);
	}

	/**
	 * Returns the distance from a point to this line segment. The distance measured
	 * is the distance between the specified point and the closest point between the
	 * current line's end points. If the specified point intersects the line segment
	 * in between the end points, this method returns 0.0.
	 *
	 * @param px the X coordinate of the specified point being measured against this
	 *           line segment
	 * @param py the Y coordinate of the specified point being measured against this
	 *           line segment
	 * @return a double value that is the distance from the specified point to the
	 *         current line segment.
	 * @see #ptLineDist(double, double)
	 */
	public double ptSegDist(double px, double py) {
		return ptSegDist(x1, y1, x2, y2, px, py);
	}

	/**
	 * Returns the distance from a <code>Point2D</code> to this line segment. The
	 * distance measured is the distance between the specified point and the closest
	 * point between the current line's end points. If the specified point
	 * intersects the line segment in between the end points, this method returns
	 * 0.0.
	 * 
	 * @param pt the specified <code>Point2D</code> being measured against this line
	 *           segment
	 * @return a double value that is the distance from the specified
	 *         <code>Point2D</code> to the current line segment.
	 * @see #ptLineDist(Point2D)
	 */
	public double ptSegDist(Point pt) {
		return ptSegDist(x1, y1, x2, y2, pt.x, pt.y);
	}

	/**
	 * Returns the square of the distance from a point to a line. The distance
	 * measured is the distance between the specified point and the closest point on
	 * the infinitely-extended line defined by the specified coordinates. If the
	 * specified point intersects the line, this method returns 0.0.
	 *
	 * @param x1 the X coordinate of the start point of the specified line
	 * @param y1 the Y coordinate of the start point of the specified line
	 * @param x2 the X coordinate of the end point of the specified line
	 * @param y2 the Y coordinate of the end point of the specified line
	 * @param px the X coordinate of the specified point being measured against the
	 *           specified line
	 * @param py the Y coordinate of the specified point being measured against the
	 *           specified line
	 * @return a double value that is the square of the distance from the specified
	 *         point to the specified line.
	 * @see #ptSegDistSq(double, double, double, double, double, double)
	 */
	public static double ptLineDistSq(double x1, double y1, double x2, double y2, double px, double py) {
		// Adjust vectors relative to x1,y1
		// x2,y2 becomes relative vector from x1,y1 to end of segment
		x2 -= x1;
		y2 -= y1;
		// px,py becomes relative vector from x1,y1 to test point
		px -= x1;
		py -= y1;
		double dotprod = px * x2 + py * y2;
		// dotprod is the length of the px,py vector
		// projected on the x1,y1=>x2,y2 vector times the
		// length of the x1,y1=>x2,y2 vector
		double projlenSq = dotprod * dotprod / (x2 * x2 + y2 * y2);
		// Distance to line is now the length of the relative point
		// vector minus the length of its projection onto the line
		double lenSq = px * px + py * py - projlenSq;
		if (lenSq < 0f) {
			lenSq = 0f;
		}
		return lenSq;
	}

	/**
	 * Returns the distance from a point to a line. The distance measured is the
	 * distance between the specified point and the closest point on the
	 * infinitely-extended line defined by the specified coordinates. If the
	 * specified point intersects the line, this method returns 0.0.
	 *
	 * @param x1 the X coordinate of the start point of the specified line
	 * @param y1 the Y coordinate of the start point of the specified line
	 * @param x2 the X coordinate of the end point of the specified line
	 * @param y2 the Y coordinate of the end point of the specified line
	 * @param px the X coordinate of the specified point being measured against the
	 *           specified line
	 * @param py the Y coordinate of the specified point being measured against the
	 *           specified line
	 * @return a double value that is the distance from the specified point to the
	 *         specified line.
	 * @see #ptSegDist(double, double, double, double, double, double)
	 */
	public static double ptLineDist(double x1, double y1, double x2, double y2, double px, double py) {
		return (double) Math.sqrt(ptLineDistSq(x1, y1, x2, y2, px, py));
	}

	/**
	 * Returns the square of the distance from a point to this line. The distance
	 * measured is the distance between the specified point and the closest point on
	 * the infinitely-extended line defined by this <code>Line2D</code>. If the
	 * specified point intersects the line, this method returns 0.0.
	 *
	 * @param px the X coordinate of the specified point being measured against this
	 *           line
	 * @param py the Y coordinate of the specified point being measured against this
	 *           line
	 * @return a double value that is the square of the distance from a specified
	 *         point to the current line.
	 * @see #ptSegDistSq(double, double)
	 */
	public double ptLineDistSq(double px, double py) {
		return ptLineDistSq(x1, y1, x2, y2, px, py);
	}

	/**
	 * Returns the square of the distance from a specified <code>Point2D</code> to
	 * this line. The distance measured is the distance between the specified point
	 * and the closest point on the infinitely-extended line defined by this
	 * <code>Line2D</code>. If the specified point intersects the line, this method
	 * returns 0.0.
	 * 
	 * @param pt the specified <code>Point2D</code> being measured against this line
	 * @return a double value that is the square of the distance from a specified
	 *         <code>Point2D</code> to the current line.
	 * @see #ptSegDistSq(Point2D)
	 */
	public double ptLineDistSq(Point pt) {
		return ptLineDistSq(x1, y1, x2, y2, pt.x, pt.y);
	}

	/**
	 * Returns the distance from a point to this line. The distance measured is the
	 * distance between the specified point and the closest point on the
	 * infinitely-extended line defined by this <code>Line2D</code>. If the
	 * specified point intersects the line, this method returns 0.0.
	 *
	 * @param px the X coordinate of the specified point being measured against this
	 *           line
	 * @param py the Y coordinate of the specified point being measured against this
	 *           line
	 * @return a double value that is the distance from a specified point to the
	 *         current line.
	 * @see #ptSegDist(double, double)
	 */
	public double ptLineDist(double px, double py) {
		return ptLineDist(x1, y1, x2, y2, px, py);
	}

	/**
	 * Returns the distance from a <code>Point2D</code> to this line. The distance
	 * measured is the distance between the specified point and the closest point on
	 * the infinitely-extended line defined by this <code>Line2D</code>. If the
	 * specified point intersects the line, this method returns 0.0.
	 * 
	 * @param pt the specified <code>Point2D</code> being measured
	 * @return a double value that is the distance from a specified
	 *         <code>Point2D</code> to the current line.
	 * @see #ptSegDist(Point2D)
	 */
	public double ptLineDist(Point pt) {
		return ptLineDist(x1, y1, x2, y2, pt.x, pt.y);
	}

	/**
	 * Returns an iteration object that defines the boundary of this
	 * <code>Line2D</code>. The iterator for this class is not multi-threaded safe,
	 * which means that this <code>Line2D</code> class does not guarantee that
	 * modifications to the geometry of this <code>Line2D</code> object do not
	 * affect any iterations of that geometry that are already in process.
	 * 
	 * @param tx the specified {@link BaseTransform}
	 * @return a {@link PathIterator} that defines the boundary of this
	 *         <code>Line2D</code>.
	 */
//	public PathIterator getPathIterator(BaseTransform tx) {
//		return new PathIterator(this, tx);
//	}

	public SpatialContext getSpatialContext() {
		return new SpatialContext(new SpatialContextFactory());
	}
	
	/**
	 * Returns an iteration object that defines the boundary of this flattened
	 * <code>Line2D</code>. The iterator for this class is not multi-threaded safe,
	 * which means that this <code>Line2D</code> class does not guarantee that
	 * modifications to the geometry of this <code>Line2D</code> object do not
	 * affect any iterations of that geometry that are already in process.
	 * 
	 * @param tx       the specified <code>BaseTransform</code>
	 * @param flatness the maximum amount that the control points for a given curve
	 *                 can vary from colinear before a subdivided curve is replaced
	 *                 by a straight line connecting the end points. Since a
	 *                 <code>Line2D</code> object is always flat, this parameter is
	 *                 ignored.
	 * @return a <code>PathIterator</code> that defines the boundary of the
	 *         flattened <code>Line2D</code>
	 */
//	public PathIterator getPathIterator(BaseTransform tx, double flatness) {
//		return new PathIterator(this, tx);
//	}

	public Line copy() {
		return new Line(spatialContext,x1, y1, x2, y2);
	}

    public int hashCode() {
    	int result;
		long temp = Double.doubleToLongBits(x1); 
		result = (int) (temp ^ (temp >>> 32));
        temp += Double.doubleToLongBits(y1) * 37;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp += Double.doubleToLongBits(x2) * 43;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp += Double.doubleToLongBits(y2) * 47;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

//    public static int hashCode(Rectangle thiz) {
//		int result;
//		long temp;
//		temp = thiz.getMinX() != +0.0d ? Double.doubleToLongBits(thiz.getMinX()) : 0L;
//		result = (int) (temp ^ (temp >>> 32));
//		temp = thiz.getMaxX() != +0.0d ? Double.doubleToLongBits(thiz.getMaxX()) : 0L;
//		result = 31 * result + (int) (temp ^ (temp >>> 32));
//		temp = thiz.getMinY() != +0.0d ? Double.doubleToLongBits(thiz.getMinY()) : 0L;
//		result = 31 * result + (int) (temp ^ (temp >>> 32));
//		temp = thiz.getMaxY() != +0.0d ? Double.doubleToLongBits(thiz.getMaxY()) : 0L;
//		result = 31 * result + (int) (temp ^ (temp >>> 32));
//		return result;
//	}
//    
    
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Line) {
			Line line = (Line) obj;
			return ((x1 == line.x1) && (y1 == line.y1) && (x2 == line.x2) && (y2 == line.y2));
		}
		return false;
	}

}
