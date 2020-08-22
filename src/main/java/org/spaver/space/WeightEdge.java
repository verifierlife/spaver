package org.spaver.space;

import org.spaver.shape.Point;

public class WeightEdge {
	
	//attributes
	public Point lStart; 
	public Point lEnd; 
	public double weight;
	
	//constructor
	public WeightEdge(Point lStart, Point lEnd, double weight) {
		super();
		this.lStart = lStart;
		this.lEnd = lEnd;
		this.weight = weight;
	} 
	
	
}
