/*******************************************************************************
 * Copyright (c) 2022 IBM Corporation and others.
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
package org.eclipse.swt.tests.win32.snippets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SWTIssue185 {

	public static void main(String[] args) {
		final Display display = new Display();

		final List<Image> images = createImages(display, "zip", "jpg", "txt", "pdf", "png", "msi", "theme");

		final Shell shell = new Shell(display);

		shell.addListener(SWT.Paint, event -> {
			int x = 0;
			for (Image image : images) {
				event.gc.drawImage(image, x, 0);
				x += image.getImageData().width;
				x += 10;
			}
		});

		shell.setSize(400, 300);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		for (Image image : images) {
			image.dispose();
		}

		display.dispose();
	}

	private static List<Image> createImages(Display display, String... extensions) {
		final List<Image> images = new ArrayList<>();
		for (String extension : extensions) {
			final Program program = Program.findProgram(extension);
			if (program == null) {
				continue;
			}
			final ImageData data = program.getImageData();
			if (data == null) {
				continue;
			}
			images.add(new Image(display, data));
		}
		return images;
	}

}
