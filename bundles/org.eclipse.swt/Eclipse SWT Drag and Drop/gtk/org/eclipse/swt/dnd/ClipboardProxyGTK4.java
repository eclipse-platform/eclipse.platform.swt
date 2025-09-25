/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.dnd.ContentProviders.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk4.*;
import org.eclipse.swt.widgets.*;

/**
 * Clipboard proxy used to copy data to the clipboard in GTK4 only
 *
 * @see ClipboardProxy the GTK3 version
 */
class ClipboardProxyGTK4 {
	private static String ID = "CLIPBOARD PROXY OBJECT"; //$NON-NLS-1$

	private Display display;
	private Clipboard activeClipboard = null;
	private Clipboard activePrimaryClipboard = null;
	private final ContentProviders contentProviders = ContentProviders.getInstance();

	static ClipboardProxyGTK4 _getInstance(final Display display) {
		if (!GTK.GTK4) {
			throw new UnsupportedOperationException("Illegal attempt to use GTK4 ClipboardProxy on GTK3");
		}
		ClipboardProxyGTK4 proxy = (ClipboardProxyGTK4) display.getData(ID);
		if (proxy != null)
			return proxy;
		proxy = new ClipboardProxyGTK4(display);
		display.setData(ID, proxy);
		display.disposeExec(() -> {
			ClipboardProxyGTK4 clipbordProxy = (ClipboardProxyGTK4) display.getData(ID);
			if (clipbordProxy == null)
				return;
			display.setData(ID, null);
			clipbordProxy.dispose();
		});
		return proxy;
	}

	private ClipboardProxyGTK4(Display display) {
		this.display = display;
	}

	void clear(Clipboard owner, int clipboards) {
		if ((clipboards & DND.CLIPBOARD) != 0 && activeClipboard == owner) {
			gtk_gdk_clipboard_clear(Clipboard.GTKCLIPBOARD);
		}
		if ((clipboards & DND.SELECTION_CLIPBOARD) != 0 && activePrimaryClipboard == owner) {
			gtk_gdk_clipboard_clear(Clipboard.GTKPRIMARYCLIPBOARD);
		}
	}

	private void gtk_gdk_clipboard_clear(long clipboard) {
		// This only clears content if we were owner (i.e. when gdk_clipboard_is_local()
		// == true)
		GDK.gdk_clipboard_set_content(clipboard, 0);
	}

	void dispose() {
		if (display == null) {
			return;
		}
		// TODO - before completing disposal, consider storing data to global clipboard
		// See
		// https://github.com/eclipse-platform/eclipse.platform.swt/issues/2126#issuecomment-3312739514
		display = null;

	}

	boolean setData(Clipboard owner, Object[] data, Transfer[] dataTypes, int clipboards) {
		// TODO: free providers
		long providers = contentProviders.createContentProviders(data, dataTypes,
				CLIPBOARD_TYPE.fromDNDConstants(clipboards), display);
		if (providers == 0) {
			return false;
		}

		boolean result = false;
		if ((clipboards & DND.CLIPBOARD) != 0) {
			long clipboard = clipboards == DND.SELECTION_CLIPBOARD ? Clipboard.GTKPRIMARYCLIPBOARD
					: Clipboard.GTKCLIPBOARD;
			result = GTK4.gdk_clipboard_set_content(clipboard, providers);
			if (clipboards == DND.SELECTION_CLIPBOARD) {
				activePrimaryClipboard = owner;
			} else {
				activeClipboard = owner;
			}
		}
		return result;

	}

	public Object getData(Clipboard owner, Transfer transfer, int clipboards) {

		long clipboard = clipboards == DND.SELECTION_CLIPBOARD ? Clipboard.GTKPRIMARYCLIPBOARD : Clipboard.GTKCLIPBOARD;
		long gType = contentProviders.getGType(transfer);

		System.out.println("About to run gdk_clipboard_read_value_async");
		Object value = new SyncFinishUtil<>().run(display, new SyncFinishCallback<>() {
			@Override
			public void async(long result) {
				GTK4.gdk_clipboard_read_value_async(clipboard, gType, OS.G_PRIORITY_DEFAULT, 0, result, 0);
			}

			@Override
			public Object await(long result) {
				long gvalue = GTK4.gdk_clipboard_read_value_finish(clipboard, result, null);
				if (gvalue == 0) {
					return null;
				}
				return contentProviders.getObject(gvalue);
			}
		});

		if (value == null) {
			// nothing on clipboard, or nothing that can be mapped to transfer's type
			return null;
		}

		return value;
	}
}
