/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.fileviewer;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.program.*;
import org.eclipse.swt.widgets.*;

import java.io.*;
import java.util.*;

/**
 * Manages icons for the application.
 * This is necessary as we could easily end up creating thousands of icons
 * bearing the same image.
 */
class IconCache {
	// Stock images
	public final int
		shellIcon = 0,
		iconClosedDrive = 1,
		iconClosedFolder = 2,
		iconFile = 3,
		iconOpenDrive = 4,
		iconOpenFolder = 5,
		cmdCopy = 6,
		cmdCut = 7,
		cmdDelete = 8,
		cmdParent = 9,
		cmdPaste = 10,
		cmdPrint = 11,
		cmdRefresh = 12,
		cmdRename = 13,
		cmdSearch = 14;
	public final String[] stockImageLocations = {
		"generic_example.gif",
		"icon_ClosedDrive.gif",
		"icon_ClosedFolder.gif",
		"icon_File.gif",
		"icon_OpenDrive.gif",
		"icon_OpenFolder.gif",
		"cmd_Copy.gif",
		"cmd_Cut.gif",
		"cmd_Delete.gif",
		"cmd_Parent.gif",
		"cmd_Paste.gif",
		"cmd_Print.gif",
		"cmd_Refresh.gif",
		"cmd_Rename.gif",
		"cmd_Search.gif"
	};
	public Image stockImages[];
	
	// Stock cursors
	public final int
		cursorDefault = 0,
		cursorWait = 1;
	public Cursor stockCursors[];
	// Cached icons
	private Hashtable iconCache; /* map Program to Image */
	
	public IconCache() {
	}
	/**
	 * Loads the resources
	 * 
	 * @param display the display
	 */
	public void initResources(Display display) {
		if (stockImages == null) {
			stockImages = new Image[stockImageLocations.length];
				
			for (int i = 0; i < stockImageLocations.length; ++i) {
				Image image = createStockImage(display, stockImageLocations[i]);
				if (image == null) {
					freeResources();
					throw new IllegalStateException(
						FileViewer.getResourceString("error.CouldNotLoadResources"));
				}
				stockImages[i] = image;
			}
		}	
		if (stockCursors == null) {
			stockCursors = new Cursor[] {
				null,
				display.getSystemCursor(SWT.CURSOR_WAIT)
			};
		}
		iconCache = new Hashtable();
	}
	/**
	 * Frees the resources
	 */
	public void freeResources() {
		if (stockImages != null) {
			for (int i = 0; i < stockImages.length; ++i) {
				final Image image = stockImages[i];
				if (image != null) image.dispose();
			}
			stockImages = null;
		}
		if (iconCache != null) {
			for (Enumeration it = iconCache.elements(); it.hasMoreElements(); ) {
				Image image = (Image) it.nextElement();
				image.dispose();
			}
		}
		stockCursors = null;
	}
	/**
	 * Creates a stock image
	 * 
	 * @param display the display
	 * @param path the relative path to the icon
	 */
	private Image createStockImage(Display display, String path) {
		InputStream stream = IconCache.class.getResourceAsStream (path);
		ImageData imageData = new ImageData (stream);
		ImageData mask = imageData.getTransparencyMask ();
		Image result = new Image (display, imageData, mask);
		try {
			stream.close ();
		} catch (IOException e) {
			e.printStackTrace ();
		}
		return result;
	}
	/**
	 * Gets an image for a file associated with a given program
	 *
	 * @param program the Program
	 */
	public Image getIconFromProgram(Program program) {
		Image image = (Image) iconCache.get(program);
		if (image == null) {
			ImageData imageData = program.getImageData();
			if (imageData != null) {
				image = new Image(null, imageData, imageData.getTransparencyMask());
				iconCache.put(program, image);
			} else {
				image = stockImages[iconFile];
			}
		}
		return image;
	}
}
