/*******************************************************************************
 * Copyright (c) 2019 Patrick Tasse and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Patrick Tasse - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Bug547557_ShellPrintGC {

	public static void main(String[] args) {
		Display display = new Display();
		String gtkVersion = System.getProperty("org.eclipse.swt.internal.gtk.version");
		Shell shell = new Shell(display);
		shell.setText(gtkVersion);

		shell.setLayout(new RowLayout(SWT.VERTICAL));

		Label label = new Label(shell, SWT.NONE);
		label.setText(gtkVersion);

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Take Snapshot...");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Add your path here i.e.:
				// String filename = "/home/<user>/shell_test.png";
				String filename = "";
				if ((filename != null) && !filename.isEmpty()) {
					saveImage(shell, filename, SWT.IMAGE_PNG);
				}
			}
		});
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void saveImage(Shell shell, String filename, int format) {
		Rectangle bounds = shell.getBounds();
		Image image = new Image(shell.getDisplay(), bounds);
		// Printing the client area will result in a warning and only the client area being printed
//		Image image = new Image(shell.getDisplay(), shell.getClientArea());
		GC gc = new GC(image);
		shell.print(gc);
		gc.dispose();
		ImageData data = image.getImageData();
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { data };
		loader.save(filename, format);
		image.dispose();
	}
}