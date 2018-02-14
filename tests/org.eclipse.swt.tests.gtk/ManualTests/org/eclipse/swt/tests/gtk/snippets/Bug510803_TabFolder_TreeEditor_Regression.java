/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
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
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/*
 * Title: Modified Snippet88, table cell editor.
 *   Snippet88 creates Cell Editor on every new click.
 *   This snippet has a passive table editor that is created once, is initially invisible and is set to be visible when needed.
 *
 *   This snippet emulates refactoring dialogues like "change method signature", where you have a Table with TableEditor inside a tab folder.
 *
 * How to run: Run snippet. Try to edit cells.
 * Bug description: Passive table/tree editors were not working.
 * Expected results: Cells can be edited.
 * GTK Version(s): Gtk3.22, Gtk2.24. x11/Wayland.
 */
public class Bug510803_TabFolder_TreeEditor_Regression {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		{ // Part one : Tab folder who's direct item is a Table.
			final TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
			TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
			tabItem.setText("One Control");
			Table table = makeTableWithPassiveControlEditor(tabFolder);
			tabItem.setControl(table);
		}

		{	// Part two : TabItem who has a composite, who then has a table with editor.
			// To verify that if a tabItem has a composite, it's children are traversed properly.
			final TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
			TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
			tabItem.setText("Composite with Children");

			Composite composite = new Composite(tabFolder, SWT.NONE);
			composite.setLayout(new FillLayout(SWT.VERTICAL));
			new Button(composite, SWT.PUSH).setText("Dummy button");

			makeTableWithPassiveControlEditor(composite);
			tabItem.setControl(composite);
		}

		shell.setSize(300, 300);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	private static Table makeTableWithPassiveControlEditor(final Composite parent) {
		final Table table = new Table(parent, SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		TableColumn column1 = new TableColumn(table, SWT.NONE);
		for (int i = 0; i < 10; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText("Item " + i);
		}
		column1.pack();

		final TableEditor tableEditor = new TableEditor(table);
		tableEditor.horizontalAlignment = SWT.LEFT;
		tableEditor.grabHorizontal = true;
		tableEditor.minimumWidth = 50;

		final Text tableEditorControl = new Text(table, SWT.NONE);
		// Location of setControl() method call has an impact.
		// If it's before 'Text tableEditorControl = new Text(table, SWT.NONE);', then things work.

		tableEditorControl.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
		tableEditorControl.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		tableEditorControl.setText("Click some row");
		tableEditorControl.addModifyListener(me -> {
			if (tableEditor.getItem() != null) {
				table.getSelection()[0].setText(tableEditorControl.getText());
			}
		});
		tableEditor.setEditor(tableEditorControl);
		tableEditorControl.setVisible(false);
		tableEditor.setColumn(0);

		table.addSelectionListener(widgetSelectedAdapter(e -> {
				TableItem item = (TableItem) e.item;
				if (item == null) return;
				tableEditorControl.setVisible(true);
				tableEditorControl.setText(item.getText());
				tableEditorControl.selectAll();
				tableEditorControl.setFocus();
				tableEditor.setItem(item);
			}));
		return table;
	}
}
