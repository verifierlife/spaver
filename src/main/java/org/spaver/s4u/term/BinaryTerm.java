package org.spaver.s4u.term;

import org.spaver.shape.PointSet;
import org.spaver.space.WeightedGraph;

/**
 * For the term operator union, intersection, until
 * @author tengf
 *
 */
public interface BinaryTerm extends Term{

	public PointSet getTerm(Term term1, Term term2);
	
	public PointSet satisfactionTerm(WeightedGraph weightedGraph, Term term1, Term term2);
}
