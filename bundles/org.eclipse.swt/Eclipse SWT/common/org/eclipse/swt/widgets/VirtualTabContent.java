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
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;

/**
 * Lazy tab content of a <code>TabFolder</code> created with {@link SWT#VIRTUAL}, shared by the
 * win32, GTK and Cocoa implementations: {@link SWT#SetData} is sent the first time a tab becomes
 * the selection while the folder is showing, and the listener creates the tab's control. A bit
 * lane records the filled tabs and shifts on insert and remove. A selection made while the folder
 * is hidden only sets a pending bit; the folder's first paint catches up once.
 */
final class VirtualTabContent {
	private final TabFolder folder;
	private VirtualBitLanes filled;
	private boolean pending;
	private Listener catchUp;

	VirtualTabContent (TabFolder folder) {
		this.folder = folder;
	}

	/** A tab was inserted at <code>index</code>. */
	void inserted (int index) {
		if (filled != null) filled.insert (index, 1);
	}

	/** The tab at <code>index</code> was removed. */
	void removed (int index) {
		if (filled != null && index < filled.length ()) filled.remove (index, 1);
	}

	/**
	 * Sends SWT.SetData for the tab at <code>index</code> when it has not been filled yet and the
	 * folder is showing. Returns true when the event was sent.
	 */
	boolean fill (int index) {
		if ((folder.style & SWT.VIRTUAL) == 0 || index < 0) return false;
		int count = folder.getItemCount ();
		if (index >= count) return false;
		if (filled == null) {
			filled = new VirtualBitLanes (1);
			filled.setLength (count);
		}
		if (filled.get (0, index)) return false;
		if (!showing ()) {
			armCatchUp ();
			return false;
		}
		filled.set (0, index, true);
		Event event = new Event ();
		event.item = folder.getItem (index);
		event.index = index;
		folder.sendEvent (SWT.SetData, event);
		return true;
	}

	boolean isFilled (int index) {
		return filled != null && index >= 0 && index < filled.length () && filled.get (0, index);
	}

	boolean pending () {
		return pending;
	}

	/**
	 * Fills the selected tab once the folder shows and lays out its control. Called by the
	 * platform's paint path (win32 WM_PAINT) or by the one-shot paint listener.
	 */
	void catchUp () {
		pending = false;
		if (catchUp != null) {
			folder.removeListener (SWT.Paint, catchUp);
			catchUp = null;
		}
		int index = folder.getSelectionIndex ();
		if (!fill (index) || folder.isDisposed ()) return;
		Control control = folder.getItem (index).getControl ();
		if (control != null && !control.isDisposed ()) {
			control.setBounds (folder.getClientArea ());
			control.setVisible (true);
		}
	}

	/* Until the effective-visibility bit of the visibility leaf lands, showing is isVisible (). */
	private boolean showing () {
		return folder.isVisible ();
	}

	private void armCatchUp () {
		pending = true;
		if (catchUp != null || !folder.paintCatchUpByListener ()) return;
		catchUp = event -> catchUp ();
		folder.addListener (SWT.Paint, catchUp);
	}
}
