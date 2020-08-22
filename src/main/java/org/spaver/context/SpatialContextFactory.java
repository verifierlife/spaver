package org.spaver.context;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;

import org.spaver.distance.DistanceCalculator;
import org.spaver.shape.Rectangle;
import org.spaver.shape.ShapeFactory;

public class SpatialContextFactory {
	/** Set by {@link #makeSpatialContext(java.util.Map, ClassLoader)}. */
	protected Map<String, String> args;
	/** Set by {@link #makeSpatialContext(java.util.Map, ClassLoader)}. */
	protected ClassLoader classLoader;

	/*
	 * These fields are public to make it easy to set them without bothering with
	 * setters.
	 */

	public boolean geo = true;
	public DistanceCalculator distCalc;// defaults in SpatialContext c'tor based on geo
	public Rectangle worldBounds;// defaults in SpatialContext c'tor based on geo

	public boolean normWrapLongitude = false;

	public Class<? extends ShapeFactory> shapeFactoryClass = ShapeFactory.class;

	/**
	 * Creates a new {@link SpatialContext} based on configuration in
	 * <code>args</code>. See the class definition for what keys are looked up in
	 * it. The factory class is looked up via "spatialContextFactory" in args then
	 * falling back to a Java system property (with initial caps). If neither are
	 * specified then {@link SpatialContextFactory} is chosen.
	 *
	 * @param args        Non-null map of name-value pairs.
	 * @param classLoader Optional, except when a class name is provided to an
	 *                    argument.
	 */
	public static SpatialContext makeSpatialContext(Map<String, String> args, ClassLoader classLoader) {
		if (classLoader == null)
			classLoader = SpatialContextFactory.class.getClassLoader();
		SpatialContextFactory instance;
		String cname = args.get("spatialContextFactory");
		if (cname == null)
			cname = System.getProperty("SpatialContextFactory");
		if (cname == null)
			instance = new SpatialContextFactory();
		else {
			try {
				Class c = classLoader.loadClass(cname);
				instance = (SpatialContextFactory) c.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		// instance.init(args, classLoader);
		return instance.newSpatialContext();
	}

	/*
	 * protected void initWorldBounds() { String worldBoundsStr =
	 * args.get("worldBounds"); if (worldBoundsStr == null) return;
	 * 
	 * //kinda ugly we do this just to read a rectangle. TODO refactor final
	 * SpatialContext ctx = newSpatialContext(); //worldBounds = (Rectangle)
	 * ctx.readShape(worldBoundsStr);//TODO use readShapeFromWkt }
	 * 
	 * /** Subclasses should simply construct the instance from the initialized
	 * configuration.
	 */
	public SpatialContext newSpatialContext() {
		return new SpatialContext(this);
	}

	public ShapeFactory makeShapeFactory(SpatialContext ctx) {
		return makeClassInstance(shapeFactoryClass, ctx, this);
	}

	@SuppressWarnings("unchecked")
	private <T> T makeClassInstance(Class<? extends T> clazz, Object... ctorArgs) {
		try {
			Constructor<?> empty = null;

			// can't simply lookup constructor by arg type because might be subclass type
			ctorLoop: for (Constructor<?> ctor : clazz.getConstructors()) {
				Class[] parameterTypes = ctor.getParameterTypes();
				if (parameterTypes.length == 0) {
					empty = ctor; // the empty constructor;
				}
				if (parameterTypes.length != ctorArgs.length)
					continue;
				for (int i = 0; i < ctorArgs.length; i++) {
					Object ctorArg = ctorArgs[i];
					if (!parameterTypes[i].isAssignableFrom(ctorArg.getClass()))
						continue ctorLoop;
				}
				return clazz.cast(ctor.newInstance(ctorArgs));
			}

			// If an empty constructor exists, use that
			if (empty != null) {
				return clazz.cast(empty.newInstance());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		throw new RuntimeException(clazz + " needs a constructor that takes: " + Arrays.toString(ctorArgs));
	}

}
