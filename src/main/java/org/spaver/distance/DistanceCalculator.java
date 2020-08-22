package org.spaver.distance;

import org.spaver.context.SpatialContext;
import org.spaver.shape.Circle;
import org.spaver.shape.Point;
import org.spaver.shape.Rectangle;

public interface DistanceCalculator {
	 /** The distance between <code>from</code> and <code>to</code>. */
	  public double distance(Point from, Point to);

	  /** The distance between <code>from</code> and <code>Point(toX,toY)</code>. */
	  public double distance(Point from, double toX, double toY);

	  /** Returns true if the distance between from and to is &lt;= distance. */
	  public boolean pointInCircle(Point from, double toX, double toY, double distance);

	  /**
	   * Calculates where a destination point is given an origin (<code>from</code>)
	   * distance, and bearing (given in degrees -- 0-360).  If reuse is given, then
	   * this method may reset() it and return it.
	   */
	  public Point pointOnBearing(Point from, double distDEG, double bearingDEG, SpatialContext ctx, Point reuse);

	  /**
	   * Calculates the bounding box of a circle, as specified by its center point
	   * and distance.
	   */
	  public Rectangle calcBoxByDistFromPt(Point from, double distDEG, SpatialContext ctx, Rectangle reuse);

	  /**
	   * The <code>Y</code> coordinate of the horizontal axis of a circle that has maximum width. On a
	   * 2D plane, this result is always <code>from.getY()</code> but, perhaps surprisingly, on a sphere
	   * it is going to be slightly different.
	   */
	  public double calcBoxByDistFromPt_yHorizAxisDEG(Point from, double distDEG, SpatialContext ctx);

	  public double area(Rectangle rect);

	  public double area(Circle circle);
}
