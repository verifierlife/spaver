package org.spaver.shape;

import org.spaver.context.SpatialContext;
import org.spaver.distance.DistanceUtils;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;


public class Rectangle extends BaseShape<SpatialContext> implements Shape {

	private double minX;
	private double maxX;
	private double minY;
	private double maxY;

	SpatialContext spatialContext;

	/**
	 * A simple constructor without normalization / validation. Constructs a
	 * rectangle
	 */
	public Rectangle(double minX, double maxX, double minY, double maxY, SpatialContext spatialContext) {
		super(spatialContext);
		// TODO change to West South East North to be more consistent with OGC?
		reset(minX, maxX, minY, maxY);
		//spatialContext = new SpatialContext(new SpatialContextFactory());
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
     * Set the bounds to match that of the RectBounds object specified. The
     * specified bounds object must not be null.
     */
    public final void setBounds(RectBounds other) {
        minX = other.getMinX();
        minY = other.getMinY();
        maxX = other.getMaxX();
        maxY = other.getMaxY();
    }


    /**
     * Sets the bounds based on the given coords, and also ensures that after
     * having done so that this RectBounds instance is normalized.
     */
    public void setBoundsAndSort(double minX, double minY, double maxX, double maxY) {
        setBounds(minX, minY, maxX, maxY);
        sortMinMax();
    }

    public void setBoundsAndSort(double minX, double minY,  double minZ,
            double maxX, double maxY, double maxZ) {
        if (minZ != 0 || maxZ != 0) {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        setBounds(minX, minY, maxX, maxY);
        sortMinMax();
    }

    public void setBoundsAndSort(Point2D p1, Point2D p2) {
        setBoundsAndSort(p1.x, p1.y, p2.x, p2.y);
    }	

    protected void sortMinMax() {
        if (minX > maxX) {
            double tmp = maxX;
            maxX = minX;
            minX = tmp;
        }
        if (minY > maxY) {
            double tmp = maxY;
            maxY = minY;
            minY = tmp;
        }
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

//	public boolean isEmpty() {
//		return Double.isNaN(minX);
//	}

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
	 * 
	 * @return
	 */
	public double getMaxX() {
		return maxX;
	}

	/**
	 * The top edge of the Y coordinate.
	 * 
	 * @return
	 */
	public double getMaxY() {
		return maxY;
	}

	/**
	 * The left edge of the X coordinate.
	 * 
	 * @return
	 */
	public double getMinX() {
		return minX;
	}

	/**
	 * The bottom edge of the Y coordinate.
	 * 
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
	 * 
	 * @return
	 */
	public Point getLeftUpper() {
		return ctx.makePoint(minX, maxY);
	}

	/**
	 * return the left and lower point
	 * 
	 * @return
	 */
	public Point getLeftLower() {
		return ctx.makePoint(minX, minY);
	}

	/**
	 * return the right and upper point
	 * 
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


    /**
     * Set the bounds to match that of the RectBounds object specified. The
     * specified bounds object must not be null.
     */
    public final void setBounds(Rectangle other) {
        minX = other.getMinX();
        minY = other.getMinY();
        maxX = other.getMaxX();
        maxY = other.getMaxY();
    }

    /**
     * Set the bounds to the given values.
     * @param minX
     * @param minY
     * @param maxX
     * @param maxY
     */
    public final void setBounds(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }
	
	/**
	 * union two rectangles
	 * @param other
	 */
    public void unionWith(Rectangle other) {
        // Short circuit union if either bounds is empty.
        if (other.isEmpty()) return;
        if (this.isEmpty()) {
            setBounds(other);
            return;
        }

        minX = Math.min(minX, other.getMinX());
        minY = Math.min(minY, other.getMinY());
        maxX = Math.max(maxX, other.getMaxX());
        maxY = Math.max(maxY, other.getMaxY());
    }

    public void unionWith(double minX, double minY, double maxX, double maxY) {
        // Short circuit union if either bounds is empty.
        if ((maxX < minX) || (maxY < minY)) return;
        if (this.isEmpty()) {
            setBounds(minX, minY, maxX, maxY);
            return;
        }

        this.minX = Math.min(this.minX, minX);
        this.minY = Math.min(this.minY, minY);
        this.maxX = Math.max(this.maxX, maxX);
        this.maxY = Math.max(this.maxY, maxY);
    }


	/**
	 * Tests if the interior of the <code>Shape</code> entirely contains the
	 * specified <code>RectBounds</code>. The {@code Shape.contains()} method allows
	 * a {@code Shape} implementation to conservatively return {@code false} when:
	 * <ul>
	 * <li>the <code>intersect</code> method returns <code>true</code> and
	 * <li>the calculations to determine whether or not the <code>Shape</code>
	 * entirely contains the <code>RectBounds</code> are prohibitively expensive.
	 * </ul>
	 * This means that for some {@code Shapes} this method might return
	 * {@code false} even though the {@code Shape} contains the {@code RectBounds}.
	 * The {@link com.sun.javafx.geom.Area Area} class performs more accurate
	 * geometric computations than most {@code Shape} objects and therefore can be
	 * used if a more precise answer is required.
	 *
	 * @param r The specified <code>RectBounds</code>
	 * @return <code>true</code> if the interior of the <code>Shape</code> entirely
	 *         contains the <code>RectBounds</code>; <code>false</code> otherwise
	 *         or, if the <code>Shape</code> contains the <code>RectBounds</code>
	 *         and the <code>intersects</code> method returns <code>true</code> and
	 *         the containment calculations would be too expensive to perform.
	 * @see #contains(double, double, double, double)
	 */
	public void intersectWith(Rectangle other) {
		// Short circuit intersect if either bounds is empty.
		if (this.isEmpty())
			return;
		if (other.isEmpty()) {
			makeEmpty();
			return;
		}
		minX = Math.max(minX, other.getMinX());
		minY = Math.max(minY, other.getMinY());
		maxX = Math.min(maxX, other.getMaxX());
		maxY = Math.min(maxY, other.getMaxY());
	}

	/**
	 * intersect with other rectangle
	 * @param other
	 */
	public void intersectWithOther(Rectangle other) {
		double xMin = other.minX;
		double yMin = other.minY;
		double xMax = other.maxX;
		double yMax = other.maxY;

		intersectWith(xMin, yMin, xMax, yMax);
	}

	public void intersectWith(double minX, double minY, double maxX, double maxY) {
		// Short circuit intersect if either bounds is empty.
		if (this.isEmpty())
			return;
		if ((maxX < minX) || (maxY < minY)) {
			makeEmpty();
			return;
		}

		this.minX = Math.max(this.minX, minX);
		this.minY = Math.max(this.minY, minY);
		this.maxX = Math.min(this.maxX, maxX);
		this.maxY = Math.min(this.maxY, maxY);
	}

	public void intersectWith(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		// Short circuit intersect if either bounds is empty.
		if (this.isEmpty())
			return;
		if ((maxX < minX) || (maxY < minY) || (maxZ < minZ)) {
			makeEmpty();
			return;
		}

		this.minX = Math.max(this.minX, minX);
		this.minY = Math.max(this.minY, minY);
		this.maxX = Math.min(this.maxX, maxX);
		this.maxY = Math.min(this.maxY, maxY);
	}

	public boolean contains(Point p) {
		if ((p == null) || isEmpty())
			return false;
		return (p.x >= minX && p.x <= maxX && p.y >= minY && p.y <= maxY);
	}

	public boolean contains(double x, double y) {
		if (isEmpty())
			return false;
		return (x >= minX && x <= maxX && y >= minY && y <= maxY);
	}

	/**
	 * Determines whether the given <code>other</code> RectBounds is completely
	 * contained within this RectBounds. Equivalent RectBounds will return true.
	 *
	 * @param other The other rect bounds to check against.
	 * @return Whether the other rect bounds is contained within this one, which
	 *         also includes equivalence.
	 */
	public boolean contains(Rectangle other) {
		if (isEmpty() || other.isEmpty())
			return false;
		return minX <= other.minX && maxX >= other.maxX && minY <= other.minY && maxY >= other.maxY;
	}

	public boolean intersects(double x, double y, double width, double height) {
		if (isEmpty())
			return false;
		return (x + width >= minX && y + height >= minY && x <= maxX && y <= maxY);
	}

	public boolean intersectsWithRectangle(Rectangle other) {
		if ((other == null) || other.isEmpty() || isEmpty()) {
			return false;
		}
		return (other.getMaxX() >= minX && other.getMaxY() >= minY && other.getMinX() <= maxX
				&& other.getMinY() <= maxY);
	}

	public boolean disjoint(double x, double y, double width, double height) {
		if (isEmpty())
			return true;
		return (x + width < minX || y + height < minY || x > maxX || y > maxY);
	}

	public boolean disjoint(Rectangle other) {
		if ((other == null) || other.isEmpty() || isEmpty()) {
			return true;
		}
		return (other.getMaxX() < minX || other.getMaxY() < minY || other.getMinX() > maxX || other.getMinY() > maxY);
	}

	public Rectangle makeEmpty() {
        minX = minY = 0.0f;
        maxX = maxY = -1.0f;
        return this;
    }
	
    public boolean isEmpty() {
        // NaN values will cause the comparisons to fail and return "empty"
        return !(maxX >= minX && maxY >= minY);
    }

}
