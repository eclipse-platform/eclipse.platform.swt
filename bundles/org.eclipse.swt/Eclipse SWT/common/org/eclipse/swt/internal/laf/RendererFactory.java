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
package org.eclipse.swt.internal.laf;

/**
 * The renderers of a look and feel: one per drawn widget class, like a Swing UI delegate. A renderer measures,
 * paints and hit-tests a widget; it never owns widget state. A drawn widget (mode <code>custom</code>,
 * <code>skija</code> or <code>gpu</code>) asks its look and feel's factory for its renderer once and falls back to its
 * own default renderer, which reads the look and feel's {@link LookAndFeelDefaults}, when the factory answers
 * <code>null</code>.
 * <p>
 * Adapted from the renderer-factory seam of the Initiative 31 prototype (<code>RendererFactory</code>,
 * <code>DefaultRendererFactory</code>, <code>Display.setRendererFactory</code>), selected at startup by
 * {@link LookAndFeel} instead of a build-time class swap.
 * </p>
 */
public interface RendererFactory {

	/** A factory without renderers: every drawn widget uses its default renderer with the look and feel's defaults. */
	RendererFactory NONE = new RendererFactory () {
		@Override
		public <R> R create (Class<R> type, Object widget) {
			return null;
		}
	};

	/**
	 * Answers the renderer of a widget.
	 *
	 * @param <R> the renderer type
	 * @param type the renderer type the widget uses, for example its default renderer's interface
	 * @param widget the widget that asks
	 * @return the renderer, or <code>null</code> when this look and feel has none for the widget
	 */
	<R> R create (Class<R> type, Object widget);
}
