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
 * SWT StyledText snippet: use gradient background.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet218 {

	static String text = "Plans do not materialize out of nowhere, nor are they entirely static. To ensure the planning process is " +
		"transparent and open to the entire Eclipse community, we (the Eclipse PMC) post plans in an embryonic "+
		"form and revise them throughout the release cycle. \n"+
		"The first part of the plan deals with the important matters of release deliverables, release milestones, target "+
		"operating environments, and release-to-release compatibility. These are all things that need to be clear for "+
		"any release, even if no features were to change.  \n";
	static Image oldImage;

	public static void main(String [] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final StyledText styledText = new StyledText(shell, SWT.WRAP | SWT.BORDER);
		styledText.setText(text);
		FontData data = display.getSystemFont().getFontData()[0];
		Font font = new Font(display, data.getName(), 16, SWT.BOLD);
		styledText.setFont(font);
		styledText.setForeground(display.getSystemColor (SWT.COLOR_BLUE));
		styledText.addListener (SWT.Resize, event -> {
			Rectangle rect = styledText.getClientArea ();
			Image newImage = new Image (display, 1, Math.max (1, rect.height));
			GC gc = new GC (newImage);
			gc.setForeground (display.getSystemColor (SWT.COLOR_WHITE));
			gc.setBackground (display.getSystemColor (SWT.COLOR_YELLOW));
			gc.fillGradientRectangle (rect.x, rect.y, 1, rect.height, true);
			gc.dispose ();
			styledText.setBackgroundImage (newImage);
			if (oldImage != null) oldImage.dispose ();
			oldImage = newImage;
		});
		shell.setSize(700, 400);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		if (oldImage != null) oldImage.dispose ();
		font.dispose();
		display.dispose();
	}
}
