/*******************************************************************************
 * Copyright (c) 2025 Kichwa Coders Canada, Inc.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.dnd;

import java.time.*;
import java.util.concurrent.*;

import org.eclipse.swt.dnd.ContentProviders.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.GAsyncReadyCallbackHelper.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk4.*;
import org.eclipse.swt.widgets.*;

/**
 * Clipboard proxy used to copy data to the clipboard in GTK4 only.
 *
 * This class has a different life-cycle than Clipboard. Each display has
 * exactly one proxy.
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

	/**
	 * Clear the contents of the specified clipboards
	 *
	 * @param owner         the calling SWT Clipboard
	 * @param clipboards    which clipboards to clear
	 * @param takeOwnership normally we only clear the clipboard if we own it,
	 *                      takeOwnership will take ownership of the clipboard and
	 *                      clear it. If there is a clipboard manager the ownership
	 *                      may be immediately transferred back to the manager.
	 *
	 * @see Clipboard#clearContents()
	 */
	void clear(Clipboard owner, int clipboards, boolean takeOwnership) {
		if ((clipboards & DND.CLIPBOARD) != 0) {
			if (takeOwnership || (activeClipboard == owner && GTK4.gdk_clipboard_is_local(Clipboard.GTKCLIPBOARD))) {
				GTK4.gdk_clipboard_set_content(Clipboard.GTKCLIPBOARD, 0);
				ContentProviders.CLIPBOARD_DATA.CLIPBOARD.clear();
			}
		}
		if (takeOwnership || ((clipboards & DND.SELECTION_CLIPBOARD) != 0 && activePrimaryClipboard == owner)) {
			if (GTK4.gdk_clipboard_is_local(Clipboard.GTKPRIMARYCLIPBOARD)) {
				GTK4.gdk_clipboard_set_content(Clipboard.GTKPRIMARYCLIPBOARD, 0);
				ContentProviders.CLIPBOARD_DATA.PRIMARYCLIPBOARD.clear();
			}
		}
	}

	private void dispose() {
		if (display == null) {
			return;
		}

		CompletableFuture<Boolean> storeClipboardFuture = storeClipboard(Clipboard.GTKCLIPBOARD);
		CompletableFuture<Boolean> storeSelectionFuture = storeClipboard(Clipboard.GTKPRIMARYCLIPBOARD);
		CompletableFuture<Void> combined = CompletableFuture.allOf(storeClipboardFuture, storeSelectionFuture);
		try {
			GTK4GlibFuture.get(display, combined, Duration.ofSeconds(10));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (TimeoutException | ExecutionException e) {
		}

		display = null;

	}

	private CompletableFuture<Boolean> storeClipboard(final long clipboard) {
		CompletableFuture<Boolean> future = new CompletableFuture<>();
		GAsyncReadyCallbackHelper.run(new Async() {
			@Override
			public void async(long callback) {
				GTK4.gdk_clipboard_store_async(clipboard, OS.G_PRIORITY_DEFAULT, 0, callback, 0);
			}

			@Override
			public void callback(long result) {
				future.complete(GTK4.gdk_clipboard_store_finish(clipboard, result, null));
			}
		});
		return future;
	}

	boolean setData(Clipboard owner, Object[] data, Transfer[] dataTypes, int clipboards) {
		boolean result = false;
		if ((clipboards & DND.CLIPBOARD) != 0 || (clipboards & DND.SELECTION_CLIPBOARD) != 0) {
			long clipboard = clipboards == DND.SELECTION_CLIPBOARD ? Clipboard.GTKPRIMARYCLIPBOARD
					: Clipboard.GTKCLIPBOARD;
			CLIPBOARD_DATA clipboardType = CLIPBOARD_DATA.fromDNDConstants(clipboards);

			long providers = contentProviders.createContentProviders(data, dataTypes, clipboardType);
			if (providers != 0) {
				result = GTK4.gdk_clipboard_set_content(clipboard, providers);
				if (clipboards == DND.SELECTION_CLIPBOARD) {
					activePrimaryClipboard = owner;
				} else {
					activeClipboard = owner;
				}
			}
		}
		return result;

	}

	public CompletableFuture<Object> getData(Clipboard owner, Transfer transfer, int clipboards) {

		long clipboard = clipboards == DND.SELECTION_CLIPBOARD ? Clipboard.GTKPRIMARYCLIPBOARD : Clipboard.GTKCLIPBOARD;
		long gType = contentProviders.getGType(transfer);

		final CompletableFuture<Object> future = new CompletableFuture<>();
		GAsyncReadyCallbackHelper.run(new Async() {
			@Override
			public void async(long callback) {
				GTK4.gdk_clipboard_read_value_async(clipboard, gType, OS.G_PRIORITY_DEFAULT, 0, callback, 0);
			}

			@Override
			public void callback(long result) {
				long gvalue = GTK4.gdk_clipboard_read_value_finish(clipboard, result, null);
				if (gvalue == 0) {
					future.complete(null);
				} else {
					Object object = contentProviders.getObject(gvalue);
					future.complete(object);
				}
			}
		});

		return future;
	}
}
