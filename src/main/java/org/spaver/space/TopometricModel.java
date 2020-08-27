package org.spaver.space;

import java.util.ArrayList;
import java.util.Set;

public interface TopometricModel<L, E> {
	
	public Set<L> getPoints();

	public Set<E> getEdges();
	
	 /**
	 * Computes the external border of <code>set</code>.
	 *
	 * @param sets a set of points
	 *
	 * @return the closure of <code>set</code>.
	 */
	public Set<L> getExternalBorder(Set<L> set);

	 /**
	 * Computes the closure of <code>set</code>.
	 * @param set a set
	 * @return the closure of <code>set</code>
	 */
	 public ArrayList<L> closure( ArrayList<L> set );
	
	 public Set<L> pre(L location);
	
	 public Set<L> post(L location);
	
	 public Set<L> pre(Set<L> locationSet);
	
	 public Set<L> post(Set<L> locationSet);
	
	 public Set<L> getSet(String name);
}
