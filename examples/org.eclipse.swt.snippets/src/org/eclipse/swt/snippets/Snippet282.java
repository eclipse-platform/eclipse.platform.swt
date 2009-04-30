/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
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
 * Copy/paste image to/from clipboard.
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet282 {

	public static void main(String[] args) {
		final Display display = new Display();
		final Clipboard clipboard = new Clipboard(display);
		final Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setLayout(new GridLayout());
		shell.setText("Clipboard ImageTransfer");

	    final Button imageButton = new Button(shell, SWT.NONE );
	    GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
	    gd.minimumHeight = 400;
	    gd.minimumWidth = 600;
	    imageButton.setLayoutData(gd);
	    
	    final Text imageText = new Text(shell, SWT.BORDER);
	    imageText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

		Composite buttons = new Composite(shell, SWT.NONE);
		buttons.setLayout(new GridLayout(4, true));
		Button button = new Button(buttons, SWT.PUSH);
		button.setText("Open");
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				FileDialog dialog = new FileDialog (shell, SWT.OPEN);
				dialog.setText("Open an image file or cancel");
				String string = dialog.open ();
				if (string != null) {
					Image image = imageButton.getImage();
					if (image != null) image.dispose();
					image = new Image(display, string);
					imageButton.setImage(image);
					imageText.setText(string);
				}
			}
		});

		button = new Button(buttons, SWT.PUSH);
		button.setText("Copy");
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				Image image = imageButton.getImage();
				if (image != null) {
					ImageTransfer imageTransfer = ImageTransfer.getInstance();
					TextTransfer textTransfer = TextTransfer.getInstance();
					clipboard.setContents(new Object[]{image.getImageData(), imageText.getText()}, 
							new Transfer[]{imageTransfer, textTransfer});
				}
			}
		});

		button = new Button(buttons, SWT.PUSH);
		button.setText("Paste");
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				ImageData imageData = (ImageData)clipboard.getContents(ImageTransfer.getInstance());
				if (imageData != null) {
					imageButton.setText("");
					Image image = imageButton.getImage();
					if (image != null) image.dispose();
					image = new Image(display, imageData);
					imageButton.setImage(image);
				} else {
					imageButton.setText("No image");
				}
				String text = (String)clipboard.getContents(TextTransfer.getInstance());
				if (text != null) {
					imageText.setText(text);
				} else {
					imageText.setText("");
				}
			}
		});
		
		button = new Button(buttons, SWT.PUSH);
		button.setText("Clear");
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				imageButton.setText("");
				Image image = imageButton.getImage();
				if (image != null) image.dispose();
				imageButton.setImage(null);
				imageText.setText("");
			}
		});
		
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
