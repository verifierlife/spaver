package org.spaver.space;

import org.spaver.shape.Point;

public class WeightEdge {
	
	//attributes
	public Point source; 
	public Point destination; 
	public double weight;
	
	public Point getSource() {
		return source;
	}

	public void setSource(Point source) {
		this.source = source;
	}

	public Point getDestination() {
		return destination;
	}

	public void setDestination(Point destination) {
		this.destination = destination;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	//constructor
	public WeightEdge(Point lStart, Point lEnd) {
		super();
		this.source = lStart;
		this.destination = lEnd;
		this.weight = Math.sqrt((lEnd.x-lStart.x)*(lEnd.x-lStart.x)+(lEnd.y-lStart.y)*(lEnd.y-lStart.y));
	} 
	
}
