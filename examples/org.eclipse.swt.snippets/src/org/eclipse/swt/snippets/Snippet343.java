/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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
 * ExpandBar snippet: change an ExpandItem's height in response to a change in a
 * contained Control
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

public class Snippet343 {

public static void main(String[] args) {
	final Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	shell.setSize(400, 400);
	SashForm sashForm = new SashForm(shell, SWT.HORIZONTAL);
	Composite leftComposite = new Composite(sashForm, SWT.NONE);
	leftComposite.setLayout(new FillLayout());
	Composite rightComposite = new Composite(sashForm, SWT.NONE);
	rightComposite.setLayout(new FillLayout());
	ExpandBar expandBar = new ExpandBar(leftComposite, SWT.NONE);
	final ExpandItem expandItem1 = new ExpandItem(expandBar, SWT.NONE);
	expandItem1.setText("item 1");
	new ExpandItem(expandBar, SWT.NONE).setText("item 2"); /* expandItem2 */

	final StyledText text = new StyledText(expandBar, SWT.MULTI | SWT.WRAP);
	expandItem1.setControl(text);
	text.setText("initial text that will wrap if it's long enough");

	/* update the item's height if needed in response to changes in the text's size */ 
	final int TRIAL_WIDTH = 100;
	final int trimWidth = text.computeTrim(0, 0, TRIAL_WIDTH, 100).width - TRIAL_WIDTH;
	text.addListener(SWT.Modify, new Listener() {
		public void handleEvent(Event event) {
			Point size = text.computeSize(text.getSize().x - trimWidth, SWT.DEFAULT);
			if (expandItem1.getHeight() != size.y) {
				expandItem1.setHeight(size.y);
			}
		}
	});
	expandBar.addListener(SWT.Resize, new Listener() {
		public void handleEvent(Event event) {
			/* 
			 * The following is done asynchronously to allow the Text's width
			 * to be changed before re-calculating its preferred height. 
			 */
			display.asyncExec(new Runnable() {
				public void run() {
					if (text.isDisposed()) return;
					Point size = text.computeSize(text.getSize().x - trimWidth, SWT.DEFAULT);
					if (expandItem1.getHeight() != size.y) {
						expandItem1.setHeight(size.y);
					}
				}
			});
		}
	});

	shell.open();
	/* set the item's initial height */
	Point size = text.computeSize(expandBar.getClientArea().width, SWT.DEFAULT);
	expandItem1.setHeight(size.y);
	expandItem1.setExpanded(true);

	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}

}
