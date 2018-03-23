/*******************************************************************************
 * Copyright (c) 2018 Simeon Andreev and others. All rights reserved.
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
 *     Simeon Andreev - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * Description: {@link ControlEditor} editors are not drawn.
 * Steps to reproduce:
 * <ol>
 * <li>Run the snippet.</li>
 * <li>Scroll horizontally so that the first column is no longer visible.</li>
 * </ol>
 * Expected results: The combo editor in the first row and first column, and
 *                   the text editor in the first row and second column,
 *                   are visible and can be interacted with.
 * Actual results: The two editors are not visible but can be interacted with.
 */
public class Bug531928_TableTreeEditorVisibility {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setSize(300, 200);
		shell.setText("Bug 531928");

		table(shell);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void table(Shell shell) {
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout());

		Table table = new Table(composite, SWT.BORDER);
		TableColumn a = new TableColumn(table, SWT.NONE);
		a.setText("column 1");
		a.setWidth(140);
		TableColumn b = new TableColumn(table, SWT.NONE);
		b.setText("column 2");
		b.setWidth(140);
		TableColumn c = new TableColumn(table, SWT.NONE);
		c.setText("column 2");
		c.setWidth(140);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableItem item1 = new TableItem(table, SWT.NONE);
		item1.setText(new String[] { "combo11", "text12", "other13" });
		TableItem item2 = new TableItem(table, SWT.NONE);
		item2.setText(new String[] { "other21", "other22", "other23" });

		TableEditor comboEditor1 = new TableEditor(table);
		Combo combo = new Combo(table, SWT.READ_ONLY);
		combo.add("item 1");
		combo.add("item 2");
		comboEditor1.grabHorizontal = true;
		comboEditor1.setEditor(combo, item1, 0);

		TableEditor textEditor = new TableEditor(table);
		Text text = new Text(table, SWT.NONE);
		text.setText("something");
		textEditor.grabHorizontal = true;
		textEditor.setEditor(text, item1, 1);
	}
}
