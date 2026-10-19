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

import org.eclipse.swt.widgets.*;

/**
 * The UI delegate of a drawn widget (docs/m3/LOOK_AND_FEEL.md, LF6), Swing's <code>ComponentUI</code>: it measures and
 * paints from the look and feel's {@link M3UIDefaults}, never owns widget state, and is created by the look and
 * feel's {@link RendererFactory} (or is the widget's default delegate). {@link M3LookAndFeel#installUI} keeps it in the
 * widget's data.
 * <p>
 * A delegate declares the keys it reads: when a look and feel switch changes one of its {@link #paintKeys()} the
 * widget is repainted, one of its {@link #metricKeys()} relaid out; a switch that changes neither leaves the widget
 * alone. Colours and fonts come from {@link M3UIResources} at paint time and are not kept across a switch.
 * </p>
 */
public interface M3ComponentUI {
	/** No keys. */
	int [] NO_KEYS = {};

	/**
	 * Installs the delegate on a widget with the defaults of its look and feel (also after a switch that changed a
	 * key it reads).
	 */
	void installUI (Control control, M3UIDefaults defaults);

	/** Uninstalls the delegate from a widget (before a switch replaces it, or when the widget drops it). */
	default void uninstallUI (Control control) {
	}

	/** The type a look and feel's factory is asked for when the delegate is created again, for example its interface. */
	default Class<? extends M3ComponentUI> type () {
		return getClass ();
	}

	/** The key ids ({@link M3UIKeys}) the delegate paints with. */
	int [] paintKeys ();

	/** The key ids the delegate measures with (sizes, insets, fonts). */
	default int [] metricKeys () {
		return NO_KEYS;
	}
}
