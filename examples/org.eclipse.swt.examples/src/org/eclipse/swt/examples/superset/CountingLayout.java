/*******************************************************************************
 * Copyright (c) 2026 Hitesh Soliwal and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.examples.superset;

import java.lang.reflect.Method;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

/**
 * Layout that counts {@code layout} and {@code computeSize} calls into
 * {@link Counters#LAYOUT} / {@link Counters#MEASURE}. With no delegate it fills
 * the client area with every child; with a delegate (only layouts from
 * {@code org.eclipse.swt.layout}, which no widget inspects by type) it forwards.
 */
public final class CountingLayout extends Layout {

	private final Layout delegate;
	private final Counters counters;
	private Method computeSize, layout, flush;

	public CountingLayout(Layout delegate, Counters counters) {
		this.delegate = delegate;
		this.counters = counters;
		if (delegate != null) bind(delegate.getClass());
	}

	private void bind(Class<?> type) {
		try {
			computeSize = Layout.class.getDeclaredMethod("computeSize", Composite.class, int.class, int.class, boolean.class);
			layout = Layout.class.getDeclaredMethod("layout", Composite.class, boolean.class);
			flush = Layout.class.getDeclaredMethod("flushCache", Control.class);
			computeSize.setAccessible(true);
			layout.setAccessible(true);
			flush.setAccessible(true);
		} catch (ReflectiveOperationException | RuntimeException e) {
			throw new IllegalStateException("cannot wrap " + type.getName(), e);
		}
	}

	/** Wraps every {@code org.eclipse.swt.layout} layout under {@code root}. */
	public static void wrapTree(Control root, Counters counters) {
		if (!(root instanceof Composite composite)) return;
		Layout current = composite.getLayout();
		if (current != null && current.getClass().getName().startsWith("org.eclipse.swt.layout.")) {
			composite.setLayout(new CountingLayout(current, counters));
		}
		for (Control child : composite.getChildren()) {
			wrapTree(child, counters);
		}
	}

	@Override
	protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
		counters.inc(Counters.MEASURE);
		if (delegate == null) return new Point(wHint > 0 ? wHint : 64, hHint > 0 ? hHint : 64);
		return (Point) call(computeSize, composite, wHint, hHint, flushCache);
	}

	@Override
	protected void layout(Composite composite, boolean flushCache) {
		counters.inc(Counters.LAYOUT);
		if (delegate != null) {
			call(layout, composite, flushCache);
			return;
		}
		Rectangle area = composite.getClientArea();
		for (Control child : composite.getChildren()) {
			child.setBounds(area);
		}
	}

	@Override
	protected boolean flushCache(Control control) {
		return delegate == null || Boolean.TRUE.equals(call(flush, control));
	}

	private Object call(Method m, Object... args) {
		try {
			return m.invoke(delegate, args);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException(m.getName(), e);
		}
	}
}
