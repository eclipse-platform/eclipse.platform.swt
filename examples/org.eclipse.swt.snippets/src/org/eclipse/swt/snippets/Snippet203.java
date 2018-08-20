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
package org.eclipse.swt.snippets;

/*
 * TextLayout example snippet: using TextLayout justify, alignment and indent
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet203 {

public static void main(String[] args) {
	Display display = new Display();
	final Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.DOUBLE_BUFFERED);
	shell.setText("Indent, Justify, Align");
	String[] texts = {
		"Plans do not materialize out of nowhere, nor are they entirely static. To ensure the planning process is transparent and open to the entire Eclipse community, we (the Eclipse PMC) post plans in an embryonic form and revise them throughout the release cycle.",
		"The first part of the plan deals with the important matters of release deliverables, release milestones, target operating environments, and release-to-release compatibility. These are all things that need to be clear for any release, even if no features were to change.",
		"The remainder of the plan consists of plan items for the various Eclipse subprojects. Each plan item covers a feature or API that is to be added to Eclipse, or some aspect of Eclipse that is to be improved. Each plan item has its own entry in the Eclipse bugzilla database, with a title and a concise summary (usually a single paragraph) that explains the work item at a suitably high enough level so that everyone can readily understand what the work item is without having to understand the nitty-gritty detail.",
	};
	int[] alignments = {SWT.LEFT, SWT.CENTER, SWT.RIGHT};
	final TextLayout[] layouts = new TextLayout[texts.length];
	for (int i = 0; i < layouts.length; i++) {
		TextLayout layout = new TextLayout(display);
		layout.setText(texts[i]);
		layout.setIndent(30);
		layout.setJustify(true);
		layout.setAlignment(alignments[i]);
		layouts[i] = layout;
	}
	shell.addListener(SWT.Paint, event -> {
		Point point = new Point(10, 10);
		int width = shell.getClientArea().width - 2 * point.x;
		for (int i = 0; i < layouts.length; i++) {
			TextLayout layout = layouts[i];
			layout.setWidth(width);
			layout.draw(event.gc, point.x, point.y);
			point.y += layout.getBounds().height + 10;
		}
	});
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	for (int i = 0; i < layouts.length; i++) {
		layouts[i].dispose();
	}
	display.dispose();
}
}
