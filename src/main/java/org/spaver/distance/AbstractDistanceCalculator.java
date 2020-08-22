package org.spaver.distance;

import org.spaver.shape.Point;

public abstract class AbstractDistanceCalculator implements DistanceCalculator {

	public double distance(Point from, Point to) {
		 return distance(from, to.getX(), to.getY());
	}

	public boolean pointInCircle(Point from, double toX, double toY, double distance) {
		return distance(from, toX, toY) <= distance;
	}

}
