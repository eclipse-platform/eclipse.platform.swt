/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
 * SWT StyledText snippet: different types of indent and combining wrap indent and bulleted lists .
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.2
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;

public class Snippet331 {

	static String text = 
		"The first paragraph has an indentation of fifty pixels and zero indentation for wrapped lines. If this paragraph wraps to several lines you will see the indentation only on the first line.\n\n" +
		"The second paragraph has an indentation of fifty pixels for all lines in the paragraph. Visually this paragraph has a fifty pixels left margin.\n\n" +
		"The third paragraph has wrap indentation of fifty pixels and zero indentation for the first line. If this paragraph wraps to several lines you should see the indentation for all the lines but the first.\n\n" +
		"This paragraph start with a bullet and does not have any kind of indentation. If this paragraph wraps to several lines, the wrapped lines will start a lead edge of the editor.\n\n" +
		"This paragraph start with a bullet and has wrap indentation with the same width of the bullet. If this paragraph wraps to several lines, all the wrapped lines will line up with the first one.";
	
	public static void main(String [] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		StyledText styledText = new StyledText(shell, SWT.WRAP | SWT.BORDER);
		styledText.setText(text);
		styledText.setLineIndent(0, 1, 50);
		styledText.setLineIndent(2, 1, 50);
		styledText.setLineWrapIndent(2, 1, 50);
		styledText.setLineWrapIndent(4, 1, 50);
		
		StyleRange style = new StyleRange();
		style.metrics = new GlyphMetrics(0, 0, 50);
		Bullet bullet = new Bullet (style);
		styledText.setLineBullet(6, 1, bullet);
		styledText.setLineBullet(8, 1, bullet);
		styledText.setLineWrapIndent(8, 1, 50);

		shell.setSize(300, 500);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
