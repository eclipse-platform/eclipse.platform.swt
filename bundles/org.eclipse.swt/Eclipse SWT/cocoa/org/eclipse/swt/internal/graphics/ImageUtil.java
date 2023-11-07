/*******************************************************************************
 * Copyright (c) 2020 IBM Corporation and others.
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
package org.eclipse.swt.internal.graphics;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;

/**
 * This class holds utility methods for image manipulation on Cocoa platform
 * Not used on other platforms
 *
 * @since 3.110
 */
public class ImageUtil {
	/**
	 * Creates new image representation based on the source image.
	 *
	 * @param image      Source image object
	 * @param targetSize the size at which image representation needs to be created
	 *
	 * @return image representation
	 *
	 * @since 3.110
	 */
	public static NSBitmapImageRep createImageRep(Image image, NSSize targetSize) {
		NSBitmapImageRep rep;
		NSImage imgHandle= image.handle;
		rep = (NSBitmapImageRep)new NSBitmapImageRep().alloc();
		rep = rep.initWithBitmapDataPlanes(0, (int) targetSize.width, (int) targetSize.height, 8, 4, true, false, OS.NSDeviceRGBColorSpace, OS.NSAlphaFirstBitmapFormat, (int) targetSize.width * 4, 32);
		C.memset(rep.bitmapData(), 0xFF, (int) targetSize.width * (int)targetSize.height * 4);
		NSGraphicsContext context = NSGraphicsContext.graphicsContextWithBitmapImageRep(rep);
		NSGraphicsContext.static_saveGraphicsState();
		context.setImageInterpolation(OS.NSImageInterpolationHigh);
		NSGraphicsContext.setCurrentContext(context);
		NSRect target = new NSRect();
		target.width = targetSize.width;
		target.height = targetSize.height;
		NSRect sourceRect = new NSRect();
		sourceRect.width = 0;
		sourceRect.height = 0;
		imgHandle.drawInRect(target, sourceRect, OS.NSCompositeCopy, 1);
		NSGraphicsContext.static_restoreGraphicsState();
		rep.autorelease();
		return rep;
	}
}
