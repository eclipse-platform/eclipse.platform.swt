/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.superset;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Late binding to classes that are not on the examples bundle's compile
 * classpath (Nebula, platform-specific SWT). The showcase compiles against SWT
 * only; Nebula jars are added to the runtime classpath by run-matrix.sh.
 */
public final class Reflect {

	private static final Map<Class<?>, Class<?>> BOXES = Map.of(int.class, Integer.class, long.class, Long.class,
			boolean.class, Boolean.class, double.class, Double.class, float.class, Float.class, char.class,
			Character.class, short.class, Short.class, byte.class, Byte.class);

	private Reflect() {
	}

	/** @return the class, or null when it is absent from the runtime classpath */
	public static Class<?> type(String name) {
		try {
			return Class.forName(name, false, Reflect.class.getClassLoader());
		} catch (ClassNotFoundException | LinkageError e) {
			return null;
		}
	}

	/** Creates {@code className} with the first constructor accepting {@code args}. */
	public static Object make(String className, Object... args) {
		Class<?> type = type(className);
		if (type == null) throw new IllegalStateException("class absent: " + className);
		for (Constructor<?> c : type.getConstructors()) {
			if (fits(c.getParameterTypes(), args)) return invoke(() -> c.newInstance(args));
		}
		throw new IllegalStateException("no public constructor " + type.getSimpleName() + sig(args));
	}

	/** Calls the first public method named {@code name} accepting {@code args}. */
	public static Object call(Object target, String name, Object... args) {
		Method m = method(target.getClass(), name, args);
		if (m == null) throw new IllegalStateException("no method " + target.getClass().getSimpleName() + "." + name + sig(args));
		return invoke(() -> m.invoke(target, args));
	}

	/** Resolves a public method once, for hot paths (SetData, scroll frames). */
	public static Method find(String className, String name, Class<?>... params) {
		Class<?> type = type(className);
		if (type == null) throw new IllegalStateException("class absent: " + className);
		try {
			return type.getMethod(name, params);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("no method " + type.getSimpleName() + "." + name, e);
		}
	}

	/** Resolves the public constructor of {@code className} whose parameters accept {@code sample} args. */
	public static Constructor<?> constructor(String className, Object... sample) {
		Class<?> type = type(className);
		if (type == null) throw new IllegalStateException("class absent: " + className);
		for (Constructor<?> c : type.getConstructors()) {
			if (fits(c.getParameterTypes(), sample)) return c;
		}
		throw new IllegalStateException("no public constructor " + type.getSimpleName() + sig(sample));
	}

	/** Creates an instance through a constructor resolved by {@link #constructor}. */
	public static Object create(Constructor<?> c, Object... args) {
		return invoke(() -> c.newInstance(args));
	}

	/** Invokes a method resolved by {@link #find}. */
	public static Object exec(Method m, Object target, Object... args) {
		return invoke(() -> m.invoke(target, args));
	}

	/** Calls the first public static method {@code className.name} accepting {@code args}. */
	public static Object callStatic(String className, String name, Object... args) {
		Class<?> type = type(className);
		if (type == null) throw new IllegalStateException("class absent: " + className);
		Method m = method(type, name, args);
		if (m == null) throw new IllegalStateException("no method " + type.getSimpleName() + "." + name + sig(args));
		return invoke(() -> m.invoke(null, args));
	}

	/** Implements {@code interfaceName} with defaults: null, false, 0. */
	public static Object nullProxy(String interfaceName) {
		return proxy(interfaceName, (p, m, a) -> {
			Class<?> r = m.getReturnType();
			if (r.isPrimitive() && r != void.class) return java.lang.reflect.Array.get(java.lang.reflect.Array.newInstance(r, 1), 0);
			if ("toString".equals(m.getName())) return interfaceName;
			if ("hashCode".equals(m.getName())) return System.identityHashCode(p);
			if ("equals".equals(m.getName())) return p == a[0];
			return null;
		});
	}

	/** @return true when a public method named {@code name} accepts {@code args} */
	public static boolean has(Object target, String name, Object... args) {
		return method(target.getClass(), name, args) != null;
	}

	/** Implements a Nebula listener/provider interface with {@code handler}. */
	public static Object proxy(String interfaceName, java.lang.reflect.InvocationHandler handler) {
		Class<?> type = type(interfaceName);
		if (type == null) throw new IllegalStateException("class absent: " + interfaceName);
		return Proxy.newProxyInstance(Reflect.class.getClassLoader(), new Class<?>[] {type}, handler);
	}

	private static Method method(Class<?> type, String name, Object[] args) {
		for (Method m : type.getMethods()) {
			if (m.getName().equals(name) && fits(m.getParameterTypes(), args)) return m;
		}
		return null;
	}

	private static boolean fits(Class<?>[] params, Object[] args) {
		if (params.length != args.length) return false;
		for (int i = 0; i < params.length; i++) {
			Class<?> p = params[i].isPrimitive() ? BOXES.get(params[i]) : params[i];
			if (args[i] == null ? params[i].isPrimitive() : !p.isInstance(args[i])) return false;
		}
		return true;
	}

	private static String sig(Object[] args) {
		StringBuilder b = new StringBuilder("(");
		for (Object a : args) b.append(b.length() > 1 ? "," : "").append(a == null ? "null" : a.getClass().getSimpleName());
		return b.append(')').toString();
	}

	private interface Call {
		Object run() throws ReflectiveOperationException;
	}

	private static Object invoke(Call call) {
		try {
			return call.run();
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof RuntimeException r) throw r;
			if (cause instanceof Error err) throw err;
			throw new IllegalStateException(cause);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException(e);
		}
	}
}
