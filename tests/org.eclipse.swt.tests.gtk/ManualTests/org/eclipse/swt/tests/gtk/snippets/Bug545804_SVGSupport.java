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
 *     Red Hat Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import java.nio.file.Path;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug545804_SVGSupport {

public static void main(String[] args) {
	Display display = new Display();
	ImageData data = ImageData.load(Path.of("./images/red_hat_fedora.svg"));
	Image image = new Image(display, data);
	Shell shell = new Shell(display);
	shell.setLayout (new GridLayout());
	Button button = new Button(shell, SWT.PUSH);
	button.setImage(image);
	button.setText("Button");
	shell.setSize(300, 300);
	shell.open();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
	image.dispose();
}
}
