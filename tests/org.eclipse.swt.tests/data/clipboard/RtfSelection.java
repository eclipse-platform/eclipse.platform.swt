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
import java.util.List;

class RtfSelection implements Transferable {
	static DataFlavor flavor;

	static void register() throws Exception {
		flavor = new DataFlavor("text/rtf;class=java.io.InputStream");
		SystemFlavorMap map = (SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap();

		String os = System.getProperty("os.name", "").toLowerCase();
		if (os.contains("win")) {
			String nativeFmt = "Rich Text Format";
			map.addUnencodedNativeForFlavor(flavor, nativeFmt);
			map.addFlavorForUnencodedNative(nativeFmt, flavor);
		} else if (os.contains("mac")) {
			String nativeFmt = "public.rtf";
			map.addUnencodedNativeForFlavor(flavor, nativeFmt);
			map.addFlavorForUnencodedNative(nativeFmt, flavor);
		} else {
			// X11/Wayland
			for (String nativeFmt : List.of("text/rtf", "application/rtf")) {
				map.addUnencodedNativeForFlavor(flavor, nativeFmt);
				map.addFlavorForUnencodedNative(nativeFmt, flavor);
			}
		}
	}

	private final byte[] rtf;

	RtfSelection(byte[] rtf) {
		this.rtf = rtf;
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
			return new ByteArrayInputStream(rtf);
		throw new UnsupportedFlavorException(f);
	}

}