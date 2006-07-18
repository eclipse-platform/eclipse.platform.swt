/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
 * Write an Image to a PNG file.
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet246 {

	public static void main(String[] args) {
		Display display = new Display();
		Font font = new Font(display, "Comic Sans MS", 24, SWT.BOLD);
		Image image = new Image(display, 87, 48);
		GC gc = new GC(image);
		gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.fillRectangle(image.getBounds());
		gc.setFont(font);
		gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
		gc.drawString("S", 3, 0);
		gc.setForeground(display.getSystemColor(SWT.COLOR_GREEN));
		gc.drawString("W", 25, 0);
		gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
		gc.drawString("T", 62, 0);
		gc.dispose();

		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] {image.getImageData()};
		loader.save("swt.png", SWT.IMAGE_PNG);

		image.dispose();
		font.dispose();
		display.dispose();
	}
}
