package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/*
 * Title: Bug 166720 - [TableEditor] flickers quite a bit when scrolling - Linux GTK
 * How to run: launch snippet and scroll TableEditor.
 * Bug description: TableEditor flickers when scrolling.
 * Expected results: TableEditor should scroll smoothly.
 * GTK Version(s): GTK2.x
 */

public class Bug166720_TableEditorFlicker {
	public static void main(String[] args) {
	    Display display = new Display ();
	    Shell shell = new Shell (display);
	    shell.setLayout (new GridLayout ());
	    Table table = new Table (shell, SWT.BORDER | SWT.MULTI);
	    GridData gd = new GridData(GridData.FILL_BOTH);
	    gd.heightHint = 200;
	    gd.widthHint = 200;
	    table.setLayoutData(gd);

	    TableColumn column = new TableColumn(table, SWT.NONE);
	    column.setWidth (100);
	    for (int i=0; i<100; i++) {
	        new TableItem (table, SWT.NONE);
	    }
	    TableItem [] items = table.getItems ();
	    for (int i=0; i<items.length; i++) {
	        TableEditor editor = new TableEditor (table);
	        editor = new TableEditor (table);
	        Text text = new Text (table, SWT.NONE);
	        text.setText("Text" + i);
	        editor.grabHorizontal = true;
	        editor.setEditor(text, items[i], 0);
	        editor = new TableEditor (table);
	    }
	    shell.pack ();
	    shell.open ();
	    while (!shell.isDisposed ()) {
	        if (!display.readAndDispatch ()) display.sleep ();
	    }
	    display.dispose ();
	}
}