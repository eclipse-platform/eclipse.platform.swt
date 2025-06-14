/*******************************************************************************
 * Copyright (c) 2025 ETAS GmbH and others, all rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ETAS GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SashWidgetExample {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SWT Sash Example");
		shell.setSize(500, 300);
		shell.setLayout(new FormLayout());

		// Left Composite
		Composite leftComposite = new Composite(shell, SWT.BORDER);
		leftComposite.setBackground(new Color(display, 255, 255, 153));

		// Right Composite (Upper part)
		Composite rightTopComposite = new Composite(shell, SWT.BORDER);
		rightTopComposite.setBackground(new Color(display, 173, 216, 230));

		// Right Composite (Lower part)
		Composite rightBottomComposite = new Composite(shell, SWT.BORDER);
		rightBottomComposite.setBackground(new Color(display, 144, 238, 144));

		// Vertical Sash
		Sash verticalSash = new Sash(shell, SWT.VERTICAL);
		verticalSash.setBackground(display.getSystemColor(SWT.COLOR_GRAY));

		// Horizontal Sash
		Sash horizontalSash = new Sash(shell, SWT.HORIZONTAL);
		horizontalSash.setBackground(display.getSystemColor(SWT.COLOR_GRAY));

		// Layout Management
		FormData leftData = new FormData();
		leftData.left = new FormAttachment(0);
		leftData.top = new FormAttachment(0);
		leftData.bottom = new FormAttachment(100);
		leftData.right = new FormAttachment(verticalSash);
		leftComposite.setLayoutData(leftData);

		FormData verticalSashData = new FormData();
		verticalSashData.top = new FormAttachment(0);
		verticalSashData.bottom = new FormAttachment(100);
		verticalSashData.left = new FormAttachment(50, 0); // Start in the middle
		verticalSash.setLayoutData(verticalSashData);

		FormData rightTopData = new FormData();
		rightTopData.left = new FormAttachment(verticalSash);
		rightTopData.top = new FormAttachment(0);
		rightTopData.bottom = new FormAttachment(horizontalSash);
		rightTopData.right = new FormAttachment(100);
		rightTopComposite.setLayoutData(rightTopData);

		FormData horizontalSashData = new FormData();
		horizontalSashData.left = new FormAttachment(verticalSash);
		horizontalSashData.right = new FormAttachment(100);
		horizontalSashData.top = new FormAttachment(50, 0);
		horizontalSash.setLayoutData(horizontalSashData);

		FormData rightBottomData = new FormData();
		rightBottomData.left = new FormAttachment(verticalSash);
		rightBottomData.top = new FormAttachment(horizontalSash);
		rightBottomData.bottom = new FormAttachment(100);
		rightBottomData.right = new FormAttachment(100);
		rightBottomComposite.setLayoutData(rightBottomData);

		// set the focus to the sash to enable dragging using the keyboard(Arrow
		// keys/ctrl+Arrow keys)
		verticalSash.setFocus();
		// Vertical Sash Dragging Logic
		verticalSash.addListener(SWT.Selection, e -> {
			Rectangle sashRect = verticalSash.getBounds();
			Rectangle shellRect = shell.getClientArea();
			int right = shellRect.width - sashRect.width - 20;
			e.x = Math.max(Math.min(e.x, right), 20);
			if (e.x != sashRect.x) {
				verticalSashData.left = new FormAttachment(0, e.x);
				shell.layout();
			}
		});

		// Horizontal Sash Dragging Logic
		horizontalSash.addListener(SWT.Selection, e -> {
			Rectangle sashRect = horizontalSash.getBounds();
			Rectangle shellRect = shell.getClientArea();
			int bottom = shellRect.height - sashRect.height - 20;
			e.y = Math.max(Math.min(e.y, bottom), 20);
			if (e.y != sashRect.y) {
				horizontalSashData.top = new FormAttachment(0, e.y);
				shell.layout();
			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
