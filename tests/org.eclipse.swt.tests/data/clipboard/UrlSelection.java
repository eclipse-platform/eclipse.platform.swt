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
package clipboard;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;

class UrlSelection implements Transferable {

	static DataFlavor flavor;

	static void register() throws ClassNotFoundException {
		flavor = new DataFlavor("application/x-uniform-resourcelocatorw;class=java.io.InputStream");
		SystemFlavorMap map = (SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap();

		String os = System.getProperty("os.name", "").toLowerCase();
		if (os.contains("win")) {
			String nativeFmt = "UniformResourceLocatorW";
			map.addUnencodedNativeForFlavor(flavor, nativeFmt);
			map.addFlavorForUnencodedNative(nativeFmt, flavor);
		} else if (os.contains("mac")) {
			String nativeFmt = "public.url";
			map.addUnencodedNativeForFlavor(flavor, nativeFmt);
			map.addFlavorForUnencodedNative(nativeFmt, flavor);
		} else {
			// X11/Wayland
			String nativeFmt = "text/x-moz-url";
			map.addUnencodedNativeForFlavor(flavor, nativeFmt);
			map.addFlavorForUnencodedNative(nativeFmt, flavor);

		}
	}

	private final byte[] url;

	UrlSelection(byte[] url) {
		this.url = url;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { flavor };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor f) {
		return f.equals(flavor);
	}

	@Override
	public Object getTransferData(DataFlavor f) throws UnsupportedFlavorException {
		if (f.equals(flavor))
			return new ByteArrayInputStream(url);
		throw new UnsupportedFlavorException(f);
	}

}