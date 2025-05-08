/*******************************************************************************
 * Copyright (c) 2019 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Bug553240_ImageLoaderSavingStriped {

	public static void main(final String[] args) {
		try {
			initUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void initUI() throws Exception {
		Path file = Files.createTempFile("swt", "example").toAbsolutePath();
		final Display display = new Display();
		final Shell shell = new Shell();

		shell.setLayout(new RowLayout());
		shell.setText("Bug 553240: Saving a PNG results in a striped Image");

		Label label = new Label(shell, SWT.BORDER);
		label.setText("This snippet loads an Image, saves it, then loads it again.\n"
				+ "The first Image is saved in a temporary file shown in the Text widget.\n"
				+ "The Image loaded the second time is drawn on the Canvas.");
		final Text text = new Text(shell, SWT.BORDER);
		text.setBounds(0, 50, 450, 75);

		ImageData tileImageData = ImageData.load(Path.of("./images/map.png"));
		final ImageLoader imageLoader = new ImageLoader();
		imageLoader.data = new ImageData[] { tileImageData };

		final int imageType = tileImageData.type;
		imageLoader.save(file, imageType);
		text.setText(file.toString());

		Composite composite = new Composite(shell, SWT.NONE);
		RowData layoutData = new RowData(256, 256);
		composite.setLayoutData(layoutData);

		composite.setLayout(new RowLayout());
		Canvas canvas = new Canvas(composite, SWT.NONE);
		layoutData = new RowData(256, 256);
		canvas.setLayoutData(layoutData);

		canvas.addPaintListener(e -> {
			ImageData ctileImageData = ImageData.load(file);
			Image imageToDraw = new Image(display, ctileImageData);
			e.gc.drawImage(imageToDraw, 0, 0);
			imageToDraw.dispose();
		});

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		Files.delete(file);
	}
}
