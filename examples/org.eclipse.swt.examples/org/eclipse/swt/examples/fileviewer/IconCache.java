package org.eclipse.swt.examples.fileviewer;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

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
/* package */ class IconCache {
	// Stock images
	public static final int
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
	public static final String[] stockImageLocations = {
		"icons/generic_example.gif",
		"icons/icon_ClosedDrive.gif",
		"icons/icon_ClosedFolder.gif",
		"icons/icon_File.gif",
		"icons/icon_OpenDrive.gif",
		"icons/icon_OpenFolder.gif",
		"icons/cmd_Copy.gif",
		"icons/cmd_Cut.gif",
		"icons/cmd_Delete.gif",
		"icons/cmd_Parent.gif",
		"icons/cmd_Paste.gif",
		"icons/cmd_Print.gif",
		"icons/cmd_Refresh.gif",
		"icons/cmd_Rename.gif",
		"icons/cmd_Search.gif"
	};
	public static Image stockImages[];
	
	// Stock cursors
	public static final int
		cursorDefault = 0,
		cursorWait = 1;
	public static Cursor stockCursors[];

	// Cached icons
	private static Hashtable iconCache;

	/**
	 * Loads the resources
	 * 
	 * @param display the display
	 */
	public static void initResources(Display display) {
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
				new Cursor(display, SWT.CURSOR_WAIT)
			};
		}
		iconCache = new Hashtable();
	}

	/**
	 * Frees the resources
	 */
	public static void freeResources() {
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
	}

	/**
	 * Creates a stock image
	 * 
	 * @param display the display
	 * @param path the relative path to the icon
	 */
	private static Image createStockImage(Display display, String path) {
		try {
			InputStream stream = IconCache.class.getResourceAsStream(path);
			if (stream != null) {
				ImageData imageData = new ImageData(stream);
				if (imageData != null) {
					ImageData mask = imageData.getTransparencyMask();
					return new Image(display, imageData, mask);
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Gets an image for a file associated with a given program and extension
	 *
	 * @param program the Program
	 * @param extension the extension
	 */
	public static Image getIconFromProgram(Program program, String extension) {
		final String key = program.getName() + "$#$" + extension;
		Image image = (Image) iconCache.get(key);
		if (image == null) {
			ImageData imageData = program.getImageData();
			if (imageData != null) {
				image = new Image(null, imageData, imageData.getTransparencyMask());
				iconCache.put(key, image);
			} else {
				image = stockImages[iconFile];
			}
		}
		return image;
	}
}
