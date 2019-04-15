/*******************************************************************************
 * Copyright (c) 2019 Syntevo and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Syntevo - initial implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.lang.reflect.Method;

public class Bug545587_TooltipColor {
	static void updateLabel(Label a_Label) {
		Display display = a_Label.getDisplay();

		// Force recalculate colors
		// Can be used with setting custom CSS in GTK inspector
		try {
			final Method setBackground = Display.class.getDeclaredMethod("initializeSystemColors");
			setBackground.setAccessible(true);
			setBackground.invoke(display);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}

		Color backColor = display.getSystemColor(SWT.COLOR_INFO_BACKGROUND);
		Color foreColor = display.getSystemColor(SWT.COLOR_INFO_FOREGROUND);

		a_Label.setBackground(backColor);
		a_Label.setForeground(foreColor);

		String labelText = String.format(
				"\n\nThese are expected tooltip's colors:\nback=%s\nfore=%s\n\nNow see tooltip and compare.",
				backColor.toString(),
				foreColor.toString()
		);
		a_Label.setText(labelText);
	}

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setLayout(new FillLayout());
		shell.setSize(300, 200);

		final Label label = new Label(shell, SWT.WRAP | SWT.CENTER);
		label.addListener(SWT.MouseDown, event -> updateLabel(label));
		updateLabel(label);

		final ToolTip tooltip = new ToolTip (shell, 0);
		tooltip.setText("Example tooltip text");
		tooltip.setVisible(true);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
