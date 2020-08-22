package org.spaver.s4u.term;

import org.spaver.context.SpatialContext;
import org.spaver.context.SpatialContextFactory;
import org.spaver.shape.Point;
import org.spaver.shape.Shape;

public class TermTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpatialContextFactory scf = new SpatialContextFactory();
		SpatialContext sc = new SpatialContext(scf);
		Shape point = new Point(1.0, 2.0, sc);
		
		
		// Math.toDegrees(Math.atan(1))
		System.out.println(Math.sin(Math.atan((double)1/3)));
		System.out.println(Math.cos(Math.toRadians(45.0)));
		System.out.println(Math.cos(Math.atan(1)));
		
		System.out.println((double)1/3);
		
		
	}
	
}
