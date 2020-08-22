package org.spaver.shape;

import org.spaver.context.SpatialContext;

public abstract class BaseShape<T extends SpatialContext> implements Shape {

	protected final T ctx;

	public BaseShape(T ctx) {
		this.ctx = ctx;
	}

	public T getContext() {
		return ctx;
	}

}
