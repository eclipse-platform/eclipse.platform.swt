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

public final class MyTypeSelection implements Transferable {

	static DataFlavor flavor;

	static void register() throws ClassNotFoundException {
		flavor = new DataFlavor("application/x-my_type_name;class=java.io.InputStream", "my_type_name");
		SystemFlavorMap map = (SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap();
		map.addUnencodedNativeForFlavor(flavor, "my_type_name");
		map.addFlavorForUnencodedNative("my_type_name", flavor);
	}

	private final byte[] bytes;

	MyTypeSelection(byte[] bytes) {
		this.bytes = bytes;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { flavor };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(flavor);
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
		if (!isDataFlavorSupported(flavor))
			throw new UnsupportedFlavorException(flavor);
		return new ByteArrayInputStream(bytes);
	}
}