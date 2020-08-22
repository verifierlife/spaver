package org.spaver.s4u.term;

import org.spaver.shape.PointSet;
import org.spaver.space.WeightedGraph;

/**
 * UnaryTerm for p, complementary, interior, closure and near
 * @author tengf
 *
 */
public interface UnaryTerm extends Term{
	
	/**
	 * For region-based space
	 * @param term
	 * @return
	 */
	public PointSet getTerm(Term term);
	
	/**
	 * For point-based region, constructing a satisfaction function
	 * @param weightedGraph
	 * @param term
	 * @return
	 */
	public PointSet satisfactionTerm(WeightedGraph weightedGraph, Term term);
	
}
