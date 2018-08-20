/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
 * Create a ScrolledComposite with wrapping content.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.0
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet166 {

public static void main(String[] args) {
	Display display = new Display();
	Image image1 = display.getSystemImage(SWT.ICON_WORKING);
	Image image2 = display.getSystemImage(SWT.ICON_QUESTION);
	Image image3 = display.getSystemImage(SWT.ICON_ERROR);

	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());

	final ScrolledComposite scrollComposite = new ScrolledComposite(shell, SWT.V_SCROLL | SWT.BORDER);

	final Composite parent = new Composite(scrollComposite, SWT.NONE);
	for(int i = 0; i <= 50; i++) {
		Label label = new Label(parent, SWT.NONE);
		if (i % 3 == 0) label.setImage(image1);
		if (i % 3 == 1) label.setImage(image2);
		if (i % 3 == 2) label.setImage(image3);
	}
	RowLayout layout = new RowLayout(SWT.HORIZONTAL);
	layout.wrap = true;
	parent.setLayout(layout);

	scrollComposite.setContent(parent);
	scrollComposite.setExpandVertical(true);
	scrollComposite.setExpandHorizontal(true);
	scrollComposite.addControlListener(ControlListener.controlResizedAdapter(e -> {
		Rectangle r = scrollComposite.getClientArea();
		scrollComposite.setMinSize(parent.computeSize(r.width, SWT.DEFAULT));
	}));

	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) {
			display.sleep();
		}
	}
	display.dispose();
}
}
