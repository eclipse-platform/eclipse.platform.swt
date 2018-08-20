/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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
 * example snippet: StyledText bulleted list example
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

public class Snippet222 {

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setText("StyledText Bullet Example");
	shell.setLayout(new FillLayout());
	final StyledText styledText = new StyledText (shell, SWT.FULL_SELECTION | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
	StringBuilder text = new StringBuilder();
	text.append("Here is StyledText with some bulleted lists:\n\n");
	for (int i = 0; i < 4; i++) text.append("Red Bullet List Item " + i + "\n");
	text.append("\n");
	for (int i = 0; i < 2; i++) text.append("Numbered List Item " + i + "\n");
	for (int i = 0; i < 4; i++) text.append("Sub List Item " + i + "\n");
	for (int i = 0; i < 2; i++) text.append("Numbered List Item " + (2 + i) + "\n");
	text.append("\n");
	for (int i = 0; i < 4; i++) text.append("Custom Draw List Item " + i + "\n");
	styledText.setText(text.toString());

	StyleRange style0 = new StyleRange();
	style0.metrics = new GlyphMetrics(0, 0, 40);
	style0.foreground = display.getSystemColor(SWT.COLOR_RED);
	Bullet bullet0 = new Bullet (style0);
	StyleRange style1 = new StyleRange();
	style1.metrics = new GlyphMetrics(0, 0, 50);
	style1.foreground = display.getSystemColor(SWT.COLOR_BLUE);
	Bullet bullet1 = new Bullet (ST.BULLET_NUMBER | ST.BULLET_TEXT, style1);
	bullet1.text = ".";
	StyleRange style2 = new StyleRange();
	style2.metrics = new GlyphMetrics(0, 0, 80);
	style2.foreground = display.getSystemColor(SWT.COLOR_GREEN);
	Bullet bullet2 = new Bullet (ST.BULLET_TEXT, style2);
	bullet2.text = "\u2713";
	StyleRange style3 = new StyleRange();
	style3.metrics = new GlyphMetrics(0, 0, 50);
	Bullet bullet3 = new Bullet (ST.BULLET_CUSTOM, style2);

	styledText.setLineBullet(2, 4, bullet0);
	styledText.setLineBullet(7, 2, bullet1);
	styledText.setLineBullet(9, 4, bullet2);
	styledText.setLineBullet(13, 2, bullet1);
	styledText.setLineBullet(16, 4, bullet3);

	styledText.addPaintObjectListener(event -> {
		Display display1 = event.display;
		StyleRange style = event.style;
		Font font = style.font;
		if (font == null) font = styledText.getFont();
		TextLayout layout = new TextLayout(display1);
		layout.setAscent(event.ascent);
		layout.setDescent(event.descent);
		layout.setFont(font);
		layout.setText("\u2023 1." + event.bulletIndex + ")");
		layout.draw(event.gc, event.x + 10, event.y);
		layout.dispose();
	});
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose ();
}
}
