/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
package org.eclipse.swt.examples.graphics;

/*
 * Drawing with transformations, paths and alpha blending
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class AdvancedGraphics {

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("examples_graphics");

	static Image loadImage (Device device, Class<AdvancedGraphics> clazz, String string) {
		InputStream stream = clazz.getResourceAsStream (string);
		if (stream == null) return null;
		Image image = null;
		try {
			image = new Image (device, stream);
		} catch (SWTException ex) {
		} finally {
			try {
				stream.close ();
			} catch (IOException ex) {}
		}
		return image;
	}

	public Shell open(final Display display) {
		final Shell shell = new Shell(display);
		shell.setText(RESOURCE_BUNDLE.getString("AdvancedGraphics")); //$NON-NLS-1$
		try {
			Path path = new Path(display);
			path.dispose();
		} catch (SWTException e) {
			MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
			dialog.setText(RESOURCE_BUNDLE.getString("Warning")); //$NON-NLS-1$
			dialog.setMessage(RESOURCE_BUNDLE.getString("LibNotFound")); //$NON-NLS-1$
			dialog.open();
			shell.dispose();
			return null;
		}
		FontData fd = shell.getFont().getFontData()[0];
		final Font font = new Font(display, fd.getName(), 96, SWT.BOLD | SWT.ITALIC);
		final Image image = loadImage(display, AdvancedGraphics.class, "irmaos.jpg");
		final Rectangle rect = image.getBounds();
		shell.addListener(SWT.Paint, event -> {
			GC gc = event.gc;
			Transform tr = new Transform(display);
			tr.translate(rect.width / 4, rect.height / 2);
			tr.rotate(-30);
			if (image != null) {
				gc.drawImage(image, 0, 0, rect.width, rect.height, 0, 0, rect.width, rect.height);
			}
			gc.setAlpha(100);
			gc.setTransform(tr);
			Path path = new Path(display);
			path.addString("SWT", 0, 0, font);
			gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
			gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
			gc.fillPath(path);
			gc.drawPath(path);
			tr.dispose();
			path.dispose();
		});
		shell.setSize(shell.computeSize(rect.width, rect.height));
		shell.open();
		shell.addListener(SWT.Dispose, event -> {
			if (image != null) image.dispose();
			font.dispose();
		});
		return shell;
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new AdvancedGraphics().open(display);
		while (shell != null && !shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
