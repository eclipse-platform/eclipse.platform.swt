/*******************************************************************************
 * Copyright (c) 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Snippet to demonstrate the use of the DPI aware Image constructors.
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet367 {
	public static void main (String [] args) {
		final Display display = new Display ();
		final ImageFileNameProvider filenameProvider = new ImageFileNameProvider () {
			@Override
			public String getImagePath (int zoom) {
				switch (zoom) {
				case 100:
					return "collapseall.png";
				case 150:
					return "collapseall@1.5x.png";
				case 200:
					return "collapseall@2x.png";
				}
				return null;
			}
		};
		final ImageDataProvider imageDataProvider = new ImageDataProvider () {
			@Override
			public ImageData getImageData (int zoom) {
				switch (zoom) {
				case 100:
					return new ImageData ("collapseall.png");
				case 150:
					return new ImageData ("collapseall@1.5x.png");
				case 200:
					return new ImageData ("collapseall@2x.png");
				}
				return null;
			}
		};
		final Image image_even = new Image (display, filenameProvider);
		final Image image_odd = new Image (display, imageDataProvider);
		final Shell shell = new Shell (display);
		shell.setLayout (new GridLayout ());
		final CTabFolder folder = new CTabFolder (shell, SWT.BORDER);
		folder.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, false));
		folder.setSimple (false);
		folder.setUnselectedImageVisible (false);
		for (int i = 0; i < 8; i++) {
			CTabItem item = new CTabItem (folder, SWT.NONE);
			item.setText ("Item "+i);
			item.setImage ((i % 2 == 0) ? image_even : image_odd);
			Text text = new Text (folder, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
			text.setText ("Switch\nTabs:\n Image\nZooms...\n");
			item.setControl (text);
		}
		shell.setSize (300, 300);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		image_even.dispose ();
		image_odd.dispose();
		display.dispose ();
	}
}
