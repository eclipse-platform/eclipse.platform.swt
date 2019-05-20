/*******************************************************************************
 * Copyright (c) 2019 Paul Pazderski and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Paul Pazderski - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.manual;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Snippet to test {@link Label} with different invocation orders of
 * {@link Label#setText(String)} and {@link Label#setImage(Image)}.
 * <p>
 * For Win32 and Bug 205199 when the label set a text then set an image and
 * later removed the image using <code>setImage(null)</code> (variant 7 in this
 * example) instead of showing the text again nothing was rendered.
 * </p>
 * <p>
 * The example shows from left to right:
 * </p>
 * <ol>
 * <li>a label only setting text -> text visible</li>
 * <li>a label only setting image -> image visible</li>
 * <li>a label first setting text then image -> image visible</li>
 * <li>a label first setting image then text -> text visible</li>
 * <li>a label first setting text then image then first text again -> text
 * visible</li>
 * <li>a label first setting image then text then first image again -> image
 * visible</li>
 * <li>a label first setting text then image then image to null -> text
 * visible</li>
 * </ol>
 */
public class Bug205199_Label_Image_vs_Text {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);

		Composite container = new Composite(shell, SWT.NONE);
		container.setLayout(new RowLayout(SWT.HORIZONTAL));

		Image image = new Image(display, 32, 32);
		Color color = display.getSystemColor(SWT.COLOR_DARK_GREEN);
		GC gc = new GC(image);
		gc.setBackground(color);
		gc.fillRectangle(image.getBounds());
		gc.dispose();

		Label labelText = new Label(container, SWT.NONE);
		Label labelImage = new Label(container, SWT.NONE);
		Label labelTextAndImage = new Label(container, SWT.NONE);
		Label labelImageAndText = new Label(container, SWT.NONE);
		Label labelTextAndImageAndText = new Label(container, SWT.NONE);
		Label labelImageAndTextAndImage = new Label(container, SWT.NONE);
		Label labelTextAndImageAndImageNull = new Label(container, SWT.NONE);

		labelText.setText("Text");

		labelImage.setImage(image);

		labelTextAndImage.setText("Text");
		labelTextAndImage.setImage(image);

		labelImageAndText.setImage(image);
		labelImageAndText.setText("Text");

		labelTextAndImageAndText.setText("Text");
		labelTextAndImageAndText.setImage(image);
		labelTextAndImageAndText.setText("Text");

		labelImageAndTextAndImage.setImage(image);
		labelImageAndTextAndImage.setText("Text");
		labelImageAndTextAndImage.setImage(image);

		labelTextAndImageAndImageNull.setText("Text");
		labelTextAndImageAndImageNull.setImage(image);
		labelTextAndImageAndImageNull.setImage(null);

		container.pack();
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		image.dispose();
		display.dispose();
	}
}
