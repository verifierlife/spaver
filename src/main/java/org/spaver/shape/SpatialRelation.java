package org.spaver.shape;

public enum SpatialRelation {

	 /**
	   * The shape shares no point in common with the target shape.
	   */
	  DISCONNECT,
	  
	  /**
	   * The shape only contains one points when externally connected: inside tangency
	   */
	  EXTERCONNECT,
	  
	  /**
	   * overlaps
	   */
	  PARTIALOVERLAP,
	  //Don't have these: TOUCHES, CROSSES, OVERLAPS, nor distinction between CONTAINS/COVERS

	  /**
	   * Tangential proper part (TPP): outside tangency
	   */
	  TANGENCY,
	  
	  /**
	   * Tangential proper part inverse (TPPI)
	   */
	  TANGENCYINVERSE,
	  
	  /**
	   * The shape contains the target geometry. It's the converse of {@link #WITHIN}.
	   * Boundaries of shapes count too.  OGC specs refer to this relation as "COVERS";
	   * CONTAINS is differentiated there by not including boundaries.
	   */ 
	  
	  /**
	   * The shape is within the target geometry. It's the converse of {@link #CONTAINS}.
	   * Boundaries of shapes count too.  OGC specs refer to this relation as "COVERED BY";
	   * WITHIN is differentiated there by not including boundaries.
	   */
	  NONTANGENCY,

	 
	  NONTANGENCYINVERSE,
	  
	  EQUAL;
	  
	  /**
	   * The shape shares some points/overlap with the target shape, and the relation is
	   * not more specifically {@link #WITHIN} or {@link #CONTAINS}.
	   */
	
	  /**
	   * Given the result of <code>shapeA.relate(shapeB)</code>, transposing that
	   * result should yield the result of <code>shapeB.relate(shapeA)</code>. There
	   * is a corner case is when the shapes are equal, in which case actually
	   * flipping the relate() call will result in the same value -- either CONTAINS
	   * or WITHIN; this method can't possible check for that so the caller might
	   * have to.
	   */
	  public SpatialRelation transpose() {
	    switch(this) {
	      case NONTANGENCY: return SpatialRelation.NONTANGENCYINVERSE;
	      case NONTANGENCYINVERSE: return SpatialRelation.NONTANGENCY;
	      case TANGENCY: return SpatialRelation.TANGENCYINVERSE;
	      case TANGENCYINVERSE: return SpatialRelation.TANGENCY;
	      default: return this;
	    }
	  }

	  /**
	   * If you were to call aShape.relate(bShape) and aShape.relate(cShape), you
	   * could call this to merge the intersect results as if bShape &amp; cShape were
	   * combined into {@link ShapeCollection}.  If {@code other} is null then the
	   * result is "this".
	   */
	  public SpatialRelation combine(SpatialRelation other) {
	    // You can think of this algorithm as a state transition / automata.
	    // 1. The answer must be the same no matter what the order is.
	    // 2. If any INTERSECTS, then the result is INTERSECTS (done).
	    // 3. A DISJOINT + WITHIN == INTERSECTS (done).
	    // 4. A DISJOINT + CONTAINS == CONTAINS.
	    // 5. A CONTAINS + WITHIN == INTERSECTS (done). (weird scenario)
	    // 6. X + X == X.)
	    // 7. X + null == X;
	    if (other == this || other == null)
	      return this;
	    if (this == DISCONNECT && other == NONTANGENCY
	        || this == NONTANGENCY && other == DISCONNECT)
	      return NONTANGENCY;
	    return PARTIALOVERLAP;
	  }

	  /** Not DISJOINT, i.e. there is some sort of intersection. */
	  public boolean intersects() {
	    return this != DISCONNECT;
	  }

	  /**
	   * If <code>aShape.relate(bShape)</code> is r, then <code>r.inverse()</code>
	   * is <code> inverse(aShape).relate(bShape)</code> whereas
	   * <code>inverse(shape)</code> is theoretically the opposite area covered by a
	   * shape, i.e. everywhere but where the shape is.
	   * <p>
	   * Note that it's not commutative!  <code>WITHIN.inverse().inverse() !=
	   * WITHIN</code>.
	   */
	  public SpatialRelation inverse() {
	    switch(this) {
	      case DISCONNECT: return NONTANGENCY;
	      case NONTANGENCY: return DISCONNECT;
	    }
	    return PARTIALOVERLAP;
	  }

}
