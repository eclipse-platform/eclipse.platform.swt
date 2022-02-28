/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
package org.eclipse.swt.snippets;

/*
 * Write an Image to a PNG file.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet246 {
	static Display display;

	public static void main(String[] args) {
		display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 246");
		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);
		createImage();

		String executionPath = System.getProperty("user.dir");
		Label label = new Label(shell, SWT.WRAP);
		label.setText("File created as: " + executionPath.replace("\\", "/")+"/swt.png");
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void createImage() {
		Font font = new Font(display, "Comic Sans MS", 48, SWT.BOLD);
		Image image = new Image(display, 174, 96);
		GC gc = new GC(image);
		gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.fillRectangle(image.getBounds());
		gc.setFont(font);
		gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
		gc.drawString("S", 3, 10);
		gc.setForeground(display.getSystemColor(SWT.COLOR_GREEN));
		gc.drawString("W", 50, 10);
		gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
		gc.drawString("T", 124, 10);
		gc.dispose();

		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { image.getImageData() };
		loader.save("swt.png", SWT.IMAGE_PNG);
		image.dispose();
		font.dispose();
	}
}
